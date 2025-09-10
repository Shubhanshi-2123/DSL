// === Folder Structure ===
folder('COE') {}
folder('COE/CI') {}
folder('COE/CI/SentryFuse') {}

def repoUrl = 'https://github.com/OT-COE/SentryFuse.git'
def githubCredId = 'sharvarikhamkar1304-creds' // replace with actual credential ID

// === Backend Webhook Multibranch Pipeline ===
multibranchPipelineJob('COE/CI/SentryFuse/Backend-Webhook') {
    displayName('Backend-Webhook')
    description('Multibranch pipeline for Backend Webhook')

    branchSources {
        github {
            id('backend-webhook-github') // unique ID
            repoOwner('OT-COE')
            repository('SentryFuse')
            scanCredentialsId('sharvarikhamkar1304-creds') // correct method
            includes('*') // build all branches
        }
    }

    orphanedItemStrategy {
        discardOldItems {
            daysToKeep(1)
            numToKeep(1)
        }
    }

    triggers {
        githubPush() // webhook trigger
    }

    factory {
        workflowBranchProjectFactory {
            scriptPath('backend-webhook/Jenkinsfile')
        }
    }
}
