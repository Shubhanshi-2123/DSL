// Create folder
folder('COE') {
    displayName('COE')
}

// Create pipeline job
pipelineJob('COE/Graviton-Compatibility-Scan') {
    description('Pipeline to scan Git repositories for Graviton compatibility')

    // Discard old builds
    logRotator {
        numToKeep(5)
    }

    // Do not allow concurrent builds (like in your UI)
    disableConcurrentBuilds()

    // Pipeline definition
    definition {
        cps {
            script("""
                pipeline {
                    agent any
                    environment {
                        REPORT_DIR = "graviton_report"
                    }
                    parameters {
                        string(name: 'REPO_URL', defaultValue: '', description: 'Git repository to scan')
                        booleanParam(name: 'IS_PRIVATE_REPO', defaultValue: false, description: 'Is the repo private?')
                        string(name: 'GIT_USERNAME', defaultValue: '', description: 'Git username (for private repo)')
                        password(name: 'GIT_TOKEN', defaultValue: '', description: 'Git token/password (for private repo)')
                    }
                    stages {
                        stage('Clone Repo') {
                            steps {
                                script {
                                    sh 'rm -rf scanned_repo'
                                    if (params.IS_PRIVATE_REPO) {
                                        // Insert credentials into URL
                                        def authRepoUrl = params.REPO_URL.replaceFirst('https://', "https://${params.GIT_USERNAME}:${params.GIT_TOKEN}@")
                                        sh "git clone \${authRepoUrl} scanned_repo"
                                    } else {
                                        sh "git clone \${params.REPO_URL} scanned_repo"
                                    }
                                }
                            }
                        }

                        stage('Run Porting Advisor') {
                            steps {
                                sh '''
                                mkdir -p \${REPORT_DIR}
                                docker run --rm -v \$(pwd)/scanned_repo:/repo -v \$(pwd)/\${REPORT_DIR}:/report ariefhidayat/graviton-porting-advisor:1.1.1 \\
                                  --output /report/report.html --output-format html /repo
                                '''
                            }
                        }

                        stage('Archive Report') {
                            steps {
                                archiveArtifacts artifacts: "\${REPORT_DIR}/*", fingerprint: true
                            }
                        }
                    }
                    post {
                        always {
                            echo "Job completed. Check archived report."
                            cleanWs()
                        }
                    }
                }
            """.stripIndent())
            sandbox()
        }
    }
}
