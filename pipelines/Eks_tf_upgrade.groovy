@Library('EksUpgrade@ot-261-develop-tf-v3') _

node {
    def clusterInfo
    def preCheckSummary = ''

    properties([
        parameters([
            base64File(name: 'cluster_properties_yamlfile', description: 'Upload your cluster YAML file')
        ])
    ])

    withCredentials([
        string(credentialsId: 'aws_access_key_id_eks_demo', variable: 'AWS_ACCESS_KEY_ID'),
        string(credentialsId: 'aws_secret_access_key_eks_demo', variable: 'AWS_SECRET_ACCESS_KEY')
    ]) {

        stage('Pre-checks') {
            withFileParameter('cluster_properties_yamlfile') {
                clusterInfo = readClusterYaml(env.cluster_properties_yamlfile)

                slackNotifier.notifyStage('Pre-checks', clusterInfo.slack_channel) {
                    def summary = ""

                    withCredentials([file(credentialsId: clusterInfo.kubeconfig_secret, variable: 'K8S_FILE')]) {
                        checkVersion("${clusterInfo.current_cluster_version}", "${clusterInfo.target_version}")
                        summary += ":bookmark_tabs: Upgrade Version check passed: ${clusterInfo.current_cluster_version} -> ${clusterInfo.target_version}\n"

                        summary += ":cd: Eks Cluster Backup status: ${clusterInfo.cluster_backup_taken}\n"

                        def result = validateKubeConfig(K8S_FILE)
                        summary += "${result.summary}\n"

                        def apicheck = k8sApiVersionCheck(K8S_FILE, clusterInfo.target_version)
                        summary += "${apicheck.summary}\n"
                    }
                    preCheckSummary = summary
                    return summary
                }
            }
        }

        stage('EKS upgrade') {
            slackNotifier.notifyStage('EKS upgrade', clusterInfo.slack_channel) {
                echo "${clusterInfo}"
                def result = cloneRepo(
                    repoUrl: clusterInfo.tf_module_repo,
                    branch: clusterInfo.tf_module_branch,
                    credentialsId: clusterInfo.tf_module_repo_cred,
                    targetDir: 'eks-tf',
                    isPublic: clusterInfo.tf_module_repo_ispublic
                )
                if (!result.success) {
                    error("Stopping build: ${result.message}")
                }
               // controlPlaneSummary = result.summary
                def tfDir = "${WORKSPACE}/eks-tf"
                def region = clusterInfo.region

                // Call the terraform functions from the shared library
                def initResult = tf.terraformInit(tfDir,region)
                echo initResult

                def validateResult = tf.terraformValidate(tfDir,region)
                echo validateResult

                def planResult = tf.terraformPlan(tfDir,region)
                echo planResult

                def applyResult = tf.terraformApply(tfDir,region)
                echo applyResult
                return "📘 Repo cloned: ${result.repo} \n 🌿 branch: ${result.branch} \n ${initResult} \n ${validateResult} \n ${planResult} \n ${applyResult}"
            }
        }
    }
}
