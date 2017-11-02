module.exports = {
    port: process.env.port || 3000,
    mongoDbUri: process.env.MONGODB_URI || 'mongodb://localhost:27017/agora',
    secretKey: process.env.AGORA_SECRET_KEY || '13n2kjrnaszdf$o3_32',
    jwtSecretKey: process.env.AGORA_JWT_SECRET_KEY || '2344uv23u5y5uu23y5v',
    enableMorgan: process.env.ENABLE_LOG_MORGAN || false,
    constants: {
        db: {
            signupCodes: 'signup_codes',
            users: 'users',
            tokens: 'tokens',
            proposals: 'proposals',
        }
    }
}