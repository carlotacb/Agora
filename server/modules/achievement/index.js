const db = require('./achievement.db')
const proposalModule = require('../proposal')
const userModule = require('../user')
const achievementTypes = require('./types.js')

const calculateAchievementsFromTypeAndNumber = (type, number) => Object
    .keys(type)
    .filter(n => number >= n)
    .map(n => type[n])

async function calculateProposalsAchievements(username) {
    const numberOfProposals = await proposalModule.countProposalsByUsername(username)

    return calculateAchievementsFromTypeAndNumber(achievementTypes.proposals.publishedProposals, numberOfProposals)
}

async function calculateUserAchievements(username) {
    const user = await userModule.get({username})

    const numberOfSharedProposals = user.sharedProposalIds && Array.isArray(user.sharedProposalIds) ?
        user.sharedProposalIds.length : 0

    return calculateAchievementsFromTypeAndNumber(achievementTypes.shared.proposalsSharedOnTwitter, numberOfSharedProposals)
}

async function getCalculatedAchievements(username) {
    const proposalsAchievements = await calculateProposalsAchievements(username)
    const userAchievements = await calculateUserAchievements(username)


    return [...proposalsAchievements, ...userAchievements]
}

async function getNewAchievements(username) {
    const calculatedAchievements = await getCalculatedAchievements(username)
    const persistedAchievements = await db.getAchievements(username)

    const newAchievements = calculatedAchievements
        .filter(achievement => {
            const persistedAchievement = persistedAchievements.find(a => a.code === achievement)
            return !persistedAchievement || (persistedAchievement && !persistedAchievement.isNotified)
        })

    if (Array.isArray(newAchievements) && newAchievements.length > 0) {
        await db.persistAchievements({username, achievements: newAchievements})
    }


    return newAchievements
}

function getAllAchievementCodesFromObject(obj) {
    const codes = []
    for (let key in obj) {
        if (typeof obj[key] === 'object') {
            codes.push(...getAllAchievementCodesFromObject(obj[key]))
        } else {
            codes.push(obj[key])
        }
    }
    return codes
}

async function getMissingAchievementsFromAchievements(achievements) {
    const allAchievements = getAllAchievementCodesFromObject(achievementTypes)
    const userAchievements = new Set(achievements.map(a => a.code))

    return allAchievements.filter(achievement => !userAchievements.has(achievement))
}


module.exports = {
    getNewAchievements: getNewAchievements,
    getAndNotifyPersistentAchievements: db.getAchievements,
    setAchievementsAsNotified: db.setAchievementsAsNotified,
    getMissingAchievementsFromAchievements: getMissingAchievementsFromAchievements,
}