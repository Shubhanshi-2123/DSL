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

    // Disable concurrent builds via configure block
    configure { project ->
        project / 'properties' / 'hudson.model.concurrent.ConcurrencyThrottleJobProperty' {
            maxConcurrentPerNode(1)
            maxConcurrentTotal(1)
        }

        // Add Base64 File Parameter
        project / 'properties' / 'hudson.model.ParametersDefinitionProperty' / 'parameterDefinitions' << 
            'org.jenkinsci.plugins.base64parameter.Base64ParameterDefinition' {
                name('cluster_properties_yamlfile')
                description('Upload your cluster YAML file')
                defaultValue('')
            }
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
