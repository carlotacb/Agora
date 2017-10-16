const MongoClient = require('mongodb').MongoClient
const {constants, mongoDbUri} = require('../../config')
const dbConstants = constants.db

async function getCollection() {
    const db = await MongoClient.connect(mongoDbUri)
    return db.collection(dbConstants.users)
}

async function get({username}) {
    const query = {
        username: username
    }

    const collection = await getCollection()
    return collection.findOne(query)
}

module.exports = {
    get: get,
}