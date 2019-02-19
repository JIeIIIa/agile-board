import {AUTH_AUTO_LOGIN, AUTH_ERROR, AUTH_LOGOUT, AUTH_NEW_ACCOUNT, AUTH_REQUEST, AUTH_SUCCESS} from '../actions/auth'
import {USER_REQUEST} from '../actions/user'
import axios from 'axios'

const state = {token: localStorage.getItem('user-token') || '', status: '', hasLoadedOnce: false}

const getters = {
  isAuthenticated: state => !!state.token,
  authStatus: state => state.status,
}

const actions = {
  [AUTH_AUTO_LOGIN]: ({commit, dispatch}) => {
    return new Promise((resolve, reject) => {
      if (getters.isAuthenticated) {
        axios.defaults.headers.common['authorization'] = state.token
        resolve()
      } else {
        commit(AUTH_LOGOUT)
        reject()
      }
    })
  },
  [AUTH_REQUEST]: ({commit, dispatch}, user) => {
    return new Promise((resolve, reject) => {
      commit(AUTH_REQUEST)
      let data = new FormData();
      data.append('login', user['login'])
      data.append('password', user['password'])
      axios.post('/api/login', data)
        .then(resp => {
          let token = resp.data.token;
          localStorage.setItem('user-token', token)
          // Here set the header of your ajax library to the token value.
          // example with axios
          commit(AUTH_SUCCESS, {token: token})
          dispatch(USER_REQUEST)
          resolve(resp)
        })
        .catch(err => {
          commit(AUTH_ERROR, err)
          localStorage.removeItem('user-token')
          reject(err)
        })
    })
  },
  [AUTH_NEW_ACCOUNT]: ({commit, dispatch}, account) => {
    return new Promise((resolve, reject) => {
      let data = new FormData();
      data.append('login', account['login']);
      data.append('password', account['password']);
      data.append('passwordConfirmation', account['passwordConfirmation']);
      axios.post('/api/registration', data)
        .then(resp => {
          let token = resp.data.token
          commit(AUTH_SUCCESS, {token: token})
          resolve()
        })
        .catch(err => {
          reject(err)
        })
    })
  },
  [AUTH_LOGOUT]: ({commit, dispatch}) => {
    return new Promise((resolve, reject) => {
      axios.defaults.headers.common['authorization'] = ''
      commit(AUTH_LOGOUT)
      localStorage.removeItem('user-token')
      resolve()
    })
  }
}

const mutations = {
  [AUTH_REQUEST]: (state) => {
    state.status = 'loading'
  },
  [AUTH_SUCCESS]: (state, resp) => {
    state.status = 'success'
    state.token = resp.token
    state.hasLoadedOnce = true
    axios.defaults.headers.common['authorization'] = 'Bearer ' + state.token
  },
  [AUTH_ERROR]: (state) => {
    state.status = 'error'
    state.hasLoadedOnce = true
  },
  [AUTH_LOGOUT]: (state) => {
    state.token = ''
    axios.defaults.headers.common['authorization'] = ''
  }
}

export default {
  state,
  getters,
  actions,
  mutations,
}
