const db = require('./signup-codes.db')

function useSignupCode(invitationCode) {
    db.useSignupCode(invitationCode)
}

module.exports = {
    useSignupCode: useSignupCode,
    getSignupCode: db.getSignupCode
}