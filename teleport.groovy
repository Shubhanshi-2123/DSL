
folder('Teleport') {}


pipelineJob('Teleport/ansible-role-execution') {
    displayName('ansible-role-execution')
    description('Pipeline for executing Ansible roles')

    // Log rotation
    logRotator {
        daysToKeep(3)      
        numToKeep(null)    
    }

    // Pipeline script (inline)
    definition {
        cps {
            script(readFileFromWorkspace('pipelines/ansible_role_execution.groovy')) // adjust path as needed
            sandbox(true) // Groovy sandbox enabled
        }
    }

}
