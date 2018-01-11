const {getCollection, collectionNames} = require('../db')
const collection = () => getCollection(collectionNames.achievements)


async function getAchievements(username) {
    const query = {
        username: username.toString().toLowerCase()
    }

    const projection = {
        _id: 0
    }

    return collection().find(query, projection).toArray()
}

async function persistAchievements({username, achievements, isNotified = true}) {
    const newAchievementsToInsert = achievements.map(a => ({
        code: a,
        username,
        isNotified
    }))

    return collection().insertMany(newAchievementsToInsert)
}

async function setAchievementsAsNotified(username) {
    const query = {
        username: username.toString().toLowerCase()
    }

    const update = {
        $set: {
            isNotified: true
        }
    }

    return collection().updateMany(query, update)
}

module.exports = {
    getAchievements: getAchievements,
    persistAchievements: persistAchievements,
    setAchievementsAsNotified: setAchievementsAsNotified,
}