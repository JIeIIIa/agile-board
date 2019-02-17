import {USER_ERROR, USER_REQUEST, USER_SUCCESS} from '../actions/user'
import Vue from 'vue'
import {AUTH_LOGOUT} from '../actions/auth'
import axios from 'axios'

const PROFILE_KEY = 'profile'
const state = {
  status: '',
  profile: JSON.parse(localStorage.getItem(PROFILE_KEY)) || {}
}

const getters = {
  getProfile: state => state.profile
}

const actions = {
  [USER_REQUEST]: ({commit, dispatch}) => {
    return new Promise((resolve, reject) => {
      commit(USER_REQUEST)
      axios.get('api/users/me')
        .then(resp => {
          let profile = {login: resp.data}
          commit(USER_SUCCESS, profile)
          resolve()
        })
        .catch(resp => {
          commit(USER_ERROR)
          // if resp is unauthorized, logout, to
          dispatch(AUTH_LOGOUT)
          reject(resp)
        })
    })
  },
}

const mutations = {
  [USER_REQUEST]: (state) => {
    state.status = 'loading'
    state.profile = {}
    localStorage.removeItem(PROFILE_KEY)
  },
  [USER_SUCCESS]: (state, profile) => {
    state.status = 'success'
    Vue.set(state, 'profile', profile)
    localStorage.setItem(PROFILE_KEY, JSON.stringify(profile))
  },
  [USER_ERROR]: (state) => {
    state.status = 'error'
  },
  [AUTH_LOGOUT]: (state) => {
    state.profile = {}
    localStorage.removeItem(PROFILE_KEY)
  }
}

export default {
  state,
  getters,
  actions,
  mutations,
}
