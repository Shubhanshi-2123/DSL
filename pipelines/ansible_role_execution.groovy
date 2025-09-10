pipeline {
    agent any
        stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/OT-OSM/teleport.git', branch: 'teleport'
            }
        }

        stage('Run Ansible Role') {
            steps {
                script {
                    sh '''
                        ansible-playbook -i inventory/Teleport-agent.ini playbook.yaml
                      '''
                }
            }
        }
    }

    post {
        success {
            echo "Ansible role executed successfully."
        }
        failure {
            echo "Ansible role execution failed."
        }
    }
}
