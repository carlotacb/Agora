const db = require('./session.db')
const jwt = require('jsonwebtoken')
const config = require('../../config.js')


async function generateSession({username}) {
    const token = jwt.sign({ username: username, createdDate: Date.now()}, config.jwtSecretKey)
    return await db.create({username, token})
}

module.exports = {
    generateSession: generateSession,
    get: db.get,
    deleteSession: db.delete
}