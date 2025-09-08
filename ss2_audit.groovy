folder('AuditTool') {
    displayName('AuditTool')
    description('Folder for all AuditTool jobs')
}

pipelineJob('AuditTool/ss2') {
    description('ScoutSuite Scan job for AWS account (ss2)')

    // Top-level properties block ensures parameters appear in UI
    configure { project ->

        // String parameters
        project / 'properties' / 'hudson.model.ParametersDefinitionProperty' / 'parameterDefinitions' << 
            stringParam('ROLE_ARN', 'arn:aws:iam::370389955750:role/scout-suite-security-role', 'Enter the Role ARN to assume')
        project / 'properties' / 'hudson.model.ParametersDefinitionProperty' / 'parameterDefinitions' << 
            stringParam('ACCOUNT_NAME', 'my-aws-account', 'Name of the AWS account being scanned')
        project / 'properties' / 'hudson.model.ParametersDefinitionProperty' / 'parameterDefinitions' << 
            stringParam('EMAIL_RECIPIENT', 'your-email@example.com', 'Email address to send the report to')

        // Active Choices parameter
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
                        sandbox(true)
                        script('return ["us-east-1"]')
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

        // Optional: prevent concurrent builds
        project / 'properties' / 'hudson.model.concurrentBuild.ConcurrentBuildProperty' << {}
    }

    // Pipeline definition: call script from workspace
    definition {
        cps {
            script(readFileFromWorkspace('pipelines/ss2.groovy'))
            sandbox()
        }
    }
}
