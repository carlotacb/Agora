const userModule = require('../../modules/user')
const {isAuthenticated} = require('../middleware')
const f = require('../util').wrapAsyncRouterFunction

module.exports = app => {

    app.get('/api/profile', isAuthenticated, f(async function (req, res) {
        const user = await userModule.getProfile({username: req.username})
        res.json(user)
    }))

    app.post('/api/profile', isAuthenticated, f(async function (req, res) {
        const {cpCode, realname, description, neighborhood, bdate, sex, image} = req.body
        const user = await userModule.updateProfile({
            username: req.username,
            description,
            cpCode,
            realname,
            neighborhood,
            bdate,
            sex,
            image,
        })
        res.json(user)
    }))

    app.put('/api/profile', isAuthenticated, f(async function (req, res) {
        const {oldpassword, password, confirmpassword} = req.body
        const user = await userModule.updatePassword({
            username: req.username,
            oldpassword,
            password,
            confirmpassword,
        })
        res.json(user)
    }))

    app.get('/api/profile/comunity', isAuthenticated, f(async function (req, res) {
        const user = await userModule.getProfile({username: req.username})
        const users = await userModule.getByZone({user})
        res.json(users)
    }))
}
