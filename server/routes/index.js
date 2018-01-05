const zoneModules = require('../modules/zone')
const userModule = require('../modules/user')
const bodyParser = require('body-parser')
const {isAuthenticated} = require('./middleware')

module.exports = app => {

    app.use(bodyParser.json({limit: '10mb'}))

    require('./auth')(app)
    require('./profile')(app)
    require('./proposal')(app)
    require('./webhook')(app)

    app.get('/', function (req, res) {
        return res.send({status: 'up'})
    })

    app.get('/api/zones', function (req, res) {
        return res.send(zoneModules.zones)
    })

}