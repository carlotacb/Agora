const db = require('./achievement.db')
const proposalModule = require('../proposal')
const userModule = require('../user')
const achievementTypes = require('./types.js')

const calculateAchievementsFromTypeAndNumber = (type, number) => Object
    .keys(type)
    .filter(n => number >= n)
    .map(n => type[n])

async function calculateProposalsAchievements(user) {
    const proposals = await proposalModule.getAllProposals({zone: user.zone})
    const proposalsByUser = proposals.filter(p => p.owner === user.username)
    const proposalsByOtherUsers = proposals.filter(p => p.owner !== user.username)

    const reduceProposalUpVotes = (total, p) => total + p && p.upvotesUsernames
        && Array.isArray(p.upvotesUsernames) ? p.upvotesUsernames.length : 0

    const reduceProposalUpVotesGave = (total, p) => total + p && p.upvotesUsernames
        && Array.isArray(p.upvotesUsernames) && p.upvotesUsernames.find(u => u === user.username) ? 1 : 0

    const reduceProposalComments = commentsFilter => (total, p) => total + p && p.comments
        && Array.isArray(p.comments) ? p.comments.filter(commentsFilter).length : 0

    const publishedProposals = proposalsByUser.length
    const numberOfUpvotesGot = proposalsByUser.reduce(reduceProposalUpVotes, 0)
    const numberOfUpvotesGave = proposalsByOtherUsers.reduce(reduceProposalUpVotesGave, 0)
    const numberOfCommentsGot = proposalsByUser.reduce(reduceProposalComments(comment => comment.author.id !== user.id), 0)
    const numberOfCommentsGave = proposalsByOtherUsers.reduce(reduceProposalComments(comment => comment.author.id === user.id), 0)
    const numberOfProposalsWithLocation = proposalsByUser.filter(p => p.location.lat && p.location.long).length

    const categoryAchievements = Object.keys(achievementTypes.proposals.category)
        .filter(category => proposalsByUser.some(p => p.categoria === category))
        .map(category => achievementTypes.proposals.category[category])

    return [
        ...calculateAchievementsFromTypeAndNumber(achievementTypes.proposals.publishedProposals, publishedProposals),
        ...calculateAchievementsFromTypeAndNumber(achievementTypes.upvotes.got, numberOfUpvotesGot),
        ...calculateAchievementsFromTypeAndNumber(achievementTypes.upvotes.gave, numberOfUpvotesGave),
        ...calculateAchievementsFromTypeAndNumber(achievementTypes.comments.got, numberOfCommentsGot),
        ...calculateAchievementsFromTypeAndNumber(achievementTypes.comments.gave, numberOfCommentsGave),
        ...calculateAchievementsFromTypeAndNumber(achievementTypes.proposals.location, numberOfProposalsWithLocation),
        ...categoryAchievements
    ]
}

async function calculateUserAchievements(user) {
    const numberOfSharedProposals = user.sharedProposalIds && Array.isArray(user.sharedProposalIds) ?
        user.sharedProposalIds.length : 0

    const proposalsFavorited = user.favorites && Array.isArray(user.favorites) ? user.favorites.length : 0
    const userCompletedProfile = user => user.description && user.cpCode && user.realname && user.neighborhood && user.bdate && user.sex && user.image

    return [
        ...calculateAchievementsFromTypeAndNumber(achievementTypes.user.sharedProposals, numberOfSharedProposals),
        ...calculateAchievementsFromTypeAndNumber(achievementTypes.user.favoritedProposals, proposalsFavorited),
        ...user.image ? [achievementTypes.user.imageChanged] : [],
        ...userCompletedProfile(user) ? [achievementTypes.user.completedInformationProfile] : []
    ]
}

async function getCalculatedAchievements(username) {
    const user = await userModule.get({username})

    const proposalsAchievements = await calculateProposalsAchievements(user)
    const userAchievements = await calculateUserAchievements(user)

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