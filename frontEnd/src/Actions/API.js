import config from "../config";


const host = config.host
const ver = config.ver

const USER = `${host}/api/${ver}`;
const MESSAGE = `${host}/api/${ver}`;
const AUTH = `${USER}/auth`;

const API = {
    USER: {
        AUTH: {
            LOGIN: `${AUTH}/authenticate`,
            REGISTRATION: `${AUTH}/registration`,
            LOGOUT: `${AUTH}/logout`
        },

        DEMO: `${USER}/demo`,
        GET_INFO: `${USER}/getCurrentUser`,
        GET_ALL_USERS: `${USER}/getAllUsers`,
        SEARCH_USER:  `${USER}/search`
    },
    MESSAGE: {
        SendMessage: `${MESSAGE}/saveMessage`,
        GetAllMessages:`${MESSAGE}/receive-all-message`
    }
}

export default API;