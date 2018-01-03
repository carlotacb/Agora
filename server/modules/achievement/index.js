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

module.exports = {
    getNewAchievements: getNewAchievements,
    getAndNotifyPersistentAchievements: db.getAchievements,
    setAchievementsAsNotified: db.setAchievementsAsNotified,
}