const {getCollection, collectionNames, generateNextId} = require('../db')
const collection = () => getCollection(collectionNames.users)
const getNextId = () => generateNextId(collectionNames.users)

async function get({username}) {
    const query = {
        username: username
    }

    return await collection().findOne(query)
}

async function create({username, password}) {
    const object = {
        id: await getNextId(),
        username: username,
        password: password,
        createdDateTime: new Date()
    }

    const insertResult = await collection().insertOne(object)
    return insertResult.ops[0]
}

module.exports = {
    get: get,
    create: create,
}