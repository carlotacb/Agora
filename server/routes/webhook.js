const proposalsModule = require('../modules/proposal')
const userModule = require('../modules/user')
const {isAuthenticated} = require('./middleware')
const f = require('./util').wrapAsyncRouterFunction

module.exports = app => {

    app.post('/api/webhook/shared/twitter', isAuthenticated, f(async function (req, res) {
        const username = req.username
        const proposalId = req.body.proposalId

        const user = await userModule.get({username})
        const proposal = await proposalsModule.getProposalById({id: proposalId, zone: user.zone})

        if (!proposal) {
            throw new Error('Proposal not found')
        }

        await userModule.logSharedProposal({username, proposalId})

        return res.sendStatus(200)
    }))
}
