const express = require('express')
const app = express()
const bodyParser = require('body-parser')

const config = require('./config')

const BootstrapRouter = require('./routes')

BootstrapServer(app)
BootstrapRouter(app)
StartServer(app)


function BootstrapServer(app) {
    app.use(bodyParser.json())
}

function StartServer(app) {
    app.listen(config.port, function () {
        console.log(`Agora app listening on port ${config.port}!`)
    })
}