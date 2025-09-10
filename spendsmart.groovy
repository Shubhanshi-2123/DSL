folder('COE') {
    displayName('COE')
    description('All COE related jobs')
}

folder('COE/SpendSmart') {
    displayName('SpendSmart')
    description('SpendSmart related jobs')
}

folder('COE/SpendSmart/AWS') {
    displayName('AWS')
    description('AWS related SpendSmart jobs')
}

folder('COE/SpendSmart/AWS/Cost-Tracker') {
    displayName('Cost Tracker')
    description('Cost tracking jobs')
}

folder('COE/SpendSmart/AWS/Cost-Tracker/HRC') {
    displayName('HRC')
    description('HRC specific cost tracking jobs')
}


pipelineJob('COE/SpendSmart/AWS/Cost-Tracker/HRC/Daily-Cost-Job') {
    description('HRC Daily Cost Tracker job using shared Jenkinsfile')

    // Disable concurrent builds
    configure { project ->
        project / 'properties' << 'org.jenkinsci.plugins.workflow.job.properties.DisableConcurrentBuildsJobProperty'()
    }

    parameters {
        stringParam('BENCHMARK_COST', '30000', 'Benchmark cost threshold')
        stringParam('account_name', 'HRC', 'Account display name')
        booleanParam('USE_ASSUME_ROLE', false, 'Use IAM Assume Role?')
        stringParam('ROLE_ARN', 'arn:aws:iam::879381273882:role/Aman', 'Role ARN if AssumeRole is enabled')
        stringParam('SENDER', 'spendsmart@opstree.us', 'Sender email')
        textParam('RECIPIENTS', '''aman.raj@opstree.com,
sandeep@opstree.com,
piyush.upadhyay@opstree.com,
deepak.nishad@opstree.com,
rajat.vats@opstree.com,
prashant.sharma@opstree.com,
saurabh.kumar@opstree.com,
neelesh.rajput@opstree.com''', 'Comma-separated recipient emails')
    }

    triggers {
        cron('H 15 * * *')
    }

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('https://github.com/ot-central-team/cloudcost-tracker.git')
                        credentials('aman-dw00')
                    }
                    branch('Revamped')
                }
            }
            scriptPath('HRC_Daily_Cost_Jenkinsfile')
            lightweight(true)
        }
    }
}
