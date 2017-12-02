const zones = [
    {
        id: 0,
        name: 'Ciutat Vella',
    },
    {
        id: 1,
        name: 'Eixample',
    },
    {
        id: 2,
        name: 'Sants-Montjuic',
    },
    {
        id: 3,
        name: 'Les Corts',
    },
    {
        id: 4,
        name: 'Sarrià-Sant Gervasi',
    },
    {
        id: 5,
        name: 'Gracia',
    },
    {
        id: 6,
        name: 'Horta-Guinardó',
    },
    {
        id: 7,
        name: 'Nou Barris',
    },
    {
        id: 8,
        name: 'San Andreu',
    },
    {
        id: 9,
        name: 'San Martí',
    }
]

function getZoneForSignupCode(signupCode) {
    const zoneId = parseInt(signupCode[0])
    const zone = zones.find(zone => zone.id === zoneId)
    if (!zone) {
        throw new Error(`Zone ${zoneId} not found for signup code ${signupCode}`)
    }
    return zone
}


module.exports = {
    getZoneForSignupCode: getZoneForSignupCode,
    zones: zones
}