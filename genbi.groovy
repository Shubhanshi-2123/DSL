// === Folder Structure ===
folder('COE') {}
folder('COE/CI') {}
folder('COE/CI/AI-Tools') {}

// === Gen-BI Pipeline ===
def repoUrl = 'https://github.com/OT-COE/GenBI.git'
def githubCredId = 'Prashantdev780' // replace with actual credential ID

pipelineJob('COE/CI/AI-Tools/Gen-BI') {
    displayName('Gen-BI')

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url(repoUrl)
                        credentials(githubCredId)
                    }
                    branch('*/JenkinsCI')
                    extensions { 
                        // Lightweight checkout
                        cloneOptions {
                            shallow(true)
                            depth(1)
                        }
                    }
                }
            }
            scriptPath('Jenkinsfile.CI')
            lightweight(true)
        }
    }
}
