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

async function getAllProposals() {
    const proposals = await db.getAll();
    if (!proposals) throw new Error(`No Proposals`)
    return proposals
}

module.exports = {
    createProposal: createProposal,
    getAllProposals: getAllProposals,
}