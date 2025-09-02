// === Folder Creation ===
folder('POC') {
    description('Proof of Concept Jobs')
}

// === python-utility Job ===
pipelineJob('POC/python-utility') {
    displayName('Python Utility Pipeline')
    description('Pipeline for running cloudcost-tracker utility on Revamped branch')

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('https://github.com/ot-central-team/cloudcost-tracker.git')
                        credentials('ayush090909') 
                    }
                    branch('Revamped')
                }
            }
            scriptPath('Jenkinsfile')
        }
    }
}
