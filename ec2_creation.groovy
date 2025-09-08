
folder('backstage') {
    displayName('backstage')
    description('Folder for Backstage-related pipelines')
}

// Create empty pipeline job inside the folder
pipelineJob('backstage/ec2-creation') {    
    definition {
        cps {
           
            sandbox()
        }
    }
}
