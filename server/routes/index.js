module.exports = app => {
    app.get('/', function (req, res) {
        return res.send({status: 'up'})
    })
}
