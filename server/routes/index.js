const userModule = require('../modules/user')
const signupCodes = require('../modules/signup-codes')
const sessionModule = require('../modules/session')
const proposalsModule = require('../modules/proposals')

module.exports = app => {
    app.get('/', function (req, res) {
        return res.send({status: 'up'})
    })

    app.post('/api/login', async function (req, res) {
        try {
            const {username, password} = req.body
            const user = await userModule.login({username, password})
            const token = await sessionModule.generateToken({username})
            res.send({
                username: user.username,
                token: token
            })
        } catch (error) {
            console.error('error on login', error)
            res.sendStatus(403)
        }
    })

    app.post('/api/signup', async function (req, res) {
        try {
            const {signupCode, username, password, confirmPassword} = req.body
            if (!signupCode || !username || !password || !confirmPassword) throw new Error(`There is an invalid field`)
            if (password !== confirmPassword) throw new Error(`Passwords are different`)
            const code = await signupCodes.getSignupCode(signupCode)
            if (!code) throw new Error(`Code used or not valid`)
            const user = await userModule.createUser({username, password})
            if (!user) throw new Error(`Could not create the user`)
            if (code.code!=="007")await signupCodes.useSignupCode({code: signupCode, userId: username})
            const token = await sessionModule.generateToken({username})
            res.send({
                username: user.username,
                token: token
            })
        } catch (error) {
            console.error('error on signup', error)
            res.sendStatus(403)
        }

    })

    app.post('/api/proposal', async function (req, res) {
        try {
            const username = 'userDemo'
            const {title, content} = req.body
            console.log(title + "\n" + content)
            const proposal = await proposalsModule.createProposal({username, title, content})
            res.send(proposal)
        } catch (error) {
            console.error('error on new post', error)
            res.sendStatus(403)
        }
    })

    app.get('/api/proposal', async function (req, res) {
        try {
            const proposals = await proposalsModule.getAllProposals()
            res.send(proposals)
        } catch (error) {
            console.error('error on get proposals', error)
            res.sendStatus(403)
        }
    })


}
