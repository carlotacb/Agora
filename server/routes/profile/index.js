const userModule = require('../../modules/user')
const {isAuthenticated} = require('../middleware')

module.exports = app => {

    app.get('/api/profile', isAuthenticated, async function (req, res) {
        try {
            const user = await userModule.getProfile({username: req.username})
            res.json(user)
        } catch (error) {
            console.error('error on getting profile', error)
            res.sendStatus(500)
        }
    })

    app.post('/api/profile', isAuthenticated, async function (req, res) {
        try {
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
        } catch (error) {
            console.error('error on updating profile', error)
            res.sendStatus(403)
        }
    })

    app.put('/api/profile', isAuthenticated, async function (req, res) {
        try {
            const {oldpassword, password, confirmpassword} = req.body
            const user = await userModule.updatePassword({
                username: req.username,
                oldpassword,
                password,
                confirmpassword,
            })
            res.json(user)
        } catch (error) {
            console.error('error on updating profile', error)
            res.sendStatus(403)
        }
    })

    app.get('/api/profile/comunity', isAuthenticated, async function (req, res) {
        try {
            const user = await userModule.getProfile({username: req.username})
            const users = await userModule.getByZone({user})
            res.json(users)
        } catch (error) {
            console.error('error on getting profile', error)
            res.sendStatus(500)
        }
    })
}
