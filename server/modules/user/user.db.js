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
    return collection().updateOne({username: username}, {
        $set: {
            cpCode: cpCode,
            realname: realname,
            neighborhood: neighborhood,
            bdate: new Date(bdate),
            sex: sex,
            description: description,
            updatedDateTime: new Date()
        }
    })
}

async function updatePassword({username, newencryptedPassword}) {
    const query = {
        username: username
    }

    const update = {
        $set: {
            password: newencryptedPassword,
            updatedDateTime: new Date()
        }
    }

    return collection().updateOne(query, update);
}

async function create({username, password}) {
    const object = {
        id: await getNextId(),
        username: username,
        password: password,
        createdDateTime: new Date(),
        updatedDateTime: null,
        zone: null,
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