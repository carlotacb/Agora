const express = require('express')
const app = express()
const morgan = require('morgan')
const db = require('./modules/db')
const mung = require('express-mung');

const config = require('./config')

const BootstrapRouter = require('./routes')


app.use(mung.headers((req, res) => {
    if (res.statusCode === 200) {
        if (req.method === 'POST' && req.path.includes('/api/proposal')) {
            const headerKey = 'X-New-Achievements'
            if (req.body.comment === 'achi') {
                res.setHeader(headerKey, 'COM5')
            } else if (req.body.comment === 'achis') {
                res.setHeader(headerKey, 'COM5,OW,NED')
            } else if (req.body.title === 'achi') {
                res.setHeader(headerKey, 'PROP10')
            } else if (req.body.title === 'achis') {
                res.setHeader(headerKey, 'PROP30,LOL')
            }
        }
    }
}))

BootstrapRouter(app)
BootstrapServer(app)
StartServer(app)


function BootstrapServer(app) {
    if (config.enableMorgan) {
        app.use(morgan('combined'))
    }

    app.use(function (err, req, res, next) {
        console.error(`Error on ${req.method} ${req.path} with request body ${JSON.stringify(req.body)}`, err)
        res.status(err.status || 500).json({errorCode: err.errorCode, message: err.message})
    });
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
