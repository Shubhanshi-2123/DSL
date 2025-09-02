pipelineJob('checkov') {
    displayName('Checkov Pipeline')
    description('Pipeline job for running Checkov scan from Jenkinsfile')

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('https://github.com/Priyanshu498/Tomcat-001.git')
                        // No credentials configured
                    }
                    branch('*/main')
                }
            }
            scriptPath('Jenkinsfile')
        }
    }
}
