// === Folder Structure ===
folder('COE') {}
folder('COE/CI') {}
folder('COE/CI/COE-Tools') {}
folder('COE/CI/COE-Tools/Toolhub') {}

pipelineJob('COE/CI/COE-Tools/Toolhub/Toolhub-pipeline') {
    displayName('Toolhub Pipeline')
    description('Pipeline job for the Toolhub main branch')

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('https://github.com/OT-COE/ToolHub.git')
                        credentials('Prashantdev780/******') 
                    }
                    branch('*/main')
                }
            }
            scriptPath('jenkinsfile.CI')
            
        }
    }
}
