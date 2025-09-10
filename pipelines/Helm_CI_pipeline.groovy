pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/OT-CONTAINER-KIT/helm-charts.git'
            }
        }

        stage('Run Gitleaks') {
    steps {
        script {
            sh '''
                # Check if gitleaks is installed
                if ! command -v gitleaks &> /dev/null
                then
                    echo "Gitleaks not found. Installing..."
                    wget https://github.com/gitleaks/gitleaks/releases/download/v8.18.2/gitleaks_8.18.2_linux_x64.tar.gz
                    tar xvzf gitleaks_8.18.2_linux_x64.tar.gz
                    chmod +x gitleaks
                    mkdir -p $HOME/.local/bin
                    mv gitleaks $HOME/.local/bin/
                    export PATH=$HOME/.local/bin:$PATH
                else
                    echo "Gitleaks already installed."
                fi

                # Run gitleaks scan
                export PATH=$HOME/.local/bin:$PATH
                gitleaks detect --source=. --report-format=json --report-path=gitleaks-report.json || true
            '''
        }
    }
}


stage('Helm Lint') {
    steps {
        script {
            sh '''
                mkdir -p $WORKSPACE/.helmcache/repository

                export HELM_REPOSITORY_CACHE=$WORKSPACE/.helmcache/repository
                export HELM_REPOSITORY_CONFIG=$WORKSPACE/.helmcache/repositories.yaml

                # Add required repos (do this every time so they're guaranteed present)
                helm repo add ot-container-kit https://ot-container-kit.github.io/helm-charts || true
                helm repo add podinfo https://stefanprodan.github.io/podinfo || true

                helm repo update
                helm dependency update charts/web
                helm lint charts/web --debug
            '''
        }
    }
}



        stage('Template Validation') {
            steps {
                script {
                    def templateOutput = sh(script: '''
                        helm template charts/web
                    ''', returnStdout: true).trim()

                    echo "Helm Template Output:\n${templateOutput}"

                    writeFile file: 'helm-template-output.yaml', text: templateOutput
                }
            }
        }

        stage('Helm Dry Run') {
            steps {
                script {
                    sh '''
                        helm install web charts/web --dry-run --debug
                    '''
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'gitleaks-report.json', onlyIfSuccessful: false
            archiveArtifacts artifacts: 'helm-lint-report.txt', onlyIfSuccessful: false
            archiveArtifacts artifacts: 'helm-template-output.yaml', onlyIfSuccessful: false
        }

        success {
            echo "✅ Helm Chart CI Pipeline Completed Successfully"
        }
        failure {
            echo "❌ Helm Chart CI Pipeline Failed"
        }
    }
}
