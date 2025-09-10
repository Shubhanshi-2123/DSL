pipeline {
    agent any

    environment {
        REGISTRY = 'registry.ldc.opstree.dev'
        REPO = "${REGISTRY}/ai-coe/postgres-mcp"
        IMAGE_TAG = 'V1.0'
    }

    stages {
        // stage('Checkout') {
        //     steps {
        //         checkout scm
        //     }
        // }

        stage('Docker Registry Login') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'harbor-creds-jenkins',
                    usernameVariable: 'REGISTRY_USER',
                    passwordVariable: 'REGISTRY_PASS'
                )]) {
                    sh '''
                        echo "$REGISTRY_PASS" | docker login $REGISTRY -u "$REGISTRY_USER" --password-stdin
                    '''
                }
            }
        }

        stage('Pull, Tag & Push Image') {
            steps {
                script {
                    sh '''
                        docker pull crystaldba/postgres-mcp
                        docker tag crystaldba/postgres-mcp $REPO:$IMAGE_TAG
                        docker push $REPO:$IMAGE_TAG
                        echo "Cleaning Up the images from local server."
                        docker rmi $REPO:$IMAGE_TAG
                        docker rmi crystaldba/postgres-mcp || true
                    '''
                }
            }
        }
    }

    post {
        success {
            echo "✅ Postgres-MCP image pushed to $REPO:$IMAGE_TAG successfully"
        }
        failure {
            echo "❌ Pipeline failed!"
        }
    }
}
