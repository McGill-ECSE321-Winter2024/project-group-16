<script setup xmlns:color="http://www.w3.org/1999/xhtml">
import { computed, ref } from "vue";
import { useStore } from "vuex";
import axios from 'axios';
import { useRouter, useRoute } from 'vue-router'
import Breadcrumbs from "@/examples/Breadcrumbs.vue";

const showMenu = ref(false);
const store = useStore();
const route = useRoute();

const name = ref('');
const email = ref('');
const password = ref('');
const agreementChecked = ref(false);
const errorMessage = ref('');

const router = useRouter()

const axiosClient = axios.create({
  baseURL: "http://localhost:8080"
});

const signIn = async () => {
  try {
    const response = await axiosClient.post('/authentication/login', {
      name: name.value,
      email: email.value,
      password: password.value,
    });
    console.log('Signup successful:', response.data.message);
    router.push('/signin');
  } catch (error) {
    console.error('Signup failed:', error.response.data.errors);
    errorMessage.value = error.response.data.errors[0];
  }
};
const loggedIn = computed(() => localStorage.getItem("loggedIn"));
const isRTL = computed(() => store.state.isRTL);
const currentRouteName = computed(() => route.name);
const currentDirectory = computed(() => {
  let dir = route.path.split("/")[1];
  return dir.charAt(0).toUpperCase() + dir.slice(1);

});

const logout = () => store.dispatch('logout');
const minimizeSidebar = () => store.commit("sidebarMinimize");
const toggleConfigurator = () => store.commit("toggleConfigurator");

</script>

<template>
  <nav
    class="navbar navbar-main navbar-expand-lg px-0 mx-4 shadow-none border-radius-xl"
    :class="isRTL ? 'top-0 position-sticky z-index-sticky' : ''"
    v-bind="$attrs"
    id="navbarBlur"
    data-scroll="true"
  >
    <div class="px-3 py-1 container-fluid">
      <breadcrumbs
        :current-page="currentRouteName"
        :current-directory="currentDirectory"
      />

      <div
        class="mt-2 collapse navbar-collapse mt-sm-0 me-md-0 me-sm-4 w-100 rounded-lg shadow-sm bg-white"
        :class="isRTL ? 'px-0' : 'me-sm-4'"
        id="navbar"
      >
        <div
          class="pe-md-3 d-flex align-items-center"
          :class="isRTL ? 'me-md-auto' : 'ms-md-auto'"
        >
        </div>
        <ul class="navbar-nav justify-content-end">
          <li class="nav-item d-flex align-items-center">
            <router-link
              to="/signin"
              class="px-0 nav-link font-weight-bold text-black">

              <i class="fa fa-user" :class="isRTL ? 'ms-sm-2' : 'me-sm-2'">
              </i>
            <span v-if="isRTL" class="d-sm-inline d-none">يسجل دخول</span>
            <span v-if="loggedIn"  class="d-sm-inline d-none"  @click="logout">Log out</span>
            <span v-else class="d-sm-inline d-none text-black"  >Log In</span>

            </router-link>
          </li>

          <li class="nav-item d-xl-none ps-3 d-flex align-items-center">
            <a
              href="#"
              @click="minimizeSidebar"
              class="p-0 nav-link text-black"
              id="iconNavbarSidenav"
            >
              <div class="sidenav-toggler-inner">
                <i class="sidenav-toggler-line bg-black"></i>
                <i class="sidenav-toggler-line bg-black"></i>
                <i class="sidenav-toggler-line bg-black"></i>
              </div>
            </a>
          </li>
          <li class="px-3 nav-item d-flex align-items-center">
            <a class="p-0 nav-link text-black" @click="toggleConfigurator">
              <i class="cursor-pointer fa fa-cog fixed-plugin-button-nav"></i>
            </a>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>
<style>
.rounded-lg {
  border-radius: 10px;
}
.shadow-sm {
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}
.text-black {
  color: black;
}
</style>


