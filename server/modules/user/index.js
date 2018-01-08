const crypto = require('crypto')
const config = require('../../config')
const db = require('./user.db.js')
const zoneModule = require('../zone')
const signupCodes = require('../signup-code')

async function createUser({username, password, signupCode}) {
    if (!username || !password || !signupCode) {
        throw new TypeError('Missing arguments')
    }

    username = username.toLowerCase()

    const existingUser = await db.get({username})
    if (existingUser) {
        throw new Error(`Username already used`)
    }

    const isWhitelistedCode = config.constants.whitelistedSignupCodes.includes(signupCode)

    if (!isWhitelistedCode) {
        const code = await signupCodes.getSignupCode(signupCode)
        if (!code) throw new Error(`Code used or not valid`)
    }

    const encryptedPassword = encryptPassword(password)

    const zone = await zoneModule.getZoneForSignupCode(signupCode)
    const user = await db.create({username: username, password: encryptedPassword, zone: zone.id})

    if (!user) {
        throw new Error(`Could not create the user`)
    }

    if (!isWhitelistedCode) {
        await signupCodes.useSignupCode({code: signupCode, userId: username})
    }


    return user
}

async function login({username, password}) {
    username = username.toLowerCase()
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
    getProfilePicture: db.getProfilePicture,
    getProfile: db.getProfile,
    updateProfile: db.updateProfile,
    updatePassword: updatePassword,
    getByZone: db.getByZone,
    setFavorite: db.setFavorite,
    unsetFavorite: db.unsetFavorite,
    logSharedProposal: db.logSharedProposal,
}