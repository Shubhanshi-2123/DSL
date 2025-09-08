folder('AuditTool') {
    displayName('Audit Tool')
    description('Folder containing all Scout Suite and audit jobs')
}

pipelineJob('AuditTool/ScoutScan') {
    description('AWS ScoutSuite security scanning job (with backup & markdown report)')
    definition {
        cps {
            script(readFileFromWorkspace('DSL/pipelines/scoutscan.groovy'))
            sandbox()
        }
    }
}
