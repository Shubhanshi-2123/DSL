folder('COE') {
    displayName('COE')
}

pipelineJob('COE/Graviton-Compatibility-Scan') {

    logRotator {
        numToKeep(5)
    }

    // Disable concurrent builds in a version-agnostic way
    configure { project ->
        project / 'properties' << 'org.jenkinsci.plugins.workflow.job.properties.DisableConcurrentBuildsJobProperty'()
        
        // Add parameters manually
        def params = project / 'properties' / 'hudson.model.ParametersDefinitionProperty' / 'parameterDefinitions'
        
        params << 'hudson.model.StringParameterDefinition' {
            name('REPO_URL')
            description('Git repository to scan')
            defaultValue('')
        }
        params << 'hudson.model.BooleanParameterDefinition' {
            name('IS_PRIVATE_REPO')
            description('Is the repo private?')
            defaultValue(false)
        }
        params << 'hudson.model.StringParameterDefinition' {
            name('GIT_USERNAME')
            description('Git username (for private repo)')
            defaultValue('')
        }
        params << 'hudson.model.PasswordParameterDefinition' {
            name('GIT_TOKEN')
            description('Git token/password (for private repo)')
            defaultValue('')
        }
    }

    definition {
        cps {
            script(readFileFromWorkspace('pipelines/graviton_scan.groovy'))
            sandbox(true)
        }
    }
}
