// Create folders
folder('COE') {
    displayName('COE')
    description('All COE related jobs')
}

folder('COE/Observability') {
    displayName('Observability')
    description('Observability related jobs')
}

folder('COE/Observability/O11y_Setup') {
    displayName('O11y Setup')
    description('Jobs related to Observability stack')
}

// List of jobs to create under O11y_Setup
def jobs = [
    [name: 'tempo', script: 'jenkins/tempo-jenkinsfile'],
    [name: 'loki', script: 'jenkins/loki-jenkinsfile'],
    [name: 'VictoriaMetrics', script: 'jenkins/vm-jenkinsfile'],
    [name: 'Grafana', script: 'jenkins/grafana-jenkinsfile'],
    [name: 'otel-operator', script: 'jenkins/otel_operator-jenkinsfile'],
    [name: 'Grafana_dashboard', script: 'jenkins/dashboard-jenkinsfile'],
    [name: 'Grafana_datasource', script: 'jenkins/datasource-jenkinsfile'],
    [name: 'alertmanager', script: 'jenkins/alertmanager-jenkinsfile'],
    [name: 'alerting_rules', script: 'jenkins/alertingrules-jenkinsfile'],
    [name: 'otel-collector', script: 'jenkins/otel_collector-jenkinsfile'],
    [name: 'Blackbox', script: 'jenkins/blackbox-jenkinsfile'],
    [name: 'Endpoint', script: 'jenkins/endpoint-jenkinsfile']
]

// Create pipeline jobs
jobs.each { job ->
    pipelineJob("COE/Observability/O11y_Setup/${job.name}") {
        definition {
            cpsScm {
                scm {
                    git {
                        remote {
                            url('https://github.com/ot-client/o11y-k8s-setup-template.git')
                            credentials('O11y_http_ldc') // your Jenkins credential ID
                        }
                        branch('main')
                    }
                    scriptPath(job.script) // Path to Jenkinsfile inside repo
                }
            }
        }
    }
}
