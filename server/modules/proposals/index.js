const db = require('./session.db')
const jwt = require('jsonwebtoken')
const config = require('../../config.js')


async function createProposal({username, title, body}) {
    const existingUser = await db.get({username})
    if (!existingUser) throw new Error(`User does not exists`)
    const proposal = await db.create(username, title, body)
    return proposal
}

module.exports = {
    createProposal: db.createProposal
}