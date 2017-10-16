module.exports = {
    port: process.env.port || 3000,
    mongoDbUri: process.env.MONGODB_URI || 'mongodb://localhost:27017/agora',

    secretKey: process.env.AGORA_SECRET_KEY || '13n2kjrnaszdf$o3_32',

    constants: {

        db: {
            signupCodes: 'signup_codes',
            users: 'users'
        }

    }
}