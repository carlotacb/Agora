const crypto = require('crypto')
const config = require('../../config')
const db = require('./user.db.js')

async function createUser({username, password}) {
    const existingUser = await db.get({username})
    if (existingUser) {
        throw new Error(`Username already used`)
    }

    const encryptedPassword = encryptPassword(password)
    const user = await db.create({username: username, password: encryptedPassword})

    if (!user) {
        throw new Error(`Could not create the user`)
    }
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

async function updatePassword({username, oldpassword, password, confirmpassword}){
    const user = await db.get({username});
    if (!user) {
        throw new Error(`Username "${username}" not found`)
    }
    const encryptedPassword =  encryptPassword(oldpassword)
    if (encryptedPassword !== user.password) {
        throw new Error(`Incorrect password for username "${username}"`)
    }
    if (password !== confirmpassword) {
        throw new Error(`Passwords are not the same one`)
    }
    const newencryptedPassword = encryptPassword(password)
    return db.updatePassword({username, newencryptedPassword});
}

module.exports = {
    createUser: createUser,
    login: login,
    get: db.get,
    getProfile: db.getProfile,
    updateProfile: db.updateProfile,
    updatePassword: updatePassword,
}