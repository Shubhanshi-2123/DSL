// === Folder Structure ===
folder('central-sre-poc') {}
folder('central-sre-poc/dev') {}

pipelineJob('central-sre-poc/dev/helmUtils-pipeline') {
    displayName('Helm Utils Pipeline')
    description('Pipeline for executing Helm utilities from shared libraries')

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('https://github.com/akashrajvanshi-opst/jenkins-shared-libraries.git')
                        credentials('akashrajvanshi-opst') 
                    }
                    branch('main')
                }
            }
            scriptPath('cd/dev/helmUtils.groovy')
        }
    }
}
