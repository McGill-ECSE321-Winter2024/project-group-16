<template>
  <div class="collapse navbar-collapse w-auto h-auto h-100" id="sidenav-collapse-main">
    <ul class="navbar-nav">

      <li class="nav-item">
        <sidenav-item
          to="/dashboard-default"
          :class="getRoute() === 'dashboard-default' ? 'active' : ''"
          navText="Dashboard"
        >
          <template v-slot:icon>
            <i class="ni ni-tv-2 text-primary text-sm opacity-10"></i>
          </template>
        </sidenav-item>
      </li>

      <li class="nav-item">
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
      <li class="nav-item">
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
      <li class="nav-item">
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
      <li class="nav-item">
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


      <li class="mt-3 nav-item">
        <h6
          class="text-xs ps-4 text-uppercase font-weight-bolder opacity-6 ms-2"
        >
          ACCOUNT PAGES
        </h6>
      </li>

      <li class="nav-item" v-if="!!loggedIn">
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

      <li class="nav-item" v-if="!!loggedIn" @click="logout">
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


      <li class="nav-item" v-if="!loggedIn">
        <sidenav-item
          to="/signin"
          :class="getRoute() === 'signIn' ? 'active' : ''"
          navText="Sign In"
        >
          <template v-slot:icon>
            <i class="ni ni-single-copy-04 text-danger text-sm opacity-10"></i>
          </template>
        </sidenav-item>
      </li>

      <li class="nav-item" v-if="!loggedIn">
        <sidenav-item
          to="/signup"
          :class="getRoute() === 'signup' ? 'active' : ''"
          navText="Sign Up"
        >
          <template v-slot:icon>
            <i class="ni ni-collection text-info text-sm opacity-10"></i>
          </template>
        </sidenav-item>
      </li>
    </ul>
  </div>
</template>
<script setup>
import {ref, computed} from "vue";
import {useRoute} from "vue-router";
import {useStore} from "vuex";
import SidenavItem from "./SidenavItem.vue";

const store = useStore();

const getRoute = () => {
  const route = useRoute();
  const routeArr = route.path.split("/");
  return routeArr[1];
};

const loggedIn = computed(() => localStorage.getItem("loggedIn"));
const logout = () => store.dispatch('logout');

</script>
