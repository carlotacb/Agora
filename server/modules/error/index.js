function locationOutsideAllowedZone() {
    Error.captureStackTrace(this, this.constructor);

    this.name = this.constructor.name;
    this.message = 'Selected location outside of allowed zone.';
    this.status = 400;
    this.errorCode = 'E001';
}

function proposalNotFound(id = '') {
    Error.captureStackTrace(this, this.constructor);

    this.name = this.constructor.name;
    this.message = `No proposal id found with id ${id}.`;
    this.status = 404;
    this.errorCode = 'E002';
}

function proposalOutsideZone(userZone, proposalZone) {
    Error.captureStackTrace(this, this.constructor);

    this.name = this.constructor.name;
    this.message = `You cannot access a proposal from another zone than ${userZone}. You're trying to access a proposal from zone ${proposalZone}.`;
    this.status = 403;
    this.errorCode = 'E003';
}

function invalidToken() {
    Error.captureStackTrace(this, this.constructor);

    this.name = this.constructor.name;
    this.message = 'Invalid Authorization token.';
    this.status = 403;
    this.errorCode = 'E004';
}

function missingToken() {
    Error.captureStackTrace(this, this.constructor);

    this.name = this.constructor.name;
    this.message = 'Missing Authorization token.';
    this.status = 403;
    this.errorCode = 'E005';
}

function invalidCredentials() {
    Error.captureStackTrace(this, this.constructor);

    this.name = this.constructor.name;
    this.message = 'Invalid username/password combination.';
    this.status = 403
    this.errorCode = 'E006';
}

module.exports = {
    locationOutsideAllowedZone: locationOutsideAllowedZone,
    proposalNotFound: proposalNotFound,
    proposalOutsideZone: proposalOutsideZone,
    invalidToken: invalidToken,
    missingToken: missingToken,
    invalidCredentials: invalidCredentials,
}