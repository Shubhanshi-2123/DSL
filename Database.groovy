// === Folder Structure ===
folder('COE') {}
folder('COE/k8s') {}
folder('COE/k8s/deployment') {}
folder('COE/k8s/deployment/database') {}

def repoUrl = 'https://github.com/ot-client/o11y-k8s-setup-template.git'
def credsId = 'rajkumar-opstree' 

// === MongoDB Pipeline ===
pipelineJob('COE/k8s/deployment/database/mongodb') {
    displayName('K8s Deployment - MongoDB')
    description('Deploy MongoDB to Kubernetes via Jenkinsfile')

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url(repoUrl)
                        credentials(credsId)
                    }
                    branch('main')
                }
            }
            scriptPath('jenkins/mongodb-jenkinsfile')
        }
    }
}

// === MySQL Pipeline ===
pipelineJob('COE/k8s/deployment/database/mysql') {
    displayName('K8s Deployment - MySQL')
    description('Deploy MySQL to Kubernetes via Jenkinsfile')

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url(repoUrl)
                        credentials(credsId)
                    }
                    branch('main')
                }
            }
            scriptPath('jenkins/mysql-jenkinsfile')
        }
    }
}

// === PostgreSQL Pipeline ===
pipelineJob('COE/k8s/deployment/database/postgres') {
    displayName('K8s Deployment - PostgreSQL')
    description('Deploy PostgreSQL to Kubernetes via Jenkinsfile')

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url(repoUrl)
                        credentials(credsId)
                    }
                    branch('main')
                }
            }
            scriptPath('jenkins/postgres-jenkinsfile')
        }
    }
}
