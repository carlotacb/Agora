const db = require('./session.db')
const jwt = require('jsonwebtoken')
const config = require('../../config.js')


async function createProposal({username, title, body}) {


}

module.exports = {
    createProposal: createProposal
}