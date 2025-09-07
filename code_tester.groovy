pipelineJob('code_tester') {
    description('Pipeline job for code testing')

    parameters {
        stringParam('GIT_REPO_URL', 'https://github.com/OT-MICROSERVICES/salary-api.git', 'Git repo URL')
    }

    definition {
        cps {
            sandbox(true)
            script('''
                pipeline {
                    agent any

                    parameters {
                        string(name: "GIT_REPO_URL", defaultValue: "https://github.com/OT-MICROSERVICES/salary-api.git", description: "Git repo URL")
                    }

                    stages {
                        stage("Checkout") {
                            steps {
                                echo "Cloning repository: ${params.GIT_REPO_URL}"
                                git branch: 'main', url: "${params.GIT_REPO_URL}"
                            }
                        }

                        stage("Build") {
                            steps {
                                echo "Running build steps..."
                                // Add build steps here
                            }
                        }

                        stage("Test") {
                            steps {
                                echo "Running test steps..."
                                // Add test steps here
                            }
                        }
                    }
                }
            ''')
        }
    }
}
