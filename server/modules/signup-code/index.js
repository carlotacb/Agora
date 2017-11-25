const db = require('./signup-codes.db')

function useSignupCode(invitationCode) {
    return db.useSignupCode(invitationCode)
}

module.exports = {
    useSignupCode: useSignupCode,
    getSignupCode: db.getSignupCode
}