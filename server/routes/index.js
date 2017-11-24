const userModule = require('../modules/user')
const signupCodes = require('../modules/signup-codes')
const sessionModule = require('../modules/session')
const proposalsModule = require('../modules/proposals')
const config = require('../config')

const {isAuthenticated} = require('./middleware')

module.exports = app => {
    app.get('/', function (req, res) {
        return res.send({status: 'up'})
    })

    app.post('/api/login', async function (req, res) {
        try {
            const {username, password} = req.body
            const user = await userModule.login({username, password})
            const session = await sessionModule.generateSession({username})
            res.send({
                username: user.username,
                token: session.token
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

            const isWhitelistedCode = config.constants.whitelistedSignupCodes.includes(signupCode)

            if (!isWhitelistedCode) {
                const code = await signupCodes.getSignupCode(signupCode)
                if (!code) throw new Error(`Code used or not valid`)
            }

            const user = await userModule.createUser({username, password})

            if (!isWhitelistedCode) {
                await signupCodes.useSignupCode({code: signupCode, userId: username})
            }

            const session = await sessionModule.generateSession({username})
            res.send({
                username: user.username,
                token: session.token
            })
        } catch (error) {
            console.error('error on signup', error)
            res.sendStatus(403)
        }

    })

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
            const {cpCode, realname, description, neighborhood, bdate, sex} = req.body
            const user = await userModule.updateProfile({
                username: req.username,
                description,
                cpCode,
                realname,
                neighborhood,
                bdate,
                sex
            })
            res.json(user)
        } catch (error) {
            console.error('error on updating profile', error)
            res.sendStatus(403)
        }
    })

    app.post('/api/proposal', isAuthenticated, async function (req, res) {
        try {
            const username = req.username
            const {title, content, location} = req.body
            console.log(title + "\n" + content)
            const proposal = await proposalsModule.createProposal({username, title, content, location})
            res.send(proposal)
        } catch (error) {
            console.error('error on new post', error)
            res.sendStatus(500)
        }
    })

    app.put('/api/proposal/:id', isAuthenticated, async function (req, res) {
        try {
            if (!req.params.id) {
                res.sendStatus(400)
            }

            const proposalId = req.params.id
            const proposal = await proposalsModule.getProposalById({id: proposalId})

            if (!proposal) {
                return res.sendStatus(404)
            } else if (proposal.owner !== req.username) {
                return res.sendStatus(403)
            }

            const {content, title} = req.body
            const newProposal = await proposalsModule.update({id: proposalId, content, title})
            res.send(newProposal)
        } catch (error) {
            console.error('error editing proposal', error)
            res.sendStatus(500)
        }
    })

    app.get('/api/proposal', isAuthenticated, async function (req, res) {
        try {
            const username = req.username
            const proposals = await proposalsModule.getAllProposals()
            res.send(proposals)
        } catch (error) {
            console.error('error on get proposals', error)
            res.sendStatus(500)
        }
    })

    app.get('/api/proposal/user', isAuthenticated, async function (req, res) {
        try {
            const username = req.username
            const proposals = await proposalsModule.getProposalsByUsername({username})
            res.send(proposals)
        } catch (error) {
            console.error('error on get proposals', error)
            res.sendStatus(500)
        }
    })

    app.delete('/api/proposal/:id', isAuthenticated, async function (req, res) {
        try {
            const id = req.params.id
            const proposal = await proposalsModule.getProposalById({id})
            if (proposal.owner !== req.username) {
                return res.sendStatus(403)
            }
            await proposalsModule.deleteProposal({id})
            res.sendStatus(200)
        } catch (error) {
            console.error('error on delete post', error)
            res.sendStatus(500)
        }
    })

    app.delete('/api/logout', isAuthenticated, async function (req, res) {
        try {
            const username = req.username
            const token = req.token
            await sessionModule.deleteSession(username, token)
            res.sendStatus(200)
        } catch (error) {
            console.error('error on logout', error)
            res.sendStatus(500)
        }
    })

}
