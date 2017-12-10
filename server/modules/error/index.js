function locationOutsideAllowedZone() {
    Error.captureStackTrace(this, this.constructor);

    this.name = this.constructor.name;
    this.message = 'Selected location outside of allowed zone.';
    this.status = 400;
    this.errorCode = 'E001';
}

module.exports = {
    locationOutsideAllowedZone: locationOutsideAllowedZone,
}