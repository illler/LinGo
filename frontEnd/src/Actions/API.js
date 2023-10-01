import config from "../config";


const host = config.host
const ver = config.ver

const USER = `${host}/api/${ver}`;
const AUTH = `${USER}/auth`;

const API = {
    USER: {
        AUTH: {
            LOGIN: `${AUTH}/authenticate`,
            REGISTRATION: `${AUTH}/registration`,
            LOGOUT: `${AUTH}/logout`
        },

        DEMO: `${USER}/demo`,
    }
}

export default API;