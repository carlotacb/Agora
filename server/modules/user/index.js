const crypto = require('crypto')
const config = require('../../config')
const db = require('./user.db.js')

async function createUser({username, password}) {
    const encryptedPassword = encryptPassword(password)
    const user = await db.create({username: username, password: encryptedPassword})
    return user

}

async function login({username, password}) {
    const user = await db.get({username})
    if (!user) {
        throw new Error(`Username "${username}" not found`)
    }
    const encryptedLoginPassword = encryptPassword(password)
    if (encryptedLoginPassword !== user.password) {
        throw new Error(`Incorrect password for username "${username}"`)
    }
    return user
}

function encryptPassword(password) {
    return crypto.createHmac('sha256', config.secretKey)
        .update(password)
        .digest('hex');
}

module.exports = {
    createUser: createUser,
    login: login,
}