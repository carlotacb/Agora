async function mapComment(comment) {
    try {
        return comment
    } catch (error) {
        console.error(error)
        return comment
    }}

async function mapProposal(proposal) {
    try {
        return proposal
    } catch (error) {
        console.error(error)
        return proposal
    }
}

module.exports = {
    mapComment: mapComment,
    mapProposal: mapProposal,
}