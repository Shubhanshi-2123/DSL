
folder('COE') {
    displayName('COE')
    description('All COE related jobs')
}

folder('COE/k8s') {
    displayName('K8s')
    description('Kubernetes related jobs')
}

folder('COE/k8s/Eks-Upgrade') {
    displayName('EKS Upgrade')
    description('Jobs related to EKS cluster upgrade')
}

pipelineJob('COE/k8s/Eks-Upgrade/EKS-UPGRADE') {
    description('EKS Upgrade job using shared library Jenkinsfile')
    logRotator {
        daysToKeep(30)
        numToKeep(50)
    }
    
   disableConcurrentBuilds()
    
    parameters {
        base64File(name: 'cluster_properties_yamlfile', description: 'Upload your cluster YAML file')
    }
    
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('git@github.com:OT-COE/shared-library.git')
                        credentials('jenkins') // ssh-key-OT-COE-org
                    }
                    branch('*/ot-261-develop-custom-ami-v3')
                    extensions {
                        // Optional: lightweight checkout
                        cloneOptions {
                            shallow(true)
                            depth(1)
                        }
                    }
                }
            }
            scriptPath('Jenkinsfile')
            lightweight(true)
        }
    }

    properties {
        pipelineTriggers([]) // Add triggers if needed later
        disableConcurrentBuilds()
    }
}
