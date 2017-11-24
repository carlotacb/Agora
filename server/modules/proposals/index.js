const db = require('./proposals.db')
const userModule = require('../user')

async function createProposal({username, title, content}) {
    const existingUser = await userModule.get({username})
    if (!existingUser) {
        throw new Error(`User does not exists`)
    }
    return await db.create({username, title, content})
}

module.exports = {
    createProposal: createProposal,
    getAllProposals: db.getAllBy,
    update: db.update,
    deleteProposal: db.delete,
    getProposalById: db.getProposalById,
}