const {getCollection, collectionNames, generateNextId} = require('../db')
const collection = () => getCollection(collectionNames.proposals)
const getNextId = () => generateNextId(collectionNames.proposals)

async function create({username, title, content}) {
    const object = {
        id: await getNextId(),
        owner: username,
        title: title,
        content: content,
        createdDateTime: new Date()
    }
    return collection().insertOne(object)
}

async function getAll() {
    return collection().find().toArray()
}

async function getByUsername({username}) {
    const collection = await getCollection()
    return collection.find({owner: username}, {_id: 0}).toArray()
}

async function deleteProposal({id}) {
    const collection = await getCollection()
    return collection.deleteOne({id:id})
}

module.exports = {
    create: create,
    getAll: getAll,
    getByUsername: getByUsername,
    delete: deleteProposal
}