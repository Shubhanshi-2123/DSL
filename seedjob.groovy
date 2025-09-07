job('seedjob') {

    // SCM config
    scm {
        git {
            remote {
                url('https://github.com/ot-central-team/ci-jenkins-wrapper-code.git')
                credentials('ayush090909')   // Jenkins credential ID
            }
            branch('*/main')
        }
    }

    steps {
        dsl {
            external('seedjob/example_seedjob.dsl')  // Path inside repo
            removeAction('IGNORE')                   // What to do with removed jobs
            removeViewAction('IGNORE')               // What to do with removed views
            ignoreExisting(false)                    // Don’t ignore updates
            lookupStrategy('JENKINS_ROOT')           // Look from Jenkins root
            sandbox(true)                            // Run in Groovy Sandbox
            ignoreMissingFiles(true)                 // Skip if file missing
        }
    }
}
