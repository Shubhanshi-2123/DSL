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

pipelineJob('COE/k8s/Eks-Upgrade/Eks-TF-Upgrade') {
    description('EKS Terraform Upgrade job')

    // Disable concurrent builds
    configure { project ->
        project / 'properties' / 'hudson.model.concurrent.ConcurrencyThrottleJobProperty' {
            maxConcurrentPerNode(1)
            maxConcurrentTotal(1)
        }

        // Base64 File Parameter
        project / 'properties' / 'hudson.model.ParametersDefinitionProperty' / 'parameterDefinitions' << 
            'org.jenkinsci.plugins.base64parameter.Base64ParameterDefinition' {
                name('cluster_properties_yamlfile')
                description('Upload your cluster YAML file')
                defaultValue('')
            }
    }

    // definition {
    //     cps {
    //         script(readFileFromWorkspace('pipelines/Eks_tf_upgrade.groovy'))
    //         sandbox(true) // same as "Use Groovy Sandbox" in UI
    //     }
    // }
}
