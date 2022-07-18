import axios from "axios";
import { AUTH_CONST_API_ORIGIN } from "./constants/auth.constants";

class TicketService {
    save(ticket, session) {
        let header = {
            method: 'POST',
            url: `${AUTH_CONST_API_ORIGIN}/ticket/create`,
            data: ticket,
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${session.jwt}`
            }
        }

        return axios(header);
    }

    update(ticket, session) {
        let header = {
            method: 'PUT',
            url: `${AUTH_CONST_API_ORIGIN}/ticket/update`,
            data: ticket,
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
            url: `${AUTH_CONST_API_ORIGIN}/ticket/list`,
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${session.jwt}`
            }
        }

        return axios(header);
    }

    delete(id, session) {
        let header = {
            method: 'DELETE',
            url: `${AUTH_CONST_API_ORIGIN}/ticket/delete?id=${id}`,
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${session.jwt}`
            }
        }

        return axios(header);
    }
}

export default new TicketService();