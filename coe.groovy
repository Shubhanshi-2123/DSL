// === Folder Structure ===
folder('COE') {}
folder('COE/CI') {}
folder('COE/CI/Spend-Smart') {}

// === CI-pipeline Job ===
pipelineJob('COE/CI/Spend-Smart/CI-pipeline') {
    displayName('CI-pipeline')
    description('Pipeline for Spend-Smart CI from SCM')

    // Branches to build from Git
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('https://github.com/OT-COE/UnitEconPro.git')
                        credentials('dnishad04-creds') // replace with the actual credentials ID
                    }
                    branches('*/unified-ingestion')
                    extensions {
                        // lightweight checkout
                        wipeWorkspace()
                    }
                }
            }
            scriptPath('Jenkinsfile')
            lightweight(true)
        }
    }
}
