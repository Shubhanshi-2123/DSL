folder('COE') {
    displayName('COE')
}

pipelineJob('COE/Graviton-Compatibility-Scan') {


    logRotator {
        numToKeep(5)
    }

    // Disable concurrent builds for pipelineJob
    configure { project ->
        project / 'properties' << 'org.jenkinsci.plugins.workflow.job.properties.DisableConcurrentBuildsJobProperty'()
    }
    concurrentBuild(false)

    parameters {
        stringParam('REPO_URL', '', 'Git repository to scan')
        booleanParam('IS_PRIVATE_REPO', false, 'Is the repo private?')
        stringParam('GIT_USERNAME', '', 'Git username (for private repo)')
        passwordParam('GIT_TOKEN', 'Git token/password (for private repo)')
    }


    definition {
        cps {
            script(readFileFromWorkspace('pipelines/graviton_scan.groovy'))
            sandbox(true)
        }
    }
}
