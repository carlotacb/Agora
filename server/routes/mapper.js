const userModule = require('../modules/user')

async function mapComment(comment) {
    try {
        if (comment.author && comment.author.username) {
            comment.author.image = await userModule.getProfilePicture(comment.author.username)
        }

        return comment
    } catch (error) {
        console.error(error)
        return comment
    }
}

async function mapProposalForUsername(proposal, username, options = {mapComments: true}) {
    try {
        delete proposal._id

        proposal.userVoted = proposal.upvotesUsernames && proposal.upvotesUsernames.includes(username) ? 1 :
            proposal.downvotesUsernames && proposal.downvotesUsernames.includes(username) ? -1 : 0

        proposal.numberUpvotes = proposal.upvotesUsernames ? proposal.upvotesUsernames.length : 0
        proposal.numberDownvotes = proposal.downvotesUsernames ? proposal.downvotesUsernames.length : 0

        delete proposal.upvotesUsernames
        delete proposal.downvotesUsernames

        if (options.mapComments) {
            proposal.comments = proposal.comments ? await Promise.all(proposal.comments.map(mapComment)) : []
        }

        const user = await userModule.get({username})

        proposal.favorited = !!(user.favorites && user.favorites.includes(parseInt(proposal.id)))

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