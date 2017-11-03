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
        content: content
    }
    const collection = await getCollection()
    return collection.insertOne(object)
}

async function getAll() {
    const collection = await getCollection()
    return collection.find({}, {_id: 0}).toArray()
}

async function deleteProposal({id}) {
    const collection = await getCollection()
    return collection.deleteOne({id:id})
}

module.exports = {
    create: create,
    getAll: getAll,
    delete: deleteProposal
}