pipelineJob('demo-github-template') {
    description('Pipeline job for GitHub repository template demo')

    parameters {
        stringParam('GITHUB_ORG', 'Priyab0612', 'GitHub organization or username')
        stringParam('REPO_NAME', 'priyansh-tf-demo', 'Name of the new GitHub repository')
    }

    definition {
        cps {
            sandbox(true)
            script('''
                pipeline {
                    agent any

                    parameters {
                        string(name: "GITHUB_ORG", defaultValue: "Priyab0612", description: "GitHub organization or username")
                        string(name: "REPO_NAME", defaultValue: "priyansh-tf-demo", description: "Name of the new GitHub repository")
                    }

                    stages {
                        stage("Init") {
                            steps {
                                echo "Using GitHub Org: ${params.GITHUB_ORG}"
                                echo "Target Repo Name: ${params.REPO_NAME}"
                            }
                        }

                        stage("Setup Repo") {
                            steps {
                                echo "Here you can add logic to create/configure repo: ${params.GITHUB_ORG}/${params.REPO_NAME}"
                                // Example: API call to GitHub or Terraform apply
                            }
                        }

                        stage("Done") {
                            steps {
                                echo "Pipeline completed for ${params.REPO_NAME}"
                            }
                        }
                    }
                }
            ''')
        }
    }
}
