const db = require('./session.db')
const jwt = require('jsonwebtoken')
const config = require('../../config.js')


async function generateToken({username}) {
    const token = jwt.sign({ username: username, createdDate: Date.now()}, config.jwtSecretKey)
    await db.createToken({username,token})
    return token
}

module.exports = {
    generateToken: generateToken
}