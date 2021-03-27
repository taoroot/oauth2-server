import { login, logout, getInfo, loginPhone } from '@/api/login'
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
  login({ commit }, userInfo) {
    const { username, password, imageKey, code, loginType, phone } = userInfo
    return new Promise((resolve, reject) => {
      console.log(loginType)
      // 手机号登录
      if (loginType) {
        var data = new FormData()
        data.append('username', username.trim())
        data.append('password', password)
        data.append('grant_type', 'password')
        var params = {
          imageKey: imageKey,
          imageCode: code
        }
        login(data, params).then(response => {
          console.log(response)
          const { access_token, refresh_token } = response
          commit('SET_TOKEN', access_token)
          setToken(access_token)
          resolve()
        }).catch(error => {
          reject(error)
        })
      } else { // 手机号登录
        loginPhone({ phone: phone.trim(), smsCode: code }).then(response => {
          const { data } = response
          commit('SET_TOKEN', data)
          setToken(data)
          resolve()
        }).catch(error => {
          reject(error)
        })
      }
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
      logout().then(() => {
        removeToken() // must remove  token  first
        resetRouter()
        commit('RESET_STATE')
        resolve()
      }).catch(error => {
        reject(error)
      })
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

