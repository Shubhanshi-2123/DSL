// === Folder Structure ===
folder('COE') {}
folder('COE/command-center') {}
folder('COE/command-center/AWS') {}

def credsId = 'jenkins' // ITMS-Shared-Library credential ID

// === AWS-VM-START Job ===
pipelineJob('COE/command-center/AWS/AWS-VM-START') {
    displayName('AWS VM START')
    description('Pipeline to start AWS VMs using helpdesk Jenkinsfile')

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('git@github.com:SheetalNain/Frappe-Helpdesk.git')
                        credentials(credsId)
                    }
                    branch('*/main')
                }
            }
            scriptPath('helpdesk/Jenkinsfile-start')
        }
    }
}

// === AWS-VM-STOP Job ===
pipelineJob('COE/command-center/AWS/AWS-VM-STOP') {
    displayName('AWS VM STOP')
    description('Pipeline to stop AWS VMs using LDCManager Jenkinsfile')

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('git@github.com:OT-COE/LDCManager.git')
                        credentials(credsId)
                    }
                    branch('*/itms-beta-1.1')
                }
            }
            scriptPath('Jenkinsfile-stop')
        }
    }
}
