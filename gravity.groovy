folder('COE') {
    displayName('COE')
}

pipelineJob('COE/Graviton-Compatibility-Scan') {
    description('Pipeline job for scanning repositories for Graviton compatibility')

    // Discard old builds (Max 5 builds)
    logRotator {
        numToKeep(5)
    }

    // Disable concurrent builds
    disableConcurrentBuilds()

    // Parameters
    parameters {
        stringParam('REPO_URL', '', 'Git repository to scan')
        booleanParam('IS_PRIVATE_REPO', false, 'Is the repo private?')
        stringParam('GIT_USERNAME', '', 'Git username (for private repo)')
        passwordParam('GIT_TOKEN', '', 'Git token/password (for private repo)')
    }

    // External pipeline script call
    definition {
        cps {
            script(readFileFromWorkspace('pipelines/graviton_scan.groovy'))
            sandbox(true)
        }
    }
}
