const userModule = require('../../modules/user')
const sessionModule = require('../../modules/session')
const f = require('../util').wrapAsyncRouterFunction
const errors = require('../../modules/error')

const {isAuthenticated} = require('../middleware')

module.exports = app => {
    app.post('/api/login', f(async function (req, res) {
        const password = req.body.password
        const username = req.body.username.toLowerCase()

        try {
            const user = await userModule.login({username, password})
            const session = await sessionModule.generateSession({username})
            return res.send({
                username: user.username,
                token: session.token,
                zone: user.zone,
            })
        } catch (error) {
            console.error(error)
            throw new errors.invalidCredentials()
        }
    }))

    app.post('/api/signup', f(async function (req, res) {
        const {signupCode, password, confirmPassword} = req.body
        const username = req.body.username.toLowerCase()
        if (!signupCode || !username || !password || !confirmPassword) {
            throw new Error('There is an invalid field')
        }

        if (password !== confirmPassword) throw new Error('Passwords are different')

        const user = await userModule.createUser({username, password, signupCode})

        const session = await sessionModule.generateSession({username})
        return res.send({
            username: user.username,
            token: session.token,
            zone: user.zone,
        })
    }))

    app.delete('/api/logout', isAuthenticated, f(async function (req, res) {
        const username = req.username
        const token = req.token
        await sessionModule.deleteSession(username, token)
        return res.sendStatus(200)
    }))

}
