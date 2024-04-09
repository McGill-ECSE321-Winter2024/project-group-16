import { createStore } from "vuex";

const LOGIN = "LOGIN";
const LOGIN_SUCCESS = "LOGIN_SUCCESS";
const LOGOUT = "LOGOUT";

export default createStore({

  state: {
    isLoggedIn: !!localStorage.getItem("loggedIn"),
    hideConfigButton: false,
    isPinned: false,
    showConfig: false,
    sidebarType: "bg-white",
    color: "bg-white",
    darkMode: false,
    isNavFixed: false,
    isAbsolute: false,
    showNavs: true,
    showSidenav: true,
    showNavbar: true,
    showFooter: true,
    showMain: true,
    layout: "default",
    userData: { id: -1 , name: "", email: "",role:"",password:"" },
  },
  mutations: {

    [LOGIN] (state) {
      state.pending = true;
    },
    [LOGIN_SUCCESS] (state) {
      state.isLoggedIn = true;
      state.pending = false;
    },
    [LOGOUT](state) {
      state.isLoggedIn = false;
    },


    setUserData(state, userData) {
      state.userData = userData;
    },

    toggleConfigurator(state) {
      state.showConfig = !state.showConfig;
    },
    sidebarMinimize(state) {
      let sidenav_show = document.querySelector("#app");
      if (state.isPinned) {
        sidenav_show.classList.add("g-sidenav-hidden");
        sidenav_show.classList.remove("g-sidenav-pinned");
        state.isPinned = false;
      } else {
        sidenav_show.classList.add("g-sidenav-pinned");
        sidenav_show.classList.remove("g-sidenav-hidden");
        state.isPinned = true;
      }
    },
    sidebarType(state, payload) {
      state.sidebarType = payload;
    },
    navbarFixed(state) {
      if (state.isNavFixed === false) {
        state.isNavFixed = true;
      } else {
        state.isNavFixed = false;
      }
    },
  },
  actions: {
    login({ commit }, creds) {
      commit(LOGIN); // show spinner
      return new Promise(resolve => {
        setTimeout(() => {
          localStorage.setItem("loggedIn", true);
          commit(LOGIN_SUCCESS);
          resolve();
        }, 1000);
      });
    },
    logout({ commit }) {
      localStorage.removeItem("loggedIn");
      commit(LOGOUT);
    },
    setUserData({ commit }, userData) {
      commit("setUserData", userData);
    },
    toggleSidebarColor({ commit }, payload) {
      commit("sidebarType", payload);
    },
    toggleShowSidenav({ commit }, payload) {
      commit("showSidenav", payload);
    },
  },
  getters: {
    isLoggedIn: state => {
      return state.isLoggedIn
     },
    userData(state) {
      return state.userData;
  }
},

});



