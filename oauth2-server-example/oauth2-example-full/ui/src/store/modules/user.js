import { oauth2Login, logout, getInfo, loginPhone, oauth2CodeLogin } from '@/api/login'
import { getToken, setToken, removeToken } from '@/utils/auth'
import { resetRouter } from '@/router'

const getDefaultState = () => {
  return {
    token: getToken(),
    name: '',
    avatar: ''
  }
}

const state = getDefaultState()

const mutations = {
  RESET_STATE: (state) => {
    Object.assign(state, getDefaultState())
  },
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  }
}

const actions = {
  // user login
  oauth2Login({ commit }, userInfo) {
    const { username, password, captchaKey, captchaCode, loginType, type, code } = userInfo
    console.log(userInfo)
    return new Promise((resolve, reject) => {
      var data = new FormData()
      if (loginType) data.append('grant_type', loginType)
      // grant_type = password
      if (username) data.append('username', username.trim())
      if (password) data.append('password', password.trim())
      // grant_type = social
      if (type) data.append('type', type.trim())
      if (code) data.append('code', code.trim())

      // captcha params
      var params = {}
      if (captchaKey) params.captchaKey = captchaKey
      if (captchaCode) params.captchaCode = captchaCode

      oauth2Login(data, params).then(response => {
        const { access_token } = response
        commit('SET_TOKEN', access_token)
        setToken(access_token)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  oauth2CodeLogin({ commit }, formData) {
    return new Promise((resolve, reject) => {
      oauth2CodeLogin(formData).then(response => {
        const { access_token } = response
        commit('SET_TOKEN', access_token)
        setToken(access_token)
        resolve()
      }).catch(error => {
        console.log(error)
        reject(error)
      })
    })
  },

  // get user info
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getInfo().then(response => {
        const { nickname, avatar } = response
        commit('SET_NAME', nickname || '匿名用户')
        commit('SET_AVATAR', avatar)
        resolve(response)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user logout
  logout({ commit }) {
    return new Promise((resolve, reject) => {
      removeToken()
      resetRouter()
      commit('RESET_STATE')
      resolve()
    })
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      removeToken() // must remove  token  first
      commit('RESET_STATE')
      resolve()
    })
  },

  // save token
  saveToken({ commit }, token) {
    return new Promise(resolve => {
      commit('SET_TOKEN', token)
      setToken(token)
      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

