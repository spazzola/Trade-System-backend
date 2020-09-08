import Vue from 'Vue'
import Vuex from 'vuex'
import axios from './axios-auth'

Vue.use(Vuex);

export default new Vuex.Store({
    state: {
        jwt: null,
        login: null,
        buyers: [],
        suppliers: [],
        isAddingOrder: false
    },
    mutations: {
        authUser (state, userData) {
            state.jwt = userData.jwt
            state.login = userData.userName
        },
        getAllBuyers(state) {
            state.buyers = []
            return new Promise((rosolve, reject) => {
                return axios.get("/buyer/getAllWithBalances", {
                    headers: {
                          'Authorization': 'Bearer ' + state.jwt
                        }
                  }).then(resp => {
                    let data = resp.data;
                    for (let key in data) {
                      const buyer = data[key];
                      buyer.id = buyer.id;
                      state.buyers.push(buyer);
                    } rosolve(resp)
                  })
                })
            },
            getAllSuppliers(state) {
            state.suppliers = []
            return new Promise((rosolve, reject) => {
                return axios.get("/supplier/getAllWithBalances", {
                    headers: {
                          'Authorization': 'Bearer ' + state.jwt
                        }
                  }).then(resp => {
                    let data = resp.data;
                    for (let key in data) {
                      const supplier = data[key];
                      supplier.id = supplier.id;
                      state.suppliers.push(supplier);
                    } rosolve(resp)
                  })
                })
            }
        },
    actions: {
        login({commit}, authData) {
            return new Promise((resolve, reject) => {
            axios.post('/user/authenticate', {
                login: authData.login,
                password: authData.password
            }).then(resp => {
                commit('authUser', {
                    jwt: resp.data.jwt,
                    userName: resp.data.login
                }), resolve(resp)
            }).catch(error => {
                if (error.response.status === 403) {
                    alert('Nieprawidłowy login!')
                    return
                }
                alert(error.response.data.message)
            })
        })
        }
    },
    getters: {
        isAuthenticated(state) {
            return state.jwt !== null;
        },
    }
})