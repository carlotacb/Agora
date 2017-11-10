const MongoClient = require('mongodb').MongoClient
const config = require('../../config')


let dbConnection = null

const collectionNames = {
    signupCodes: 'signup_codes',
    users: 'users',
    tokens: 'tokens',
    proposals: 'proposals',
    idSequence: 'id_counter',
}

const connect = () => MongoClient.connect(config.mongoDbUri)
    .then(db => dbConnection = db)
    .then(() => console.log('Successfully connected to DB'))

const getCollection = collection => {
    if (!dbConnection) {
        throw new Error('Not connected to DB')
    }
    return dbConnection.collection(collection)
}

const generateNextId = collectionName => {
    if (!dbConnection) {
        reject('Not connected to DB')
    }
    const collection = getCollection(collectionNames.idSequence)

    const query = {
        collection: collectionName
    }

    const update = {
        $inc: {
            id: 1
        }
    }

    const options = {
        upsert: true,
        returnNewDocument: true
    }

    return collection
        .findOneAndUpdate(query, update, options)
        .then(response => response.value.id)
}


module.exports = {
    collectionNames: collectionNames,
    connect: connect,
    getCollection: getCollection,
    generateNextId: generateNextId,
}