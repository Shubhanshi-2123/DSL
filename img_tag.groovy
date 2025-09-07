folder('Uniticonpro') {
    description('Folder for Uniticonpro pipelines')
}


pipelineJob('Uniticonpro/img-tag-ci') {
    definition {
        cps {
            script("""
                pipeline {
                    agent any

                    environment {
                        REGISTRY = 'registry.ldc.opstree.dev'
                        REPO = "\${REGISTRY}/ot-microservices/uniteconpro-sandbox"
                    }

                    stages {
                        stage('Checkout') {
                            steps {
                                git(
                                    branch: 'unified-ingestion',
                                    url: 'https://github.com/ot-central-team/UnitEconPro.git',
                                    credentialsId: 'uniteconpro'
                                )
                            }
                        }

                        stage('Set Image Version') {
                            steps {
                                script {
                                    def commitId = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
                                    env.IMAGE = "\${env.REPO}:\${commitId}"
                                    echo "Using image tag: \${env.IMAGE}"
                                }
                            }
                        }

                        stage('Docker Registry Login') {
                            steps {
                                withCredentials([usernamePassword(
                                    credentialsId: 'harbor-reg-creds',
                                    usernameVariable: 'REGISTRY_USER',
                                    passwordVariable: 'REGISTRY_PASS'
                                )]) {
                                    sh 'echo "\$REGISTRY_PASS" | docker login \$REGISTRY -u "\$REGISTRY_USER" --password-stdin'
                                }
                            }
                        }

                        stage('Docker Build & Push Image') {
                            steps {
                                sh '''
                                    docker build -t $IMAGE .
                                    docker push $IMAGE
                                '''
                            }
                        }
                    }

                    post {
                        success {
                            echo "docker image pushed: \${env.IMAGE}"
                        }
                        failure {
                            echo "build failed!"
                        }
                    }
                }
            """.stripIndent())
            sandbox()
        }
    }
}
