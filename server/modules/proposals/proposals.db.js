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

module.exports = {
    create: create,
}