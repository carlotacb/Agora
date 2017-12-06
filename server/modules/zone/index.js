const inside = require('point-in-polygon')

const loadPolygon = id => require(`./geometries/${parseInt(id)}.json`).geometries[0].coordinates[0][0]

const zones = [
    {
        id: 0,
        name: 'Ciutat Vella',
        polygon: null,
    },
    {
        id: 1,
        name: 'Eixample',
        polygon: null,
    },
    {
        id: 2,
        name: 'Sants-Montjuic',
        polygon: null,
    },
    {
        id: 3,
        name: 'Les Corts',
        polygon: null,
    },
    {
        id: 4,
        name: 'Sarrià-Sant Gervasi',
        polygon: null,
    },
    {
        id: 5,
        name: 'Gràcia',
        polygon: null,
    },
    {
        id: 6,
        name: 'Horta-Guinardó',
        polygon: null,
    },
    {
        id: 7,
        name: 'Nou Barris',
        polygon: null,
    },
    {
        id: 8,
        name: 'Sant Andreu',
        polygon: null,
    },
    {
        id: 9,
        name: 'Sant Martí',
        polygon: null,
    }
].map(zone => {
    zone.polygon = loadPolygon(zone.id)
    return zone
})

const get = id => zones.find(zone => zone.id === id)

function getZoneForSignupCode(signupCode) {
    const zoneId = parseInt(signupCode[0])
    const zone = get(zoneId)
    if (!zone) {
        throw new Error(`Zone ${zoneId} not found for signup code ${signupCode}`)
    }
    return zone
}

async function throwIfInvalidLocationForZone({zoneId, location}) {
    if (!location || !location.lat || !location.long) {
        return
    }

    const zone = get(parseInt(zoneId))
    const point = [location.long, location.lat]
    const pointIsInsideZone = inside(point, zone.polygon)

    if (!pointIsInsideZone) {
        throw new Error(`Point ${JSON.stringify(location)} is not in zone ${zone.name}`)
    }
}

module.exports = {
    getZoneForSignupCode: getZoneForSignupCode,
    throwIfInvalidLocationForZone: throwIfInvalidLocationForZone,
    zones: zones
}