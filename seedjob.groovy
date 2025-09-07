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

    triggers {
        // Uncomment if you want auto build on webhook or schedule
        // scm('H/5 * * * *')   // Poll SCM every 5 minutes
        // githubPush()         // GitHub webhook trigger
    }

    steps {
        dsl {
            external('seedjob/example_seedjob.dsl')  // Path inside repo
            removeAction('IGNORE')                   // Do nothing on removed jobs
            removeViewAction('IGNORE')               // Do nothing on removed views
            removeConfigFilesAction('IGNORE')        // Do nothing on removed configs
            ignoreExisting(false)                    // Don’t ignore updates
            lookupStrategy('JENKINS_ROOT')           // Look from Jenkins root
            additionalClasspath('')                  // Optional
            sandbox(true)                            // Run in Groovy Sandbox
            ignoreMissingFiles(true)                 // Skip if file missing
        }
    }
}
