const db = require('./proposals.db')
const dbUsers = require('../user/user.db')
const jwt = require('jsonwebtoken')
const config = require('../../config.js')


async function createProposal({username, title, content}) {
    const existingUser = await dbUsers.get({username})
    if (!existingUser) throw new Error(`User does not exists`)
    const proposal = await db.create({username, title, content})
    return proposal
}

module.exports = {
    createProposal: createProposal
}