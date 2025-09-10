// === Folder Structure ===
folder('COE') {}
folder('COE/Cluster-User-creation') {}

pipelineJob('COE/Cluster-User-creation/user-creation-2') {
    displayName('user-creation-2')
    description('Pipeline job for creating users in the cluster via K8s Jenkinsfile')

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('git@github.com:SheetalNain/Frappe-Helpdesk.git')
                        credentials('jenkins (ITMS-Shared-Library)') 
                    }
                    branch('*/main')
                }
            }
            scriptPath('helpdesk/Jenkinsfile-K8s')
        }
    }
}
