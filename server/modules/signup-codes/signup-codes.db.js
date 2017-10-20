const MongoClient = require('mongodb').MongoClient
const {constants, mongoDbUri} = require('../../config')
const dbConstants = constants.db

async function getCollection() {
    const db = await MongoClient.connect(mongoDbUri)
    return db.collection(dbConstants.signupCodes)
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
        $set: {usedBy: userId,}
    }

    const options = {
        upsert: false
    }

    const collection = await getCollection()
    return collection.updateOne(query, update, options)
}

async function getSignupCode(code) {
    const query = {
        code: code,
        usedBy: null
    }
    const collection = await getCollection()
    return collection.findOne(query)
}

module.exports = {
    createSignupCode: createSignupCode,
    useSignupCode: useSignupCode,
    getSignupCode: getSignupCode,
}


