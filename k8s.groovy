folder('COE') {
    displayName('COE')
}

folder('COE/command-center') {
    displayName('Command Center')
}

folder('COE/command-center/K8S') {
    displayName('K8S')
}

pipelineJob('COE/command-center/K8S/user-creation-2') {

    logRotator {
        daysToKeep(3)
        numToKeep(5)
    }

    // Disable concurrent builds
    concurrentBuild(false)

    parameters {
        stringParam('USERNAME', 'pritam', 'Kubernetes username to create')
        stringParam('NAMESPACE', 'test', 'Namespace for the user')
        stringParam('TICKET_ID', '', 'Frappe Helpdesk Ticket ID')
    }

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('git@github.com:SheetalNain/Frappe-Helpdesk.git')
                        credentials('jenkins')
                    }
                    branches('*/main')
                    extensions {}
                }
            }
            scriptPath('helpdesk/Jenkinsfile-K8s')
            lightweight(true)
        }
    }
}
