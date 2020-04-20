import Vue from 'Vue'
import Vuex from 'vuex'
import axios from './axios-auth';

Vue.use(Vuex);

export default new Vuex.Store({
    state: {
        jwt: null,
        login: null
    },
    mutations: {
        authUser (state, userData) {
            state.jwt = userData.jwt
            state.login = userData.userName
        }
    },
    actions: {
        login({commit}, authData) {
            axios.post('/user/authenticate', {
                login: authData.login,
                password: authData.password
            }).then(resp => {
                commit('authUser', {
                    jwt: resp.data.jwt,
                    userName: resp.data.login
                })
            }).catch( error => {alert(error.response.data.message)});
        }
    },
    getters: {
        isAuthenticated(state) {
            return state.jwt !== null;
        }
    }
})