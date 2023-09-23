import { createStore } from 'vuex'

let key = "12306_MEMBER";

function getMemberFromStorage(){
  let item = window.sessionStorage.getItem(key);
  return item && typeof(item) !== 'undefined' && item !== 'undefined'? JSON.parse(item):{}
}

export default createStore({

  state: {
    member: getMemberFromStorage()
  },
  getters: {
  },
  mutations: {
      setMember(state,_member){
        window.sessionStorage.setItem(key,JSON.stringify(_member))
        state.member = _member;
      }
    },
  actions: {
  },
  modules: {
  }
})
