pipelineJob('AuditTool/ScoutScan') {
    description('AWS ScoutSuite security scanning job (with backup & markdown report)')
    definition {
        cps {
            script(readFileFromWorkspace('pipelines/scoutscan.groovy'))
            sandbox()
        }
    }
}
