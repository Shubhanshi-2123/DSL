// === Folder Structure ===
folder('COE') {}
folder('COE/CI') {}
folder('COE/CI/SentryFuse') {}

def githubCredId = 'sharvarikhamkar1304-creds'

// === Backend Webhook Multibranch Pipeline ===
multibranchPipelineJob('COE/CI/SentryFuse/Backend-Webhook') {
    displayName('Backend-Webhook')
    description('Multibranch pipeline for Backend Webhook')

    branchSources {
        github {
            id('backend-webhook-github') // unique ID
            repoOwner('OT-COE')
            repository('SentryFuse')
            scanCredentialsId(githubCredId)
            includes('main') // only build main branch
        }
    }

    orphanedItemStrategy {
        discardOldItems {
            daysToKeep(1)
            numToKeep(1)
        }
    }

    factory {
        workflowBranchProjectFactory {
            scriptPath('backend-webhook/Jenkinsfile')
        }
    }
}
