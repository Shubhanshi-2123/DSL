// === Folder Creation ===
folder('POC') {
    description('Proof of Concept Jobs')
}

// === Notification Job ===
pipelineJob('POC/notification') {
    displayName('Notification Pipeline')
    description('POC pipeline for notification test using attendance-api repo')

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('https://github.com/ayush090909/attendance-api.git')
                        credentials('ayush090909') 
                    }
                    branch('*/main')
                }
            }
            scriptPath('Jenkinsfile')
        }
    }
}
