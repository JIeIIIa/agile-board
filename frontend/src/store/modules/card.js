import {
  CARD_ADD,
  CARD_ADD_SUCCESS,
  CARD_DELETE,
  CARD_DELETE_SUCCESS,
  CARD_ERROR,
  CARD_LOAD_ALL_SUCCESS,
  CARD_REQUEST,
  CARD_UPDATE,
  CARD_UPDATE_SUCCESS
} from "../actions/card";
import axios from 'axios'
import {AUTH_LOGOUT} from "../actions/auth";


const state = {cards: [], status: ''}

const getters = {
  cardsList: state => state.cards
}

const actions = {
  [CARD_REQUEST]: ({commit, dispatch}) => {
    return new Promise((resolve, reject) => {
      console.log('CARD_REQUEST')
      commit(CARD_REQUEST)
      axios.get("/api/cards")
        .then(resp => {
          let cards = resp.data
          commit(CARD_LOAD_ALL_SUCCESS, cards)
          resolve()
        })
        .catch(err => {
          commit(CARD_ERROR)
          reject(err)
        })
    })
  },
  [CARD_ADD]: ({commit, dispatch}, card) => {
    return new Promise((resolve, reject) => {
      commit(CARD_ADD)
      let formData = new FormData()
      formData.append('text', card.text)
      formData.append('status', card.status)

      axios.post("/api/cards", formData)
        .then(resp => {
          commit(CARD_ADD_SUCCESS, resp.data)
          resolve()
        })
        .catch(err => {
          commit(CARD_ERROR)
          reject(err)
        })
    })
  },
  [CARD_UPDATE]: ({commit, dispatch}, card) => {
    return new Promise((resolve, reject) => {
      commit(CARD_UPDATE)
      let formData = new FormData()
      formData.append('id', card.id)
      formData.append('text', card.text)
      formData.append('status', card.status)

      axios.put("/api/cards/" + card.id, formData)
        .then(resp => {
          commit(CARD_UPDATE_SUCCESS, {id: card.id, card: resp.data})
          resolve()
        })
        .catch(err => {
          commit(CARD_ERROR)
          reject(err)
        })
    })
  },
  [CARD_DELETE]: ({commit, dispatch}, cardId) => {
    return new Promise((resolve, reject) => {
      commit(CARD_DELETE)
      axios.delete("/api/cards/" + cardId)
        .then(resp => {
          commit(CARD_DELETE_SUCCESS, cardId)
          resolve()
        })
        .catch(err => {
          commit(CARD_ERROR)
          reject(err)
        })
    })
  }
}

const mutations ={
  [AUTH_LOGOUT]: (state) => {
    state.status = 'logout'
    state.cards = []
  },
  [CARD_REQUEST]: (state) => {
    state.status = 'loading'
  },
  [CARD_ERROR]: (state) => {
    state.status = 'error'
  },
  [CARD_LOAD_ALL_SUCCESS]: (state, list) => {
    state.status = 'all success'
    state.cards = list
  },
  [CARD_ADD]: (state) => {
    state.status = 'loading'
  },
  [CARD_ADD_SUCCESS]: (state, card) => {
    state.status = 'add success'
    state.cards.push(card)
  },
  [CARD_UPDATE]: (state) => {
    state.status = 'updating'
  },
  [CARD_UPDATE_SUCCESS]: (state, payload) => {
    state.status = 'update success'
    const index = state.cards.findIndex(c => c.id === payload.id)
    state.cards.splice(index, 1, payload.card)
  },
  [CARD_DELETE]: (state) => {
    state.status = 'deleting'
  },
  [CARD_DELETE_SUCCESS]: (state, id) => {
    state.status = 'delete success'
    const index = state.cards.findIndex(c => c.id === id)
    state.cards.splice(index, 1)
  }
}

export default {
  state,
  getters,
  actions,
  mutations,
}

