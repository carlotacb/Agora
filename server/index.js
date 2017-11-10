const express = require('express')
const app = express()
const bodyParser = require('body-parser')
const morgan = require('morgan')
const db = require('./modules/db')

const config = require('./config')

const BootstrapRouter = require('./routes')

BootstrapServer(app)
BootstrapRouter(app)
StartServer(app)


function BootstrapServer(app) {
    app.use(bodyParser.json())
    if (config.enableMorgan) {
        app.use(morgan('combined'))
    }
}

function StartServer(app) {
    app.listen(config.port, function () {
        console.log(`Agora app listening on port ${config.port}!`)
    })
    connectDBWithRetry()
}

function connectDBWithRetry() {
    db.connect().catch(error => {
        console.error('Error connecting DB. Retrying in 3 seconds...')
        console.error(error)
        setTimeout(connectDBWithRetry, 3000)
    })
}
