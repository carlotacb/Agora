const {getCollection, collectionNames, generateNextId} = require('../db')
const collection = () => getCollection(collectionNames.users)
const getNextId = () => generateNextId(collectionNames.users)

async function get({username}) {
    const query = {
        username: username
    }

    return await collection().findOne(query)
}

async function getProfile({username}) {
    return collection().findOne({username}, {_id: 0, password: 0})
}

async function updateProfile({username, description, cpCode, realname, neighborhood, bdate, sex}) {
    return collection().updateOne({username: username}, {$set: {cpCode: cpCode, realname: realname,
        neighborhood: neighborhood, bdate: bdate, sex: sex, description: description}})
}

async function updatePassword({username, newencryptedPassword}){
    return collection().updateOne({username: username}, {$set: {password: newencryptedPassword}});
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
    getProfile: getProfile,
    updateProfile: updateProfile,
    updatePassword: updatePassword,
    create: create,
}