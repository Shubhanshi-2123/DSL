// === Folder Structure ===
folder('COE') {}
folder('COE/CI') {}
folder('COE/CI/SentryFuse') {}

def repoUrl = 'https://github.com/OT-COE/SentryFuse.git'
def githubCredId = 'sharvarikhamkar1304-creds' // replace with actual credential ID

// === Backend Consumer Pipeline ===
pipelineJob('COE/CI/SentryFuse/Backend-Consumer') {
    displayName('Backend-Consumer')
    description('CI/CD pipeline for the SentryFuse Backend Consumer')

    // Keep builds for 1 day, max 1 build
    logRotator {
        daysToKeep(1)
        numToKeep(1)
    }

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url(repoUrl)
                        credentials(githubCredId)
                    }
                    branch('*/main')
                }
            }
            scriptPath('backend-consumer/Jenkinsfile')
            lightweight() // Enable lightweight checkout
        }
    }

    // Only disable concurrent builds if needed; remove this block if not required
    // properties {
    //     disableConcurrentBuilds()
    // }

    // configure { project ->
    //     // GitHub webhook trigger for SCM polling
    //     project / triggers << 'com.cloudbees.jenkins.GitHubPushTrigger' {
    //         spec ''
    //     }
    // }
}
