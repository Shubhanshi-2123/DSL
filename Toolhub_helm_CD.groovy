folder('COE') {
    displayName('COE')

}

folder('COE/Toolhub-helm-CD') {
    displayName('Toolhub-helm-CD')
}

pipelineJob('COE/Toolhub-helm-CD') {

 
    definition {
        cps {
        }
    }
}
