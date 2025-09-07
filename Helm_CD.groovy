folder('Uniticonpro') {
    description('Folder for Uniticonpro pipelines')
}



pipelineJob('Uniticonpro/Helm-CD') {
  
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('https://github.com/ot-central-team/UnitEconPro.git')
                        credentials('dnishad04')   // Jenkins credential ID
                    }
                    branch('*/unified-ingestion')
                }
            }
            scriptPath('JenkinsfileCD')
            lightweight(true)   // enable lightweight checkout
        }
    }
}
