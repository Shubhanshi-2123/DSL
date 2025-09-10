// === Folder Structure ===
folder('COE') {}
folder('COE/CI') {}
folder('COE/CI/AI-Tools') {}

// === Postgres-MCP Pipeline ===
pipelineJob('COE/CI/AI-Tools/Postgres-MCP') {
    displayName('Postgres-MCP')

    definition {
        cps {
            script(readFileFromWorkspace('pipelines/postgres_mcp.groovy'))
            sandbox(true) // enable Groovy sandbox
        }
    }
}
