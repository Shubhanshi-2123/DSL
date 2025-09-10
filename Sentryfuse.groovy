// === Folder Structure ===
folder('COE') {}
folder('COE/CI') {}
folder('COE/CI/SentryFuse') {}

def repoUrl = 'https://github.com/OT-COE/SentryFuse.git'
def githubCredId = 'sharvarikhamkar1304-creds' // replace with actual credential ID

// === Backend API ===
pipelineJob('COE/CI/SentryFuse/Backend-Api') {
    displayName('Backend-API')
    description('CI/CD pipeline for the SentryFuse Backend API')

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url(repoUrl)
                        credentials(githubCredId)
                    }
                    branch('*/CI-CD')
                }
            }
            scriptPath('backend-api/Jenkinsfile')
        }
    }

    logRotator {
        daysToKeep(2)
        numToKeep(10)
    }

    properties {
        disableConcurrentBuilds()
    }

    configure { project ->
        // GitHub webhook trigger
        project / triggers << 'com.cloudbees.jenkins.GitHubPushTrigger' {
            spec ''
        }

        // Durability hint (disable resume on controller restart)
        project / 'properties' << 'org.jenkinsci.plugins.workflow.job.properties.DurabilityHintJobProperty' {
            hint 'PERFORMANCE_OPTIMIZED'
        }
    }
}

// === Backend Consumer ===
pipelineJob('COE/CI/SentryFuse/Backend-Consumer') {
    displayName('Backend-Consumer')
    description('CI/CD pipeline for the SentryFuse Backend Consumer')

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url(repoUrl)
                        credentials(githubCredId)
                    }
                    branch('*/CI-CD')
                }
            }
            scriptPath('backend-consumer/Jenkinsfile')
        }
    }

    logRotator {
        daysToKeep(2)
        numToKeep(10)
    }

    properties {
        disableConcurrentBuilds()
    }

    configure { project ->
        project / triggers << 'com.cloudbees.jenkins.GitHubPushTrigger' {
            spec ''
        }

        project / 'properties' << 'org.jenkinsci.plugins.workflow.job.properties.DurabilityHintJobProperty' {
            hint 'PERFORMANCE_OPTIMIZED'
        }
    }
}
