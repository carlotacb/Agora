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

    app.put('/api/proposal/:id', isAuthenticated, async function (req, res) {
        try {
            if (!req.params.id) {
                res.sendStatus(400)
            }

            const proposalId = req.params.id
            const proposal = await proposalsModule.getProposalById({id: proposalId})

            if (!proposal) {
                return res.sendStatus(404)
            } else if (proposal.owner !== req.username) {
                return res.sendStatus(403)
            }

            const {content, title, location} = req.body
            const newProposal = await proposalsModule.update({id: proposalId, content, title, location})
            res.send(newProposal)
        } catch (error) {
            console.error('error editing proposal', error)
            res.sendStatus(500)
        }
    })

    app.get('/api/proposal', isAuthenticated, async function (req, res) {
        try {
            const user = await userModule.get({username: req.username})
            const query = {
                username: req.query.username,
                category: req.query.category,
                zone: user.zone
            }
            const sort = {
                createdDateTime: 1
            }
            const proposals = await proposalsModule.getAllProposals(query, sort)
            res.send(proposals)
        } catch (error) {
            console.error('error on get proposals', error)
            res.sendStatus(500)
        }
    })

    app.get('/api/proposal/:id', isAuthenticated, async function (req, res) {
        try {
            const id = req.params.id
            const user = await userModule.get({username: req.username})
            const proposal = await proposalsModule.getProposalById({id, zone: user.zone})

            if (!proposal) {
                return res.sendStatus(404)
            }

            res.send(proposal)
        } catch (error) {
            console.error('error on get proposals', error)
            res.sendStatus(500)
        }
    })

    app.post('/api/proposal/:id/comment', isAuthenticated, async function (req, res) {
        try {
            const comment = req.body.comment.toString()
            const proposalId = req.params.id
            const newComment = await proposalsModule.addComment({proposalId: proposalId, author: req.username, comment})
            return res.send(newComment)
        } catch (error) {
            console.error('Error commenting proposal', error)
            res.sendStatus(500)
        }
    })

    app.put('/api/proposal/:id/comment/:idc', isAuthenticated, async function (req, res) {
        try {
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
        } catch (error) {
            console.error('Error commenting proposal', error)
            res.sendStatus(500)
        }
    })

    app.delete('/api/proposal/:id/comment/:idc', isAuthenticated, async function (req, res) {
        try {
            const proposalId = req.params.id
            const commentId = req.params.idc
            console.log(proposalId)
            console.log(commentId)
            await proposalsModule.deleteComment({proposalId: proposalId, author: req.username, commentId: commentId})
            return res.sendStatus(200)
        } catch (error) {
            console.error('Error deleting proposal', error)
            res.sendStatus(500)
        }
    })

    app.delete('/api/proposal/:id', isAuthenticated, async function (req, res) {
        try {
            const id = req.params.id
            const proposal = await proposalsModule.getProposalById({id})
            if (proposal.owner !== req.username) {
                return res.sendStatus(403)
            }
            await proposalsModule.deleteProposal({id})
            res.sendStatus(200)
        } catch (error) {
            console.error('error on delete post', error)
            res.sendStatus(500)
        }
    })
}
