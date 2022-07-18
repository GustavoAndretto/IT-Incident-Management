import axios from "axios";
import { AUTH_CONST_API_ORIGIN } from "./constants/auth.constants";

class UserService {
    load(session) {
        let data = JSON.parse(session.data);

        let header = {
            method: 'GET',
            url: `${AUTH_CONST_API_ORIGIN}/user/find?email=${data.email}`,
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${session.jwt}`
            }
        }

        return axios(header);
    }

    save(user, session) {
        let header = {
            method: 'POST',
            url: `${AUTH_CONST_API_ORIGIN}/user/create`,
            data: user,
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${session.jwt}`
            }
        }

        return axios(header);
    }

    list(session) {
        let header = {
            method: 'GET',
            url: `${AUTH_CONST_API_ORIGIN}/user/list`,
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${session.jwt}`
            }
        }

        return axios(header);
    }

    update(user, session) {
        let header = {
            method: 'PUT',
            url: `${AUTH_CONST_API_ORIGIN}/user/update`,
            data: user,
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${session.jwt}`
            }
        }

        return axios(header);
    }

    delete(email, session) {
        let header = {
            method: 'DELETE',
            url: `${AUTH_CONST_API_ORIGIN}/user/delete?email=${email}`,
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${session.jwt}`
            }
        }

        return axios(header);
    }
}

export default new UserService();