// Step 1: Ensure the folder exists
folder('AuditTool') {
    displayName('AuditTool')
}

// Step 2: Create the pipeline job inside the folder
pipelineJob('AuditTool/scoutscuite-gcp') {
    description('GCP ScoutSuite scanning job')

    // Parameters
    parameters {
        // Multi-line string parameter for GCP service account JSON key
        textParam('SA_KEY_CONTENT', '', 'Paste the content of your GCP service account JSON key here')
    }

    // Pipeline definition: read Groovy script from workspace
    definition {
        cps {
            script(readFileFromWorkspace('pipelines/scoutscuite-gcp.groovy'))
            sandbox()
        }
    }

    // Optional configuration: prevent concurrent builds
    configure { project ->
        project / 'properties' / 'hudson.model.concurrentBuild.ConcurrentBuildProperty' << {}
    }
}
