const {getCollection, collectionNames, generateNextId} = require('../db')
const collection = () => getCollection(collectionNames.tokens)
const getNextId = () => generateNextId(collectionNames.tokens)

async function create({username, token}) {
    const object = {
        id: await getNextId(),
        username: username,
        token: token,
        createdDateTime: new Date()
    }

    const insertResult = await collection().insertOne(object)
    return insertResult.ops[0]
}

async function get(token) {
    const query = {
        token: token
    }

    return collection().findOne(query)
}

async function deleteSession(username, token) {
    return collection().deleteOne({username, token})
}

module.exports = {
    create: create,
    get: get,
    delete: deleteSession
}