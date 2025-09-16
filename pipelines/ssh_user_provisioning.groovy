// pipeline {
//     agent any

//     parameters {
//         string(
//             name: 'TARGET_HOST_INVENTORY_CONTENT', 
//             defaultValue: 'localhost ansible_user=jenkins ansible_connection=ssh ansible_ssh_common_args="-o StrictHostKeyChecking=no"', 
//             description: '''Inventory content.
//         - For localhost to work: Add your ~/.ssh/id_rsa.pub to ~/.ssh/authorized_keys.'''
//         )
//         string(
//             name: 'SSH_CREDENTIAL_ID',
//             defaultValue: 'ssh-key-OT-COE-org',
//             description: 'SSH credential ID to use'
//         )
//         text(name: 'USER_LIST', defaultValue: 'user1,dev,present\nuser2,devops,present', description: 'List of users (format: username,role,status, one per line)')
//         text(name: 'SSH_KEYS_JSON', defaultValue: '{"user1": "ssh-AAedeuddkqnd", "user2": "ssh-Bwdhwqjdwqkjdqk"}', description: 'JSON object with username: SSH key pairs (e.g., {"username": "ssh-rsa ..."})')
//     }

//     environment {
//         LANG = 'en_US.UTF-8'
//         LC_ALL = 'en_US.UTF-8'
//     }

//     stages {
//         stage('Clean Workspace') {
//             steps {
//                 cleanWs()
//             }
//         }

//         stage('Clone Ansible Role') {
//             steps {
//                 sh '''
//                     mkdir -p roles
//                     git clone https://github.com/OT-OSM/usermanagement.git roles/osm_usermanagement
//                 '''
//             }
//         }

//         stage('Prepare Inventory and Playbook') {
//             steps {
//                 script {

//                     // Write Inventory file
//                     writeFile file: 'inventory.ini', text: params.TARGET_HOST_INVENTORY_CONTENT

//                     // Write userlist
//                     writeFile file: 'roles/osm_usermanagement/userlist', text: params.USER_LIST

//                     // Parse SSH_KEYS_JSON
//                     def sshKeys = readJSON text: params.SSH_KEYS_JSON
//                     sshKeys.each { user, key ->
//                         if (key?.trim()) {
//                             writeFile file: "roles/osm_usermanagement/pub_keys/${user}", text: key
//                         }
//                     }

//                     // Generate playbook.yaml
//                     writeFile file: 'playbook.yaml', text: '''
//                     - hosts: all
//                       become: yes
//                       vars:
//                         user_mapping_file_path: "{{ playbook_dir }}/roles/osm_usermanagement/userlist"
//                         pub_keys_dir_path: "{{ playbook_dir }}/roles/osm_usermanagement/pub_keys"
//                       roles:
//                         - osm_usermanagement
//                     '''
//                 }
//             }
//         }

//         stage('Run Playbook') {
//             steps {
//                 sshagent([params.SSH_CREDENTIAL_ID]) {
//                     sh '''
//                         docker run --rm \
//                             -v $PWD:/ansible \
//                             -v $SSH_AUTH_SOCK:/ssh-agent \
//                             -e SSH_AUTH_SOCK=/ssh-agent \
//                             -e LANG=$LANG \
//                             -e LC_ALL=$LC_ALL \
//                             --network host \
//                             --workdir /ansible \
//                             alpine/ansible \
//                             ansible-playbook -i inventory.ini playbook.yaml
//                     '''
//                 }
//             }
//         }
//     } // stages
// }
