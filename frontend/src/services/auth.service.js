import router from '@/router';
import axios from 'axios'
import localforage from 'localforage';
import { AUTH_CONST_API_ORIGIN, AUTH_CONST_JWT_SESSION } from './constants/auth.constants';

class AuthService {
    // Realiza o login, passando como parâmetros o email e senha do usuário
    login(user) {
        let header = {
            method: 'POST',
            url: `${AUTH_CONST_API_ORIGIN}/auth/login`,
            data: user,
            headers: {
                'Content-Type': 'application/json'
            }
        }

        // Chamada da API axios que executa a requisição POST no endpoint de autenticação do servidor
        // Retorna uma promise para tratar o response fora da chamada de login
        return axios(header)
            .then(response => {
                if (response.headers.authorization.includes('Bearer')) {
                    let bearer = response.headers.authorization;
                    let data = response.config.data;

                    // Cria o objeto 'session' que armazenará o Bearer token e dados de login
                    let session = {
                        jwt: bearer.replace('Bearer ', ''),
                        data: data
                    }

                    localforage.setItem(AUTH_CONST_JWT_SESSION, session);

                    // Armazena em cache o token e dados de login
                    localStorage.setItem(AUTH_CONST_JWT_SESSION, session);

                    return session;
                }

                return null;
            });
    }

    // Retorna a sessão atual contendo informações do login e jwt
    currentSession() {
        return localStorage.getItem(AUTH_CONST_JWT_SESSION);
    }

    // Verifica se já possui uma sessão em cache
    loggedIn() {
        return localStorage.getItem(AUTH_CONST_JWT_SESSION) !== null;
    }

    // Realiza o logout, removendo a chave AUTH_CONST_JWT_SESSION do cache
    logout() {
        if (this.loggedIn()) {
            localforage.clear();
            localStorage.removeItem(AUTH_CONST_JWT_SESSION);
            router.go({ name: 'login' });
        }
    }
}

export default new AuthService();