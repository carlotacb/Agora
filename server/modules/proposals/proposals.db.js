const MongoClient = require('mongodb').MongoClient
const {constants, mongoDbUri} = require('../../config')
const dbConstants = constants.db

async function getCollection() {
    const db = await MongoClient.connect(mongoDbUri)
    return db.collection(dbConstants.proposals)
}


async function create({username, title, content}) {
    const object = {
        owner: username,
        title: title,
        content: content,
        createdDateTime: new Date(),
        updatedDateTime: null,
    }
    const collection = await getCollection()
    return collection.insertOne(object)
}

async function getAll() {
    const collection = await getCollection()
    return collection.find({}, {_id: 0}).toArray()
}

async function getByUsername({username}) {
    const collection = await getCollection()
    return collection.find({owner: username}, {_id: 0}).toArray()
}

async function getProposalById({id}) {
    const collection = await getCollection()
    return collection.findOne({id: parseInt(id)}, {_id: 0})
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

    const collection = await getCollection()
    return collection.findOneAndUpdate(query, update, options)
        .then(response => response.value)
}

async function deleteProposal({id}) {
    const collection = await getCollection()
    return collection.deleteOne({id:id})
}

module.exports = {
    create: create,
    update: update,
    getAll: getAll,
    getByUsername: getByUsername,
    getProposalById: getProposalById,
    delete: deleteProposal
}