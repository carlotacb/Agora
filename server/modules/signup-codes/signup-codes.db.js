const {getCollection, collectionNames} = require('../db')
const collection = () => getCollection(collectionNames.signupCodes)

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

    return collection().updateOne(query, update, options)
}

async function getSignupCode(code) {
    const query = {
        code: code,
        usedBy: null
    }
    return collection().findOne(query)
}

module.exports = {
    useSignupCode: useSignupCode,
    getSignupCode: getSignupCode,
}


