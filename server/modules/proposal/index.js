const db = require('./proposals.db')
const userModule = require('../user')
const zoneModule = require('../zone')

async function createProposal({username, title, content, location, categoria}) {
    const existingUser = await userModule.get({username})
    if (!existingUser) {
        throw new Error('User does not exists')
    }

    const zone = existingUser.zone
    await zoneModule.throwIfInvalidLocationForZone({location, zoneId: zone})

    return await db.create({username, title, content, location, zone: zone, categoria})
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

    const user = await userModule.get({username: author})

    if (!user) {
        throw new Error('User not found')
    }

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

async function voteProposal({proposalId, vote, username}) {
    return await db.voteProposal({proposalId, vote, username})
}

async function addImage({proposalId, author, images}) {
    const proposal = await db.getProposalById({id: proposalId})
    if (!proposal) {
        throw new Error('Proposal not found')
    }

    const user = await userModule.get({username: author})

    if (!user) {
        throw new Error('User not found')
    }
    else if (author !== proposal.owner) {
        throw new Error('User is not the owner of the proposal')
    }

    return db.addImage({proposalId, images})
}

async function deleteImage({proposalId, author, imageId}) {
    const proposal = await db.getProposalById({id: proposalId})
    if (!proposal) {
        throw new Error('Proposal not found')
    }

    const user = await userModule.get({username: author})

    if (!user) {
        throw new Error('User not found')
    }
    else if (author !== proposal.owner) {
        throw new Error('User is not the owner of the proposal')
    }

        return db.deleteImage({
        proposalId,
        author,
        imageId,
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
    voteProposal: voteProposal,
    addImage: addImage,
    deleteImage: deleteImage,
    countProposalsByUsername: db.countProposalsByUsername,
    getProposalsCommentedByUsername: db.getProposalsCommentedByUsername
}