// === Folder Structure ===
folder('COE') {}
folder('COE/CI') {}
folder('COE/CI/COE-Tools') {}
folder('COE/CI/COE-Tools/Orchestrator') {}

def commonRepo = 'git@github.com:OT-COE/PatchMaster.git'
def jenkinsCredId = 'ssh-key-OT-COE-org'

// === Backend API ===
multibranchPipelineJob('COE/CI/COE-Tools/Orchestrator/PatchMaster-Backend-API') {
    displayName('PatchMaster - Backend API')
    description('Multibranch pipeline for the Backend API module of PatchMaster')

    branchSources {
        git {
            id('patchmaster-backend-api-id')
            remote(commonRepo)
            credentialsId(jenkinsCredId)
        }
    }

    factory {
        workflowBranchProjectFactory {
            scriptPath('backend-api/Jenkinsfile')
        }
    }

    orphanedItemStrategy {
        discardOldItems {
            daysToKeep(7)
            numToKeep(10)
        }
    }

    configure { project ->
        // Git traits
        def traits = project / sources / data / 'jenkins.branch.BranchSource' / source / traits
        traits << 'jenkins.scm.impl.trait.BranchDiscoveryTrait' {
            strategyId(1)
        }
        traits << 'jenkins.scm.impl.trait.CleanBeforeCheckoutTrait' {}

        // Periodic trigger (every 5 minutes)
        project / triggers << 'com.cloudbees.hudson.plugins.folder.computed.PeriodicFolderTrigger' {
            interval '300000'
        }

        // Abort old builds
        project / 'properties' / 'org.jenkinsci.plugins.workflow.multibranch.JobPropertyImpl' {
            strategy '1'
        }
    }
}

// === Backend Consumer ===
multibranchPipelineJob('COE/CI/COE-Tools/Orchestrator/PatchMaster-Backend-Consumer') {
    displayName('PatchMaster - Backend Consumer')
    description('Multibranch pipeline for the Backend Consumer module of PatchMaster')

    branchSources {
        git {
            id('patchmaster-backend-consumer-id')
            remote(commonRepo)
            credentialsId(jenkinsCredId)
        }
    }

    factory {
        workflowBranchProjectFactory {
            scriptPath('backend-consumer/Jenkinsfile')
        }
    }

    orphanedItemStrategy {
        discardOldItems {
            daysToKeep(7)
            numToKeep(10)
        }
    }

    configure { project ->
        // Git traits
        def traits = project / sources / data / 'jenkins.branch.BranchSource' / source / traits
        traits << 'jenkins.scm.impl.trait.BranchDiscoveryTrait' {
            strategyId(1)
        }
        traits << 'jenkins.scm.impl.trait.CleanBeforeCheckoutTrait' {}

        // Periodic trigger (every 5 minutes)
        project / triggers << 'com.cloudbees.hudson.plugins.folder.computed.PeriodicFolderTrigger' {
            interval '300000'
        }

        // Abort old builds
        project / 'properties' / 'org.jenkinsci.plugins.workflow.multibranch.JobPropertyImpl' {
            strategy '1'
        }
    }
}

// === Orchestrator Frontend ===
pipelineJob('COE/CI/COE-Tools/Orchestrator/Orchestrator-Frontend') {
    displayName('Orchestrator Frontend')
    description('Pipeline job for the Orchestrator UI component')

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url(commonRepo)
                        credentials(jenkinsCredId)
                    }
                    branch('*/Orchestrator_frontend')
                }
            }
            scriptPath('orchestrator_UI/Jenkinsfile')
        }
    }
}
