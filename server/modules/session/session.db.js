const MongoClient = require('mongodb').MongoClient
const {constants, mongoDbUri} = require('../../config')
const dbConstants = constants.db

async function getCollection() {
    const db = await MongoClient.connect(mongoDbUri)
    return db.collection(dbConstants.tokens)
}

async function create({username, token}) {
    const object = {
        username: username,
        token: token,
        createdDateTime: new Date()
    }

    const collection = await getCollection()
    const insertResult = await collection.insertOne(object)
    return insertResult.ops[0]
}

async function get(token) {
    const query = {
        token: token
    }

    const collection = await getCollection()
    return collection.findOne(query)
}

module.exports = {
    create: create,
    get: get,
}