folder('COE') {
    displayName('COE')
}
folder('COE/VM') {
    displayName('VM')
}

folder('COE/VM/UserManagement') {
    displayName('UserManagement')
}
pipelineJob('COE/VM/UserManagement/SSH_User_Provisioning') {
    displayName('SSH_User_Provisioning')
    description('Job to provision SSH users using Ansible')

    parameters {
        stringParam('TARGET_HOST_INVENTORY_CONTENT', 
            'localhost ansible_user=jenkins ansible_connection=ssh ansible_ssh_common_args="-o StrictHostKeyChecking=no"', 
            'Inventory content.\n- For localhost to work: Add your ~/.ssh/id_rsa.pub to ~/.ssh/authorized_keys.')

        stringParam('SSH_CREDENTIAL_ID', 'ssh-key-OT-COE-org', 'SSH credential ID to use')

        textParam('USER_LIST', '''user1,dev,present
user2,devops,present''', 
            'List of users (format: username,role,status, one per line)')

        textParam('SSH_KEYS_JSON', '{"user1": "ssh-AAedeuddkqnd", "user2": "ssh-Bwdhwqjdwqkjdqk"}',
            'JSON object with username: SSH key pairs (e.g., {"username": "ssh-rsa ..."})')
    }

    definition {
        cps {
            // Instead of writing the full script here, read from repo file
            script(readFileFromWorkspace('pipelines/ssh_user_provisioning.groovy'))
            sandbox(true)
        }
    }
}
