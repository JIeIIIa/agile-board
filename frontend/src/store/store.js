import Vue from 'vue'
import Vuex from 'vuex'
import user from './modules/user'
import auth from './modules/auth'
import card from './modules/card'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    user,
    auth,
    card
  },
  state: {},
  mutations: {},
  actions: {}
})
