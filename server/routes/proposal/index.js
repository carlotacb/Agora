const proposalsModule = require('../../modules/proposal')
const userModule = require('../../modules/user')
const {isAuthenticated, isProposalFromUserZone} = require('../middleware')
const f = require('../util').wrapAsyncRouterFunction
const {mapProposal, mapComment} = require('../mapper')

module.exports = app => {
    app.post('/api/proposal', isAuthenticated, f(async function (req, res) {
        const username = req.username
        const {title, content, location, categoria} = req.body
        const proposal = await proposalsModule.createProposal({username, title, content, location, categoria})
        res.send(await mapProposal(proposal))
    }))

    app.put('/api/proposal/:proposalId', isAuthenticated, isProposalFromUserZone, f(async function (req, res) {
        if (!req.params.proposalId) {
            res.sendStatus(400)
        }
        const proposalId = req.params.proposalId
        const proposal = await proposalsModule.getProposalById({id: proposalId})

        if (!proposal) {
            return res.sendStatus(404)
        } else if (proposal.owner !== req.username) {
            return res.sendStatus(403)
        }

        const {content, title, location} = req.body
        const newProposal = await proposalsModule.update({id: proposalId, content, title, location})
        res.send(await mapProposal(newProposal))
    }))

    app.get('/api/proposal', isAuthenticated, f(async function (req, res) {
        const user = await userModule.get({username: req.username})
        const query = {
            username: req.query.username,
            category: req.query.category,
            zone: user.zone
        }
        if (req.query.favorite) {
            if (user.favorites) {
                query.favorites = user.favorites
            }
        }
        const sort = {
            createdDateTime: 1
        }
        const proposals = await proposalsModule.getAllProposals(query, sort)
        proposals.forEach(function(proposal){
            if (user.favorites && user.favorites.includes(parseInt(proposal.id))){
                proposal.favorited = true
            }
            else proposal.favorited = false
        })
        res.send(proposals)

    }))

    app.get('/api/proposal/:proposalId', isAuthenticated, isProposalFromUserZone, f(async function (req, res) {
        const id = req.params.proposalId
        const user = await userModule.get({username: req.username})
        const proposal = await proposalsModule.getProposalById({id, zone: user.zone})

        if (!proposal) {
            return res.sendStatus(404)
        }

        proposal.favorited = user.favorites && user.favorites.includes(parseInt(id))

        res.send(await mapProposal(proposal))
    }))

    app.post('/api/proposal/:proposalId/favorite', isAuthenticated, isProposalFromUserZone, f(async function (req, res) {
        const id = req.params.proposalId
        const user = await userModule.get({username: req.username})
        const proposal = await proposalsModule.getProposalById({id, zone: user.zone})

        if (proposal) {
            if (user.favorites && user.favorites.includes(parseInt(id))){
                await userModule.unsetFavorite({id, user})
                proposal.favorited = false
            }
            else {
                await userModule.setFavorite({id, user})
                proposal.favorited = true
            }
        }

        return res.send(await mapProposal(proposal))
    }))

    app.post('/api/proposal/:proposalId/comment', isAuthenticated, isProposalFromUserZone, f(async function (req, res) {
        if (!req.body.comment) {
            throw new TypeError('Missing body field: comment')
        }
        const comment = req.body.comment.toString()
        const proposalId = req.params.proposalId
        const newComment = await proposalsModule.addComment({proposalId: proposalId, author: req.username, comment})
        return res.send(await mapComment(newComment))
    }))

    app.put('/api/proposal/:proposalId/comment/:idc', isAuthenticated, isProposalFromUserZone, f(async function (req, res) {
        const comment = req.body.comment.toString()
        const commentId = req.params.idc
        const proposalId = req.params.proposalId
        const updatedComment = await proposalsModule.editComment({
            proposalId: proposalId,
            author: req.username,
            commentId,
            comment
        })
        return res.send(await mapComment(updatedComment))
    }))

    app.delete('/api/proposal/:proposalId/comment/:idc', isAuthenticated, isProposalFromUserZone, f(async function (req, res) {
        const proposalId = req.params.proposalId
        const commentId = req.params.idc
        await proposalsModule.deleteComment({proposalId: proposalId, author: req.username, commentId: commentId})
        return res.sendStatus(200)
    }))

    app.post('/api/proposal/:proposalId/image', isAuthenticated, isProposalFromUserZone, f(async function (req, res) {
        if (!req.body.images) {
            throw new TypeError('Missing body field: images')
        }
        const images = req.body.images
        if (!Array.isArray(images)) {
            throw new TypeError('Images must be an array of images')
        }
        const proposalId = req.params.proposalId
        const proposal = await proposalsModule.addImage({proposalId: proposalId, author: req.username,images})
        return res.send(await mapProposal(proposal))
    }))

    app.delete('/api/proposal/:proposalId/image/:imageId', isAuthenticated, isProposalFromUserZone, f(async function (req, res) {
        const proposalId = req.params.proposalId
        const imageId = req.params.imageId
        await proposalsModule.deleteImage({proposalId: proposalId, author: req.username, imageId: imageId})
        return res.sendStatus(200)
    }))

    app.delete('/api/proposal/:proposalId', isAuthenticated, isProposalFromUserZone, f(async function (req, res) {
        const id = req.params.proposalId
        const proposal = await proposalsModule.getProposalById({id})
        if (proposal.owner !== req.username) {
            return res.sendStatus(403)
        }
        await proposalsModule.deleteProposal({id})
        res.sendStatus(200)
    }))

    app.post('/api/proposal/:proposalId/vote', isAuthenticated, isProposalFromUserZone, f(async function (req, res) {
        const proposalId = req.params.proposalId
        const vote = parseInt(req.body.vote)
        const username = req.username

        if (vote !== -1 && vote !== 1 && vote !== 0) {
            throw new Error('Invalid Vote')
        }

        await proposalsModule.voteProposal({proposalId, vote, username})
        const proposal = await proposalsModule.getProposalById({id: proposalId})

        res.send(await mapProposal(proposal))
    }))
}
