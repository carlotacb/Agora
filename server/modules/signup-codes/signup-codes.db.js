const MongoClient = require('mongodb').MongoClient
const {constants, mongoDbUri} = require('../../config')
const dbConstants = constants.db

async function getCollection() {
    const db = await MongoClient.connect(mongoDbUri)
    return db.collection(dbConstants)
}

async function createSignupCode(code) {
    const object = {
        code: code,
        usedBy: null,
    }
}

async function useSignupCode({code, userId}) {
    const query = {
        code: code,
        usedBy: null
    }

    const update = {
        usedBy: userId,
    }

    const options = {
        upsert: false
    }

    return getCollection().updateOne(query, update, options)
}

async function getSignupCode(code) {
    const query = {
        code: code
    }

    return getCollection().findOne(query)
}

module.exports = {
    createSignupCode: createSignupCode,
    useSignupCode: useSignupCode,
    getSignupCode: getSignupCode,
}


