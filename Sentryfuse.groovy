// // === Folder Structure ===
// folder('COE') {}
// folder('COE/CI') {}
// folder('COE/CI/SentryFuse') {}

// def repoUrl = 'https://github.com/OT-COE/SentryFuse.git'
// def githubCredId = 'sharvarikhamkar1304-creds' // replace with actual credential ID

// // === Backend API ===
// pipelineJob('COE/CI/SentryFuse/Backend-Api') {
//     displayName('Backend-API')
//     description('CI/CD pipeline for the SentryFuse Backend API')

//     definition {
//         cpsScm {
//             scm {
//                 git {
//                     remote {
//                         url(repoUrl)
//                         credentials(githubCredId)
//                     }
//                     branch('*/CI-CD')
//                 }
//             }
//             scriptPath('backend-api/Jenkinsfile')
//         }
//     }

//     logRotator {
//         daysToKeep(2)
//         numToKeep(10)
//     }

//     properties {
//         disableConcurrentBuilds()
//     }

//     configure { project ->
//         // GitHub webhook trigger
//         project / triggers << 'com.cloudbees.jenkins.GitHubPushTrigger' {
//             spec ''
//         }

//         // Durability hint (disable resume on controller restart)
//         project / 'properties' << 'org.jenkinsci.plugins.workflow.job.properties.DurabilityHintJobProperty' {
//             hint 'PERFORMANCE_OPTIMIZED'
//         }
//     }
// }

// // === Backend Consumer ===
// pipelineJob('COE/CI/SentryFuse/Backend-Consumer') {
//     displayName('Backend-Consumer')
//     description('CI/CD pipeline for the SentryFuse Backend Consumer')

//     definition {
//         cpsScm {
//             scm {
//                 git {
//                     remote {
//                         url(repoUrl)
//                         credentials(githubCredId)
//                     }
//                     branch('*/CI-CD')
//                 }
//             }
//             scriptPath('backend-consumer/Jenkinsfile')
//         }
//     }

//     logRotator {
//         daysToKeep(2)
//         numToKeep(10)
//     }

//     properties {
//         disableConcurrentBuilds()
//     }

//     configure { project ->
//         project / triggers << 'com.cloudbees.jenkins.GitHubPushTrigger' {
//             spec ''
//         }

//         project / 'properties' << 'org.jenkinsci.plugins.workflow.job.properties.DurabilityHintJobProperty' {
//             hint 'PERFORMANCE_OPTIMIZED'
//         }
//     }
// }
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
        project / triggers << 'com.cloudbees.jenkins.GitHubPushTrigger' {
            spec ''
        }

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

// === Backend Webhook (Multibranch) ===
multibranchPipelineJob('COE/CI/SentryFuse/Backend-Webhook') {
    displayName('Backend-Webhook')
    description('CI/CD multibranch pipeline for the SentryFuse Backend Webhook')

    branchSources {
        github {
            id('backend-webhook') // unique ID
            repoOwner('OT-COE')
            repository('SentryFuse')
            scanCredentialsId(githubCredId) // credentials

            configure { node ->
                def traitsNode = node / 'traits'
                traitsNode << 'jenkins.branch.BranchDiscoveryTrait' {
                    strategyId 1 // discover all branches
                }
            }
        }
    }

    factory {
        workflowBranchProjectFactory {
            scriptPath('backend-webhook/Jenkinsfile')
        }
    }

    orphanedItemStrategy {
        discardOldItems {
            numToKeep(10)
        }
    }

    triggers {
        githubPush() // GitHub webhook trigger
    }

    configure { node ->
        node / 'properties' << 'org.jenkinsci.plugins.workflow.job.properties.DurabilityHintJobProperty' {
            hint 'PERFORMANCE_OPTIMIZED'
        }
    }
}
