const db = require('./achievement.db')
const proposalModule = require('../proposal')
const achievementTypes = require('./types.js')

async function getCalculatedAchievements(username) {
    let achievements = []

    const numberOfProposals = await proposalModule.countProposalsByUsername(username)

    const achievementsFromNumberOfProposals = Object.keys(achievementTypes.proposals.numberPublished)
        .filter(number => numberOfProposals >= number)
        .map(number => achievementTypes.proposals.numberPublished[number])


    achievements = achievements.concat(achievementsFromNumberOfProposals)

    return achievements
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