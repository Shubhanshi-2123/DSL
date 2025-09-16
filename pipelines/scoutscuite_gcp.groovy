// pipeline {
//     agent {
//         docker {
//             image 'i4imran/scoutsuite:v2'
//             args '-u root' 
//         }
//     }

//     environment {
//         SCOUT_OUTPUT_DIR = 'scoutsuite-report'
//         SA_KEY_FILE = 'scout-sa-key.json'
//     }

//     parameters {
//         text(name: 'SA_KEY_CONTENT', defaultValue: '', description: 'Paste the content of your GCP service account JSON key here')
//     }

//     stages {
//         stage('Prepare Key File') {
//             steps {
//                 script {
//                     writeFile file: "${env.SA_KEY_FILE}", text: "${params.SA_KEY_CONTENT}"
//                 }
//             }
//         }

//         stage('Scout Suite Scan') {
//             steps {
//                 sh "scout aws -h"
//                 //sh "scout gcp --service-account ${env.SA_KEY_FILE} --report-dir ${env.SCOUT_OUTPUT_DIR}"
//             }
//         }

//         stage('Archive Report') {
//             steps {
//                 sh "ls -la"
//                 archiveArtifacts artifacts: "${SCOUT_OUTPUT_DIR}/**", fingerprint: true
//             }
//         }
//     }

//     post {
//         always {
//             echo "Cleaning up workspace"
//             sh "rm -rf ${SCOUT_OUTPUT_DIR} ${SA_KEY_FILE}"
//         }
//     }
// }
