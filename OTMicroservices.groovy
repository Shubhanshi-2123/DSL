// === Folder Structure ===
folder('COE') {}
folder('COE/CI') {}
folder('COE/CI/OT-Microservices') {}

def githubCredId = 'github-jenkins-creds'

// === Attendance API Pipeline ===
pipelineJob('COE/CI/OT-Microservices/Attendence-Api-Pipeline') {
    displayName('Attendance-API-Pipeline')
    description('CI pipeline for the Attendance API microservice')

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('https://github.com/ayush090909/attendance-api.git')
                        credentials(githubCredId)
                    }
                    branch('main')
                }
            }
            scriptPath('Jenkinsfile')
        }
    }
}

// === Employee API Pipeline ===
pipelineJob('COE/CI/OT-Microservices/Employee-Api-Pipeline') {
    displayName('Employee-API-Pipeline')
    description('CI pipeline for the Employee API microservice')

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('https://github.com/OT-MICROSERVICES/employee-api.git')
                        credentials(githubCredId)
                    }
                    branch('main')
                }
            }
            scriptPath('Jenkinsfile')
        }
    }
}

// === Salary API Pipeline ===
pipelineJob('COE/CI/OT-Microservices/Salary-Api-Pipeline') {
    displayName('Salary-API-Pipeline')
    description('CI pipeline for the Salary API microservice')

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('https://github.com/OT-MICROSERVICES/salary-api.git')
                        credentials(githubCredId)
                    }
                    branch('main')
                }
            }
            scriptPath('Jenkinsfile')
        }
    }
}
