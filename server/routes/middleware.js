const sessionModule = require('../modules/session')
const userModule = require('../modules/user')
const proposalModule = require('../modules/proposal')
const errors = require('../modules/error')

function logRequest(req) {
    console.log(`Request - ${JSON.stringify({
        method: req.method,
        username: req.username,
        url: req.url,
        body: req.body,
    }, null, 4)}`);
}

async function isAuthenticated(req, res, next) {
    const authorizationToken = req.headers.authorization

    if (!authorizationToken) {
        return next(new errors.missingToken())
    }

    const session = await sessionModule.get(authorizationToken)

    if (!session) {
        return next(new errors.invalidToken())
    }

    req.username = session.username
    req.token = session.token

    logRequest(req)

    return next()
}

async function isProposalFromUserZone(req, res, next) {
    const username = req.username
    const proposalId = req.params.proposalId

    if (!username || !proposalId) {
        next(new TypeError('Missing required parameters'))
    }

    const proposal = await proposalModule.getProposalById({id: proposalId})

    if (!proposal) {
        return next(new errors.proposalNotFound(proposalId))
    }

    const user = await userModule.get({username: username})

    if (user.zone === proposal.zone) {
        return next()
    } else {
        return next(new errors.proposalOutsideZone(user.zone, proposal.zone))
    }
}

module.exports = {
    isAuthenticated: isAuthenticated,
    isProposalFromUserZone: isProposalFromUserZone,
}