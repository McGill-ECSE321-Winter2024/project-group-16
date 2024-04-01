import { createStore } from "vuex";
// Define a custom Vuex plugin to persist specific state properties
const customPersistedState = store => {
  // Subscribe to Vuex store mutations
  store.subscribe((mutation, state) => {
    // Define an array of state properties to persist
    const persistedProperties = ["useremail", "userData", "loggedIn"];
    
    // Check if the mutated property is in the array of persisted properties
    if (persistedProperties.includes(mutation.type)) {
      // Retrieve the value of the mutated property
      const value = state[mutation.type];
      
      // Persist the property to local storage
      localStorage.setItem(mutation.type, JSON.stringify(value));
    }
  });
};



export default createStore({
 
  state: {
    hideConfigButton: false,
    isPinned: false,
    showConfig: false,
    sidebarType: "bg-white",
    isRTL: false,
    mcolor: "",
    darkMode: false,
    isNavFixed: false,
    isAbsolute: false,
    showNavs: true,
    showSidenav: true,
    showNavbar: true,
    showFooter: true,
    showMain: true,
    layout: "default",
    useremail:"",
    loggedIn:false,
    userData: { id: -1 , name: "", email: "",role:"",password:"" },
  },
  mutations: {

    setUserEmail(state, email) {
      state.useremail = email;
    },
    setUserData(state, userData) {
      state.userData = userData;
    },
    setLoggedIn(state, isLoggedIn) {
      state.loggedIn = isLoggedIn;
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
    setUserEmail({ commit }, email) {
      commit("setUserEmail", email);
    },
    setUserData({ commit }, userData) {
      commit("setUserData", userData);
    },
    setLoggedIn({ commit }, loggedIn) {
      commit("setLoggedIn", loggedIn);
    },
    toggleSidebarColor({ commit }, payload) {
      commit("sidebarType", payload);
    },
    toggleShowSidenav({ commit }, payload) {
      commit("showSidenav", payload);
    },
  },
  getters: {
    isLoggedIn(state) {
        return state.loggedIn;
    },
    userData(state) {
      return state.userData;
  }
},
plugins: [customPersistedState]

});



