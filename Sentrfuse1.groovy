// === Folder Structure ===
folder('COE') {}
folder('COE/CI') {}
folder('COE/CI/SentryFuse') {}

// === Backend API Pipeline ===
pipelineJob('COE/CI/SentryFuse/Backend-Api') {
    displayName('Backend-API')

    // === Log Rotation ===
    logRotator {
        daysToKeep(1)
        numToKeep(1)
    }

    // === Disable Concurrent Builds ===
    // properties {
    //     disableConcurrentBuilds()
    // }

    // === Pipeline SCM Configuration ===
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('https://github.com/OT-COE/SentryFuse.git')
                        credentials('sharvarikhamkar1304-creds')
                    }
                    branch('*/main')
                    // Optional: Lightweight checkout
                    extensions {
                        lightweight(true)
                    }
                }
            }
            scriptPath('backend-api/Jenkinsfile')
        }
    }

  
}
