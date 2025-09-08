// folder('AuditTool') {
//     description('Folder containing all Scout Suite and audit jobs')
// }

// pipelineJob('AuditTool/ScoutScan') {
//     description('AWS ScoutSuite security scanning job (with backup & markdown report)')
//     definition {
//         cps {
//             script(readFileFromWorkspace('pipelines/scoutscan.groovy'))
//             sandbox()
//         }
//     }
// }
folder('AuditTool') {
    description('Folder containing all Scout Suite and audit jobs')
}

pipelineJob('AuditTool/ScoutScan') {
    description('AWS ScoutSuite security scanning job')
    definition {
        cps {
            script(readFileFromWorkspace('pipelines/scoutscan.groovy'))
            sandbox()
        }
    }
}

pipelineJob('AuditTool/scoutscuite-gcp') {
    description('GCP ScoutSuite security scanning job')
    definition {
        cps {
            script(readFileFromWorkspace('pipelines/scoutscuite-gcp.groovy'))
            sandbox()
        }
    }
}
pipelineJob('AuditTool/ss') {
    definition {
        cps {
            script(readFileFromWorkspace('pipelines/ss.groovy'))
            sandbox()
        }
    }
}

pipelineJob('AuditTool/ss2') {
    definition {
        cps {
            script(readFileFromWorkspace('pipelines/ss2.groovy'))
            sandbox()
        }
    }
}
