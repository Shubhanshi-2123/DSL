pipeline {
    agent {
        docker {
            image 'i4imran/scoutsuite:v2' 
        }
    }

    parameters {
        string(name: 'ROLE_ARN', defaultValue: 'arn:aws:iam::370389955750:role/scout-suite-security-role', description: 'Enter the Role ARN to assume')
        string(name: 'ACCOUNT_NAME', defaultValue: 'my-aws-account', description: 'Name of the AWS account being scanned')
        string(name: 'EMAIL_RECIPIENT', defaultValue: 'your-email@example.com', description: 'Email address to send the report to')
    }

    environment {
        SCOUT_OUTPUT_DIR = 'scoutsuite-report'
        ROLE_SESSION_NAME = 'jenkins-scout-session'
        ZIP_FILE = "scout-report-${params.ACCOUNT_NAME}.zip"
    }

    stages {
        stage('Scout Suite Scan') {
            steps {
                withAWS(credentials: 'aws-scout', region: "${params.AWS_REGION}", role: "${params.ROLE_ARN}", roleSessionName: "${env.ROLE_SESSION_NAME}") {
                    sh "scout aws --region ${params.AWS_REGION} --report-dir $SCOUT_OUTPUT_DIR --result-format json"
                    
                }
            }
        }

	stage('Generate Markdown Report') {
    	    steps {
        	script {
            		def pyScript = '''#!/usr/bin/env python3
import json
import glob
from datetime import datetime

# Load the JSON data
file_list = glob.glob("scoutsuite-report/scoutsuite-results/scoutsuite_results*.js")
if not file_list:
    raise FileNotFoundError("No ScoutSuite result file found.")

with open(file_list[0], "r") as file:
    raw_data = file.read().replace("scoutsuite_results =", "")
    data = json.loads(raw_data)
    
# Extract metadata
account_id = data.get("account_id")
date = data["last_run"].get("time", "").split(" ")[0]
issues = data["last_run"]["summary"]

# Initialize counters
severity_count = {"Critical": 0, "High": 0, "Medium": 0, "Low": 0}
detailed_findings = []

# Severity map for max_level
severity_map = {
    "danger": "Critical",
    "high": "High",
    "warning": "Medium",
    "info": "Low"
}

# Collect findings
for service, details in issues.items():
    flagged = details.get("flagged_items", 0)
    max_level = severity_map.get(details.get("max_level", "info"), "Low")
    if flagged > 0:
        severity_count[max_level] += flagged
        detailed_findings.append({
            "service": service,
            "risk_level": max_level,
            "flagged": flagged,
            "recommendation": f"Review security posture for {service.upper()} and resolve {flagged} flagged item(s)."
        })

# Build markdown report
report = f"""# 🧾 Cloud Security Assessment Report

**Client**: [Client Name Here]  
**Environment**: AWS Account {account_id}  
**Date**: {date}  
**Prepared By**: Opstree AWS Team

---

## 1. Executive Summary

### Objective
Conduct a cloud security posture assessment to identify potential misconfigurations and security risks.

### Scan Summary
- **Total Issues Identified**: {sum(severity_count.values())}
  - Critical: {severity_count['Critical']}
  - High: {severity_count['High']}
  - Medium: {severity_count['Medium']}
  - Low: {severity_count['Low']}

### Key Risks
- Services with the highest flagged items: {', '.join(sorted([d['service'] for d in detailed_findings], key=lambda x: -next(d['flagged'] for d in detailed_findings if d['service'] == x))[:4])}

### Summary Recommendations
- Review services with critical or high severity risks
- Apply principle of least privilege across IAM
- Enable CloudTrail and config in all regions
- Audit public access to storage and networking resources

---

## 2. Risk Heatmap

| **Severity** | **Count** |
|--------------|-----------|
| 🔴 Critical  | {severity_count['Critical']}         |
| 🟠 High      | {severity_count['High']}         |
| 🟡 Medium    | {severity_count['Medium']}         |
| 🟢 Low       | {severity_count['Low']}         |

---

## 3. Key Findings and Business Impact

| **Service** | **Risk Level** | **Flagged Items** | **Recommendation** |
|------------|----------------|-------------------|---------------------|
"""
for finding in detailed_findings:
    report += f"| {finding['service']} | {finding['risk_level']} | {finding['flagged']} | {finding['recommendation']} |\\n"

report += """

---

## 4. Remediation Roadmap

| **Priority** | **Tasks** | **Timeline** |
|--------------|-----------|--------------|
| High         | Resolve critical/high issues in IAM, EC2, S3, CloudTrail | 7 days |
| Medium       | Enable services like AWS Config, GuardDuty | 14–21 days |
| Low          | Clean up unused resources, enable tagging | 30 days |

---

## 5. Technical Appendix

Refer to the full ScoutSuite JSON for exact resource paths and security rule identifiers.
"""

# Write to .md file
with open("scoutsuite-report/ScoutSuite_Generated_Report.md", "w") as f:
    f.write(report)

print("Markdown report generated: ScoutSuite_Generated_Report.md")
'''

            writeFile file: 'generate_md.py', text: pyScript
            sh 'python3 generate_md.py'
        }
    }
}
	stage('Inject Client Name') {
	    steps {
		sh """
		sed -i 's|\\*\\*Client\\*\\*: \\[Client Name Here\\]|**Client**: ${params.ACCOUNT_NAME}|' scoutsuite-report/ScoutSuite_Generated_Report.md
		"""
	    }
	}


        stage('Archive Report') {
            steps {
                sh "ls -la"
                
                // Zip the report directory
                sh "zip -r $ZIP_FILE $SCOUT_OUTPUT_DIR"
                
                // Archive the zip for Jenkins
                archiveArtifacts artifacts: "${SCOUT_OUTPUT_DIR}/**", fingerprint: true
            }
        }
        
        stage('Send Email Report') {
            steps {
                emailext (
                    subject: "Scout Suite Report for ${params.ACCOUNT_NAME}",
                    body: "Attached is the Scout Suite security scan report for AWS account: ${params.ACCOUNT_NAME} in region ${params.AWS_REGION}.",
                    to: "${params.EMAIL_RECIPIENT}",
                    attachLog: false,
                    attachmentsPattern: "scoutsuite-report/ScoutSuite_Generated_Report.md"
                )
            }
        }

        
    }

    post {
        
        always {
            echo "Cleaning up workspace"
            sh 'rm -rf venv $SCOUT_OUTPUT_DIR $ZIP_FILE'
        }
    }
}
