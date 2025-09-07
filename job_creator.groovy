freeStyleJob('job_creator') {
    description('Seed job to create Jenkins jobs using Job DSL')

    scm {
        git {
            remote {
                url('https://github.com/akashrajvanshi-opst/jenkins-shared-libraries.git')
                credentials('ayush090909/******')
            }
            branch('*/main')
        }
    }

    steps {
        dsl {
            external('seedjobs/jobs_dsl.groovy')
            removeAction('IGNORE')  // Ignore removed jobs/views
            ignoreMissingFiles(false)
            sandbox(true)
        }
    }
}
