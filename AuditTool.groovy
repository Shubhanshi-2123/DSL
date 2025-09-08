// Step 1: Ensure the folder exists
folder('AuditTool') {
    displayName('AuditTool')
}

// Step 2: Create the ScoutScan pipeline job
pipelineJob('AuditTool/ScoutScan') {
    description('AWS ScoutSuite security scanning job')

    // Simple string parameters
    parameters {
        stringParam('ROLE_ARN', 'arn:aws:iam::370389955750:role/scout-suite-security-role', 'Enter the Role ARN to assume')
        stringParam('ACCOUNT_NAME', 'my-aws-account', 'Name of the AWS account being scanned')
        stringParam('EMAIL_RECIPIENT', 'your-email@example.com', 'Email address to send the report to')
    }

    // Active Choices parameter using proper $class XML
    configure { project ->
        project / 'properties' / 'hudson.model.ParametersDefinitionProperty' / 'parameterDefinitions' << 
            'org.biouno.unochoice.CascadeChoiceParameter'(
                plugin: 'uno-choice@2.5'
            ) {
                name('AWS_REGION')
                description('Select AWS Region')
                choiceType('SINGLE_SELECT')
                filterLength(1)
                referencedParameters('')
                script(class: 'org.biouno.unochoice.GroovyScript') {
                    fallbackScript(class: 'org.biouno.unochoice.GroovyScript') {
                        script('return ["us-east-1"]')
                        sandbox(true)
                    }
                    script('''
                        import groovy.json.JsonSlurper
                        def url = 'https://ip-ranges.amazonaws.com/ip-ranges.json'
                        def conn = new URL(url).openConnection()
                        conn.setRequestMethod("GET")
                        conn.connect()
                        if (conn.responseCode != 200) {
                            return ["Error fetching regions"]
                        }
                        def data = new JsonSlurper().parse(conn.inputStream)
                        return data.prefixes.collect { it.region }.findAll { it }.unique().sort()
                    ''')
                    sandbox(true)
                }
            }

        // Prevent concurrent builds
        project / 'properties' / 'hudson.model.concurrentBuild.ConcurrentBuildProperty' << {}
    }

    // Pipeline definition: read from workspace
    definition {
        cps {
            script(readFileFromWorkspace('pipelines/scoutscan.groovy'))
            sandbox()
        }
    }
}
