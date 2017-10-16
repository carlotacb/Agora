const crypto = require('crypto')
const config = require('../../config')
const db = require('./user.db.js')

function createUser({username, password}) {

}

async function login({username, password}) {
    const user = await db.get({username}).catch(console.erro)
    if (!user) {
        throw new Error(`Username "${username}" not found`)
    }
    const encryptedLoginPassword = encryptPassword(password)
    console.log('encrypted xina password', typeof encryptedLoginPassword);
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