const {getProfilePicture} = require('../modules/user')

async function mapComment(comment) {
    try {
        if (comment.author && comment.author.username) {
            comment.author.image = await getProfilePicture(comment.author.username)
        }

        return comment
    } catch (error) {
        console.error(error)
        return comment
    }
}

async function mapProposalForUsername(proposal, username) {
    try {
        delete proposal._id

        proposal.userVoted = proposal.upvotesUsernames && proposal.upvotesUsernames.includes(username) ? 1 :
            proposal.downvotesUsernames && proposal.downvotesUsernames.includes(username) ? -1 : 0

        proposal.numberUpvotes = proposal.upvotesUsernames ? proposal.upvotesUsernames.length : 0
        proposal.numberDownvotes = proposal.downvotesUsernames ? proposal.downvotesUsernames.length : 0

        delete proposal.upvotesUsernames
        delete proposal.downvotesUsernames

        proposal.comments = proposal.comments ? await Promise.all(proposal.comments.map(mapComment)) : []

        return proposal
    } catch (error) {
        console.error(error)
        return proposal
    }
}

module.exports = {
    mapComment: mapComment,
    mapProposalForUsername: mapProposalForUsername,
}