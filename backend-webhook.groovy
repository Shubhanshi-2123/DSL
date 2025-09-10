// === Folder Structure ===
folder('COE') {}
folder('COE/CI') {}
folder('COE/CI/SentryFuse') {}

def repoUrl = 'https://github.com/OT-COE/SentryFuse.git'
def githubCredId = 'sharvarikhamkar1304-creds' // replace with actual credential ID

// === Backend-Webhook Multibranch Pipeline ===
multibranchPipelineJob('COE/CI/SentryFuse/Backend-Webhook') {
    displayName('Backend-Webhook')
    description('CI/CD multibranch pipeline for the SentryFuse Backend Webhook')

    branchSources {
        github {
            id('backend-webhook') // unique ID for this branch source
            repoOwner('OT-COE')
            repository('SentryFuse')
            credentialsId(githubCredId)
            traits {
                branchDiscovery { strategyId(1) } // discover all branches
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
