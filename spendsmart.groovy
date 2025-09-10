folder('COE/SpendSmart/AWS/Cost-Tracker/HRC') {
    displayName('HRC')
    description('Jobs related to HRC Daily Cost Tracking')
}

pipelineJob('COE/SpendSmart/AWS/Cost-Tracker/HRC/Daily-Cost-Job') {
    description('HRC Daily Cost Tracker job using shared Jenkinsfile')

    // Disable concurrent builds
    concurrentBuild(false)

    // Parameters
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

    // Triggers (daily at 15:00 IST)
    triggers {
        cron('H 15 * * *')
    }

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('https://github.com/ot-central-team/cloudcost-tracker.git')
                        credentials('aman-dw00') // Jenkins credential ID
                    }
                    branch('Revamped')
                }
            }
            scriptPath('HRC_Daily_Cost_Jenkinsfile')
            lightweight(true)
        }
    }
}
