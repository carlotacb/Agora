const zoneModules = require('../modules/zone')
const bodyParser = require('body-parser')

module.exports = app => {

    app.use(bodyParser.json())

    require('./auth')(app)
    require('./profile')(app)
    require('./proposal')(app)

    app.get('/', function (req, res) {
        return res.send({status: 'up'})
    })

    app.get('/api/zones', async function (req, res) {
        return res.send(zoneModules.zones)
    })

}