
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

    // Disable concurrent builds correctly for pipelineJob
    configure { project ->
        project / 'properties' / 'hudson.model.concurrent.ConcurrencyThrottleJobProperty' {
            maxConcurrentPerNode(1)
            maxConcurrentTotal(1)
        }
    }

    parameters {
        base64File(name: 'cluster_properties_yamlfile', description: 'Upload your cluster YAML file')
    }

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('git@github.com:OT-COE/shared-library.git')
                        credentials('jenkins')
                    }
                    branch('*/ot-261-develop-custom-ami-v3')
                }
            }
            scriptPath('Jenkinsfile')
            lightweight(true)
        }
    }
}
