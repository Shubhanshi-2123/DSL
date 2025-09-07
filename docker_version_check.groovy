freeStyleJob('docker-version-check') {
    description('Freestyle job to check Docker version')

    // You can add SCM here if needed later (currently set to none)
    scm {
        // no SCM configured
    }

    steps {
        shell('docker version')
    }
}
