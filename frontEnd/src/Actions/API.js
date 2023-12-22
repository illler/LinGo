import config from "../config";


const host = config.host
const ver = config.ver

const USER = `${host}/api/${ver}`;
const MESSAGE = `${host}/api/${ver}/message`;
const AUTH = `${USER}/auth`;
const Friends = `${host}/api/${ver}/friends`
const Files = `${host}/api/${ver}/files`
const API = {
    USER: {
        AUTH: {
            LOGIN: `${AUTH}/authenticate`,
            REGISTRATION: `${AUTH}/registration`,
            LOGOUT: `${AUTH}/logout`
        },

        DEMO: `${USER}/demo`,
        GET_INFO: `${USER}/user/getCurrentUser`,
        GET_PROFILE_INFO: `${USER}/user/getUserInfo`,
        GET_ALL_USERS: `${USER}/user/getAllUsers`,
        SEARCH_USER:  `${USER}/user/search`
    },
    MESSAGE: {
        SendMessage: `${MESSAGE}/saveMessage`,
        GetAllMessages:`${MESSAGE}/receive-all-message`,
        RecieveAllCorrespondence: `${MESSAGE}/receiving-all-correspondence`
    },
    Friends: {
        AddFriends: `${Friends}/addFriends`,
        RetrieveAllFriends: `${Friends}/retrieveAllFriends`,
        FriendsCheck: `${Friends}/friendsCheck`,
        RemoveFriend:  `${Friends}/deleteFriend`
    },

    Files: {
        UPLOAD_FILE: `${Files}/upload-file`,
        GET_FILE: `${Files}/get-file`
    }
}

export default API;