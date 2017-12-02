const db = require('./proposals.db')
const userModule = require('../user')

async function createProposal({username, title, content, location}) {
    const existingUser = await userModule.get({username})
    if (!existingUser) {
        throw new Error(`User does not exists`)
    }
    return await db.create({username, title, content, location, zone: existingUser.zone})
}

async function addComment({proposalId, author, comment}) {
    const proposal = await db.getProposalById({id: proposalId})
    if (!proposal) {
        throw new Error('Proposal not found')
    }

    const user = await userModule.get({username: author})

    if (!user) {
        throw new Error('User not found')
    }

    return db.addComment({
        proposalId,
        author: {
            username: user.username,
            id: user.id
        },
        comment
    })
}

async function deleteComment({proposalId, author, commentId}) {
    const proposal = await db.getProposalById({id: proposalId})
    if (!proposal) {
        throw new Error('Proposal not found')
    }

    console.log(proposalId)
    console.log(commentId)
    console.log(author)

    const user = await userModule.get({username: author})

    if (!user) {
        throw new Error('User not found')
    }

    console.log(proposalId)
    console.log(commentId)
    console.log(author)
    return db.deleteComment({
        proposalId,
        author,
        commentId,
    })
}

async function editComment({proposalId, author, comment, commentId}) {
    const proposal = await db.getProposalById({id: proposalId})
    if (!proposal) {
        throw new Error('Proposal not found')
    }

    const user = await userModule.get({username: author})

    if (!user) {
        throw new Error('User not found')
    }

    return await db.editComment({
        proposalId,
        author,
        commentId,
        comment,
    })
}

module.exports = {
    createProposal: createProposal,
    getAllProposals: db.getAllBy,
    update: db.update,
    deleteProposal: db.delete,
    getProposalById: db.getProposalById,
    addComment: addComment,
    editComment: editComment,
    deleteComment: deleteComment,
}