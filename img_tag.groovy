folder('Uniticonpro') {
    description('Folder for Uniticonpro pipelines')
}
pipelineJob('Uniticonpro/img-tag-ci') {
    definition {
        cps {
            script("""
                pipeline {
                    agent any
                    stages {
                        stage('Build') {
                            steps {
                                echo "Running image tag CI pipeline for Uniticonpro..."
                                // Add your steps here
                            }
                        }
                    }
                }
            """.stripIndent())
            sandbox(true)   // allow running in Groovy sandbox
        }
    }
}
