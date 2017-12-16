const proposalsModule = require('../../modules/proposal')
const userModule = require('../../modules/user')
const {isAuthenticated} = require('../middleware')
const f = require('../util').wrapAsyncRouterFunction

module.exports = app => {
    app.post('/api/proposal', isAuthenticated, f(async function (req, res) {
        const username = req.username
        const {title, content, location, categoria} = req.body
        const proposal = await proposalsModule.createProposal({username, title, content, location, categoria})
        res.send(proposal)
    }))

    app.put('/api/proposal/:id', isAuthenticated, f(async function (req, res) {
        if (!req.params.id) {
            res.sendStatus(400)
        }


        const proposalId = req.params.id
        const proposal = await proposalsModule.getProposalById({id: proposalId})

        console.log('username', req.username, 'owner', proposal.owner, req.body)

        if (!proposal) {
            return res.sendStatus(404)
        } else if (proposal.owner !== req.username) {
            return res.sendStatus(403)
        }
        console.log(req.body)
        const {content, title, location} = req.body
        const newProposal = await proposalsModule.update({id: proposalId, content, title, location})
        res.send(newProposal)
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

    app.get('/api/proposal/:id', isAuthenticated, f(async function (req, res) {
        const id = req.params.id
        const user = await userModule.get({username: req.username})
        const proposal = await proposalsModule.getProposalById({id, zone: user.zone})

        if (!proposal) {
            return res.sendStatus(404)
        }
        if (user.favorites && user.favorites.includes(parseInt(id))){
            proposal.favorited = true
        }
        else proposal.favorited = false
        res.send(proposal)
    }))

    app.post('/api/proposal/:id/favorite', isAuthenticated, f(async function (req, res) {
        const id = req.params.id
        const user = await userModule.get({username: req.username})
        const proposal = await proposalsModule.getProposalById({id, zone: user.zone})
        console.log(user.favorites)
        console.log(proposal)
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
        return res.send(proposal)
    }))

    app.post('/api/proposal/:id/comment', isAuthenticated, f(async function (req, res) {
        const comment = req.body.comment.toString()
        const proposalId = req.params.id
        const newComment = await proposalsModule.addComment({proposalId: proposalId, author: req.username, comment})
        return res.send(newComment)
    }))

    app.put('/api/proposal/:id/comment/:idc', isAuthenticated, f(async function (req, res) {
        const comment = req.body.comment.toString()
        const commentId = req.params.idc
        const proposalId = req.params.id
        const updatedComment = await proposalsModule.editComment({
            proposalId: proposalId,
            author: req.username,
            commentId,
            comment
        })
        return res.send(updatedComment)
    }))

    app.delete('/api/proposal/:id/comment/:idc', isAuthenticated, f(async function (req, res) {
        const proposalId = req.params.id
        const commentId = req.params.idc
        console.log(proposalId)
        console.log(commentId)
        await proposalsModule.deleteComment({proposalId: proposalId, author: req.username, commentId: commentId})
        return res.sendStatus(200)
    }))

    app.delete('/api/proposal/:id', isAuthenticated, f(async function (req, res) {
        const id = req.params.id
        const proposal = await proposalsModule.getProposalById({id})
        if (proposal.owner !== req.username) {
            return res.sendStatus(403)
        }
        await proposalsModule.deleteProposal({id})
        res.sendStatus(200)
    }))
}
