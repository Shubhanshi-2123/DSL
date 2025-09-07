pipelineJob('test-checkov') {
    description('Pipeline to test Checkov scan using Tomcat-001 Jenkinsfile')

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('https://github.com/Priyanshu498/Tomcat-001.git')
                        // No credentials 
                    }
                    branch('*/main')
                }
            }
            scriptPath('Jenkinsfile')
        }
    }
}
