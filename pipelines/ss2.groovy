// pipeline {
//     agent {
//         docker {
//             image 'i4imran/scoutsuite:v2' 
//         }
//     }

//     parameters {
//         string(name: 'ROLE_ARN', defaultValue: 'arn:aws:iam::370389955750:role/scout-suite-security-role', description: 'Enter the Role ARN to assume')
//         string(name: 'ACCOUNT_NAME', defaultValue: 'my-aws-account', description: 'Name of the AWS account being scanned')
//         string(name: 'EMAIL_RECIPIENT', defaultValue: 'your-email@example.com', description: 'Email address to send the report to')
//     }

//     environment {
//         SCOUT_OUTPUT_DIR = 'scoutsuite-report'
//         ROLE_SESSION_NAME = 'jenkins-scout-session'
//         ZIP_FILE = "scout-report-${params.ACCOUNT_NAME}.zip"
//     }

//     stages {
//         stage('Scout Suite Scan') {
//             steps {
//                 withAWS(credentials: 'aws-scout', region: "${params.AWS_REGION}", role: "${params.ROLE_ARN}", roleSessionName: "${env.ROLE_SESSION_NAME}") {
//                     sh "scout aws --report-dir $SCOUT_OUTPUT_DIR --result-format json"
                    
//                 }
//             }
//         }

// 	stage('Generate Markdown Report') {
//     steps {
//         script {
//             def pyScript = '''#!/usr/bin/env python3
// import json
// import glob
// from datetime import datetime

// # Load the JSON data
// file_list = glob.glob("scoutsuite-report/scoutsuite-results/scoutsuite_results*.js")
// if not file_list:
//     raise FileNotFoundError("No ScoutSuite result file found.")

// with open(file_list[0], "r") as file:
//     raw_data = file.read().replace("scoutsuite_results =", "")
//     data = json.loads(raw_data)

// # Extract metadata
// account_id = data.get("account_id")
// date = data["last_run"].get("time", "").split(" ")[0]
// issues = data["last_run"]["summary"]

// # Initialize severity and extract top services by checked_items
// severity_count = {"Critical": 0, "High": 0, "Medium": 0, "Low": 0}
// severity_map = {"danger": "Critical", "high": "High", "warning": "Medium", "info": "Low"}

// service_data = []
// total_flagged = 0

// for service, details in issues.items():
//     checked = details.get("checked_items", 0)
//     flagged = details.get("flagged_items", 0)
//     max_level = severity_map.get(details.get("max_level", "info"), "Low")

//     if flagged > 0:
//         severity_count[max_level] += flagged
//         total_flagged += flagged

//     service_data.append({
//         "service": service,
//         "checked": checked,
//         "flagged": flagged,
//         "max_severity": max_level
//     })

// # Sort by checked items and pick top 10
// top_services = sorted(service_data, key=lambda x: x["checked"], reverse=True)[:10]

// # Compute column widths for aligned markdown
// def format_row(row, col_widths):
//     return "| " + " | ".join(f"{str(val).ljust(width)}" for val, width in zip(row, col_widths)) + " |"

// headers = ["Service", "Checked Items", "Flagged Items", "Max Severity"]
// rows = [[s["service"], s["checked"], s["flagged"], s["max_severity"]] for s in top_services]
// col_widths = [max(len(str(cell)) for cell in col) for col in zip(*([headers] + rows))]

// # Build Markdown
// report = f"""# 🧾 Cloud Security Assessment Report

// **Client**: [Client Name Here]  
// **Environment**: AWS Account {account_id}  
// **Date**: {date}  
// **Prepared By**: Opstree AWS Team

// ---

// ## 1. Executive Summary

// ### Objective
// Conduct a cloud security posture assessment to identify potential misconfigurations and security risks.

// ### Scan Summary
// - **Total Issues Identified**: {total_flagged}
//   - Critical: {severity_count['Critical']}
//   - High: {severity_count['High']}
//   - Medium: {severity_count['Medium']}
//   - Low: {severity_count['Low']}

// ---

// ## 2. Risk Heatmap

// | **Severity** | **Count** |
// |--------------|-----------|
// | 🔴 Critical  | {severity_count['Critical']} |
// | 🟠 High      | {severity_count['High']} |
// | 🟡 Medium    | {severity_count['Medium']} |
// | 🟢 Low       | {severity_count['Low']} |

// ---

// ## 3. Key Findings

// | **Service** | **Checked Items** | **Flagged Items** | **Max Severity** |
// |-------------|-------------------|-------------------|------------------|
// """

// # Add each service row
// for row in rows:
//     report += format_row(row, col_widths) + "\\n"

// # Write to markdown file
// with open("scoutsuite-report/ScoutSuite_Generated_Report.md", "w") as f:
//     f.write(report)

// print("Markdown report generated: ScoutSuite_Generated_Report.md")
// '''
//             writeFile file: 'generate_md.py', text: pyScript
//             sh 'python3 generate_md.py'
//         }
//     }
// }


// 	stage('Inject Metadata') {
// 	    steps {
// 		sh """
// 		sed -i 's|\\*\\*Client\\*\\*: \\[Client Name Here\\]|**Client**: ${params.ACCOUNT_NAME}|' scoutsuite-report/ScoutSuite_Generated_Report.md
// 		"""
// 	    }
// 	}


//         stage('Archive Report') {
//             steps {
//                 sh "ls -la"
                
//                 // Zip the report directory
//                 sh "zip -r $ZIP_FILE $SCOUT_OUTPUT_DIR"
                
//                 // Archive the zip for Jenkins
//                 archiveArtifacts artifacts: "${SCOUT_OUTPUT_DIR}/**", fingerprint: true
//             }
//         }
        
//         stage('Send Email Report') {
//             steps {
//                 emailext (
//                     subject: "Scout Suite Report for ${params.ACCOUNT_NAME}",
//                     body: "Attached is the Scout Suite security scan report for AWS account: ${params.ACCOUNT_NAME} in region ${params.AWS_REGION}.",
//                     to: "${params.EMAIL_RECIPIENT}",
//                     attachLog: false,
//                     attachmentsPattern: "scoutsuite-report/ScoutSuite_Generated_Report.md"
//                 )
//             }
//         }

        
//     }

//     post {
        
//         always {
//             echo "Cleaning up workspace"
//             sh 'rm -rf venv $SCOUT_OUTPUT_DIR $ZIP_FILE'
//         }
//     }
// }
