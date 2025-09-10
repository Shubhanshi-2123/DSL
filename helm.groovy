folder('COE') {
    displayName('COE')
    description('All COE related jobs')
}

folder('COE/Helm-CI') {
    displayName('Helm-CI')
}

pipelineJob('COE/Helm-CI/CI-pipeline') {

    // Directly inline script (from repo file)
    definition {
        cps {
            script(readFileFromWorkspace('pipelines/Helm_CI_pipeline.groovy'))
            sandbox(true)   // UI me jo "Use Groovy Sandbox" option hai
        }
    }
}

