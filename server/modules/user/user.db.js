const {getCollection, collectionNames, generateNextId} = require('../db')
const collection = () => getCollection(collectionNames.users)
const getNextId = () => generateNextId(collectionNames.users)

async function get({username}) {
    const query = {
        username: username
    }

    return await collection().findOne(query)
}

async function getByZone({user}) {
    const query = {
        zone: user.zone
    }
    const options = {
        _id: 0,
        password: 0,
        createdDateTime: 0,
        updatedDateTime: 0,
        zone : 0,
        favorites: []
    }
    return await collection().find(query, options).toArray()
}

async function getProfile({username}) {
    return collection().findOne({username}, {_id: 0, password: 0})
}

async function updateProfile({username, description, cpCode, realname, neighborhood, bdate, sex, image}) {
    return collection().updateOne({username: username}, {
        $set: {
            cpCode: cpCode,
            realname: realname,
            neighborhood: neighborhood,
            bdate: new Date(bdate),
            sex: sex,
            description: description,
            image: image,
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

    return collection().updateOne(query, update)
}

async function create({username, password, zone}) {
    const object = {
        id: await getNextId(),
        username: username.toString(),
        password: password,
        createdDateTime: new Date(),
        updatedDateTime: null,
        zone: parseInt(zone),
    }

    const insertResult = await collection().insertOne(object)
    return insertResult.ops[0]
}

async function setFavorite({id, user}){

    const query = {
        username: user.username,
    }

    const update = {
        $push: {
            favorites: parseInt(id)
        }
    }

    const options = {
        upsert: false,
        returnOriginal: false
    }

    return collection().findOneAndUpdate(query, update, options)
        .then(response => response.value)
}

async function unsetFavorite({id, user}){
    console.log('unsetFavorite')
    const query = {
        username: user.username,
    }

    const update = {
        $pull: {
            favorites: parseInt(id)
        }
    }

    const options = {
        multi: true
    }

    return collection().update(query, update, options)
            .then(response => response.value)
}

async function getProfilePicture(username) {
    const query = {
        username: username,
    }

    const projection = {
        image: 1,
        _id: 0
    }

    return collection().findOne(query, projection)
        .then(user => user && user.image ? user.image : null)
}

async function logSharedProposal({proposalId, username}) {
    const query = {
        username: username.toString().toLowerCase()
    }

    const update = {
        $addToSet: {
            sharedProposalIds: proposalId
        }
    }

    return collection().updateOne(query, update)
}

module.exports = {
    get: get,
    getProfile: getProfile,
    updateProfile: updateProfile,
    updatePassword: updatePassword,
    create: create,
    getByZone: getByZone,
    setFavorite: setFavorite,
    unsetFavorite: unsetFavorite,
    getProfilePicture: getProfilePicture,
    logSharedProposal: logSharedProposal,
}