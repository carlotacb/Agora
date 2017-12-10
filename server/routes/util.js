const wrapAsyncRouterFunction = fn => (...args) => fn(...args).catch(args[2])

module.exports = {
    wrapAsyncRouterFunction: wrapAsyncRouterFunction
}