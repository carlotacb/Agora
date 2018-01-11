const userModule = require('../../modules/user')
const {isAuthenticated} = require('../middleware')
const f = require('../util').wrapAsyncRouterFunction
const achievementModule = require('../../modules/achievement')

module.exports = app => {

    app.get('/api/profile', isAuthenticated, f(async function (req, res) {
        const username = req.query.username ? req.query.username : req.username
        const user = await userModule.getProfile({username: username})
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

    app.get('/api/profile/achievements', isAuthenticated, f(async function (req, res) {
        const achievements = await achievementModule.getAndNotifyPersistentAchievements(req.username)
        const missingAchievements = await achievementModule.getMissingAchievementsFromAchievements(achievements)
        achievementModule.setAchievementsAsNotified(req.username)
            .catch(console.error)

        const response = {
            achievements,
            missingAchievements,
        }

        console.log(`Sending response ${JSON.stringify(response)} from GET /api/profile/achievements to ${req.username} `)

        res.json(response)
    }))

    app.get('/api/user/:username', isAuthenticated, f(async function (req, res) {
        const user = await userModule.getProfile({username: req.params.username})

        res.json({
            username: user.username,
            cpCode: user.cpCode,
            realname: user.realname,
            neighborhood: user.neighborhood,
            bdate: new Date(user.bdate),
            sex: user.sex,
            description: user.description,
            image: user.image,
            updatedDateTime: new Date(user.updatedDateTime),
            zone: parseInt(user.zone),
        })
    }))
}
