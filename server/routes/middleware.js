const sessionModule = require('../modules/session')

async function isAuthenticated(req, res, next) {
    const authorizationToken = req.headers.authorization

    if (!authorizationToken) {
        return res.status(403).json({error: 'Missing Authorization token'})
    }

    const session = await sessionModule.get(authorizationToken)

    if (!session) {
        return res.status(403).json({error: 'Invalid Authorization token'})
    }

    req.username = session.username
    req.token = session.token

    return next()
}

module.exports = {
    isAuthenticated: isAuthenticated
}