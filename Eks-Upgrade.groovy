// === Folder Structure ===
folder('COE') {}
folder('COE/k8s') {}
folder('COE/k8s/Eks-Upgrade') {}

pipelineJob('COE/k8s/Eks-Upgrade/EKS-UPGRADE') {
    displayName('EKS Cluster Upgrade Pipeline')
    description('Pipeline for upgrading EKS cluster using shared-library Jenkinsfile')

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('git@github.com:OT-COE/shared-library.git')
                        credentials('jenkins (ssh-key-OT-COE-org)') 
                    }
                    branch('*/ot-261-develop-custom-ami-v3')
                }
            }
            scriptPath('Jenkinsfile')
        }
    }
}
