<script setup>

import {useStore} from "vuex";
import SidenavItem from "./SidenavItem.vue";

import {ref, onMounted} from "vue";

import {useRoute, useRouter} from 'vue-router'

const router = useRouter()

const getRoute = () => {
  const route = useRoute();
  const routeArr = route.path.split("/");
  return routeArr[1];
};

const store = useStore();
const isLoggedIn = ref(localStorage.getItem('loggedIn') || undefined)

const userData = JSON.parse(localStorage.getItem("userData"));

const isOwner = isLoggedIn.value && userData != null && userData.role === "Owner";
const logout = () => {
  localStorage.setItem('loggedIn', false);
  store.dispatch('logout');
  setTimeout(() => {
      router.go('/signin');
    }) 
    router.push('/signin');
};
const sidebarColor = (color ="warning") => {
  document.querySelector("#sidenav-main").setAttribute("data-color", color);
};

onMounted(() => {
  sidebarColor();
});
</script>

<template>
  <div class="collapse navbar-collapse w-auto h-auto h-100" id="sidenav-collapse-main">
    <ul class="navbar-nav">

      <li class="nav-item">
        <sidenav-item
          to="/dashboard-default"
          :class="getRoute() === 'dashboard-default' ? 'active' : ''"
          navText="Dashboard"
          @click="sidebarColor()"

        >
          <template v-slot:icon>
            <i class="ni ni-tv-2 text-primary text-sm opacity-10"></i>
          </template>
        </sidenav-item>
      </li>
      <li class="nav-item">
        <sidenav-item
          to="/classes"
          :class="getRoute() === 'classes' ? 'active' : ''"
          navText="Classes"
          @click="sidebarColor()"

        >
          <template v-slot:icon>
            <i class="ni ni-tv-2 text-primary text-sm opacity-10"></i>
          </template>
        </sidenav-item>
      </li>
      <li class="nav-item">
        <sidenav-item
          to="/schedule"
          :class="getRoute() === 'schedule' ? 'active' : ''"
          navText="Schedule"
          @click="sidebarColor()"

        >
          <template v-slot:icon>
            <i class="ni ni-tv-2 text-primary text-sm opacity-10"></i>
          </template>
        </sidenav-item>
      </li>
      <li class="nav-item" v-if="!!isOwner">
        <sidenav-item
          to="/customers"
          :class="getRoute() === 'customers' ? 'active' : ''"
          navText="Customers"
        >
          <template v-slot:icon>
            <i class="ni ni-single-02 text-warning text-sm opacity-300"></i>
          </template>
        </sidenav-item>
      </li>
      <li class="nav-item" v-if="!!isOwner">
        <sidenav-item
          to="/instructors"
          :class="getRoute() === 'instructors' ? 'active' : ''"
          navText="Instructors"
        >
          <template v-slot:icon>
            <i class="ni ni-single-02 text-info text-sm opacity-300"></i>
          </template>
        </sidenav-item>
      </li>
      <li class="nav-item" v-if="!!isOwner">
        <sidenav-item
          to="/courseTypes"
          :class="getRoute() === 'courseTypes' ? 'active' : ''"
          navText="Course types"
        >

          <template v-slot:icon>
            <i
              class="ni ni-collection text-warning text-sm opacity-10"
            ></i>
          </template>
        </sidenav-item>
      </li>
      <li class="nav-item" v-if="!!isOwner">
        <sidenav-item
          to="/scheduledCourses"
          :class="getRoute() === 'scheduledCourses' ? 'active' : ''"
          navText="Scheduled courses"
        >

          <template v-slot:icon>
            <i
              class="ni ni-calendar-grid-58 text-warning text-sm opacity-10"
            ></i>
          </template>
        </sidenav-item>
      </li>
      <li class="nav-item" v-if="isLoggedIn">
        <sidenav-item
          to="/profile"
          :class="getRoute() === 'profile' ? 'active' : ''"
          navText="Profile"
        >
          <template v-slot:icon>
            <i class="ni ni-single-02 text-dark text-sm opacity-10"></i>
          </template>
        </sidenav-item>
      </li>
      <li class="nav-item" v-if="!!isLoggedIn" @click="logout">
        <sidenav-item
          to="/signin"
          :class="getRoute() === 'signin' ? 'active' : ''"
          navText="Logout"
        >
          <template v-slot:icon>
            <i class="fa fa-sign-out text-dark text-sm opacity-1000"></i>
          </template>
        </sidenav-item>
      </li>


    </ul>
  </div>
</template>