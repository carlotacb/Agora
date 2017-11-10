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

async function getProfile({username}) {
    const collection = await getCollection()
    return collection.findOne({username}, {_id: 0, password: 0})
}

async function updateProfile({username, description, cpCode, realname, neighborhood, bdate, sex}) {
    const collection = await getCollection()
    return collection.updateOne({username: username}, {$set: {cpCode: cpCode, realname: realname,
        neighborhood: neighborhood, bdate: bdate, sex: sex, description: description}})
}

async function create({username, password}) {
    const object = {
        username: username,
        password: password,
        createdDateTime: new Date()
    }

    const collection = await getCollection()
    const insertResult = await collection.insertOne(object)
    return insertResult.ops[0]
}

module.exports = {
    get: get,
    getProfile: getProfile,
    updateProfile: updateProfile,
    create: create,
}