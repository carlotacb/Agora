const {getCollection, collectionNames, generateNextId} = require('../db')
const collection = () => getCollection(collectionNames.proposals)
const getNextId = () => generateNextId(collectionNames.proposals)

async function create({username, title, content, location}) {
    const object = {
        id: await getNextId(),
        owner: username,
        title: title,
        content: content,
        createdDateTime: new Date(),
        updatedDateTime: null,
        comments: []
    }
    if (location && location.lat && location.long) {
        object.location = {
            lat: location.lat,
            long: location.long,
        }
    }
    return collection().insertOne(object)
}

async function getAllBy({username}) {

    const query = {}

    if (username) {
        query.owner = username.toString()
    }

    return collection().find(query).toArray()
}

async function getByUsername({username}) {
    return collection().find({owner: username}, {_id: 0}).toArray()
}

async function getProposalById({id}) {
    return collection().findOne({id: parseInt(id)}, {_id: 0})
}

async function update({id, content, title}) {
    const query = {
        id: parseInt(id)
    }

    const update = {
        $set: {
            updatedDateTime: new Date()
        }
    }

    const options = {
        upsert: false,
        returnOriginal: false
    }

    if (content) {
        update.$set.content = content
    }

    if (title) {
        update.$set.title = title
    }

    return collection().findOneAndUpdate(query, update, options)
        .then(response => response.value)
}

async function deleteProposal({id}) {
    return collection().deleteOne({id: parseInt(id)})
}

async function addComment({proposalId, author, comment}) {
    const query = {
        id: parseInt(proposalId),
    }

    const update = {
        $push: {
            comments: {
                id: await generateNextId('comment'),
                createdDateTime: new Date(),
                updatedDateTime: null,
                author: {
                    id: parseInt(author.id),
                    username: author.username.toString()
                },
                comment: comment.toString()
            }
        }
    }

    const options = {
        upsert: false,
        returnOriginal: false
    }

    return collection().findOneAndUpdate(query, update, options)
        .then(response => response.value)
}

async function deleteComment({proposalId, author, commentId}) {
    const query = {
        id: parseInt(proposalId),

    }

    const update = {
        $pull: {
            comments: {
                id: commentId,
                author: {
                    username: author,
                }
            }
        }
    }

    const options = {
        multi: true
    }

    return collection().findOneAndUpdate(query, update, options)
        .then(response => response.value)
}


module.exports = {
    create: create,
    update: update,
    getAllBy: getAllBy,
    getByUsername: getByUsername,
    getProposalById: getProposalById,
    addComment: addComment,
    delete: deleteProposal
}