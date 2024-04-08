<script setup>
import { onBeforeMount, onMounted, onBeforeUnmount } from "vue";
import { useStore } from "vuex";
import axios from 'axios';

import setTooltip from "@/assets/js/tooltip.js";
import ProfileCard from "./ProfileCard.vue";
import ArgonButton from "@/argon_components/ArgonButton.vue";
import { ref } from 'vue';
import imagePath from '../assets/importedpng/profile_page_top.png';

const body = document.getElementsByTagName("body")[0];
const store = useStore();


onBeforeMount(() => {
  store.state.imageLayout = "profile-overview";
  store.state.showNavbar = false;
  store.state.showFooter = true;
  store.state.hideConfigButton = true;
  body.classList.add("profile-overview");
});
onBeforeUnmount(() => {
  store.state.isAbsolute = false;
  store.state.imageLayout = "default";
  store.state.showNavbar = true;
  store.state.showFooter = true;
  store.state.hideConfigButton = false;
  body.classList.remove("profile-overview");
});


onMounted(() => {
  store.state.isAbsolute = true;
  setTooltip();

});

const userData = JSON.parse(localStorage.getItem("userData"));
let email = ref('');
email.value = userData.email;
let name = ref('');
name.value =  userData.name;
let password = ref('');
let userID = userData.id;

password.value = userData.password

const axiosClient = axios.create({
  baseURL: "http://localhost:8080"
});

const successMessage = ref('');
const errorMessage = ref('');

const updateUserProfile = async () => {

  try {
    var endpoint;
    if (userData.role != "Owner"){
      endpoint = `/customers/${userID}`;
    }
    else{
      endpoint = "/owner";
    }
    const response = await axiosClient.put(endpoint, {
      name: name.value,
      email: email.value,
      password: password.value,
    });

    console.log("Update");
    successMessage.value = 'Profile updated successfully !';
    errorMessage.value = ''; // Clear error message if any
  } catch (error) {
    console.error('Error updating profile:', error.response.data.errors[0]);
    successMessage.value = ''; // Clear success message if any
    errorMessage.value =  error.response.data.errors[0];
  }
};




</script>
<template>
  <main>
    <div
        class="page-header min-height-300"
        :style="{
          backgroundImage: 'url(' + imagePath + ')',
          marginRight: '-24px',
          marginLeft: '-34%'
        }"
      >
      </div>
    <div class="container">

      <div class="card shadow-lg mt-n6">
        <div class="card-body p-3">
          <div class="row gx-4">
            <div class="col-auto">
              <div class="avatar">
                <img
                  src="../assets/importedpng/user.png"
                  alt="profile_image"
                />
              </div>
            </div>
            <div class="col-auto my-auto">
              <div class="h-100">
                <h5 class="mb-1">{{ userData.role }}</h5>
                <p class="mb-0 font-weight-bold text-sm">{{userData.name }}</p>
              </div>
            </div>
            <div
              class="mx-auto mt-3 col-lg-4 col-md-6 my-sm-auto ms-sm-auto me-sm-0"
            >
              <div class="nav-wrapper position-relative end-0">
                <ul
                  class="p-1 bg-transparent nav nav-pills nav-fill"
                  role="tablist"
                >
                  <li v-if="userData.role === 'Instructor' || userData.role === 'Customer'" class="nav-item">

                      <svg
                        class="text-dark"
                        width="16px"
                        height="16px"
                        viewBox="0 0 40 44"
                        version="1.1"
                        xmlns="http://www.w3.org/2000/svg"
                        xmlns:xlink="http://www.w3.org/1999/xlink"
                      >
                        <title>document</title>
                        <g
                          stroke="none"
                          stroke-width="1"
                          fill="none"
                          fill-rule="evenodd"
                        >
                          <g
                            transform="translate(-1870.000000, -591.000000)"
                            fill="#FFFFFF"
                            fill-rule="nonzero"
                          >
                            <g transform="translate(1716.000000, 291.000000)">
                              <g transform="translate(154.000000, 300.000000)">
                                <path
                                  class="color-background"
                                  d="M40,40 L36.3636364,40 L36.3636364,3.63636364 L5.45454545,3.63636364 L5.45454545,0 L38.1818182,0 C39.1854545,0 40,0.814545455 40,1.81818182 L40,40 Z"
                                  opacity="0.603585379"
                                />
                                <path
                                  class="color-background"
                                  d="M30.9090909,7.27272727 L1.81818182,7.27272727 C0.814545455,7.27272727 0,8.08727273 0,9.09090909 L0,41.8181818 C0,42.8218182 0.814545455,43.6363636 1.81818182,43.6363636 L30.9090909,43.6363636 C31.9127273,43.6363636 32.7272727,42.8218182 32.7272727,41.8181818 L32.7272727,9.09090909 C32.7272727,8.08727273 31.9127273,7.27272727 30.9090909,7.27272727 Z M18.1818182,34.5454545 L7.27272727,34.5454545 L7.27272727,30.9090909 L18.1818182,30.9090909 L18.1818182,34.5454545 Z M25.4545455,27.2727273 L7.27272727,27.2727273 L7.27272727,23.6363636 L25.4545455,23.6363636 L25.4545455,27.2727273 Z M25.4545455,20 L7.27272727,20 L7.27272727,16.3636364 L25.4545455,16.3636364 L25.4545455,20 Z"
                                />
                              </g>
                            </g>
                          </g>
                        </g>
                      </svg>
                      <router-link  to="/customer/registrations"> <span class="ms-1">Scheduled courses</span></router-link>

                  </li>

                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="py-4 container">
      <div class="row">
        <div class="col">
          <profile-card class="h-100"/>
        </div>
        <div class="col">
          <div class="card row h-100">
            <div class="card-header pb-1">
              <div class="d-flex align-items-center">
                <p class="mb-0">Edit Profile</p>
                <argon-button color="success" size="lg" class="ms-auto" @click="updateUserProfile"
                  >Update</argon-button
                >
              </div>
            </div>
            <div class="card-body">

              <p class="text-uppercase text-sm">User Information</p>
              <div class="mb-4">
                <label for="name" class="form-label fs-6">Name</label>
                <div class="input-group">
                  <input id="name" class="form-control form-control-lg" type="text" v-model="name" placeholder="Enter your name">
                  <span class="input-group-text"><i class="fas fa-user"></i></span>
                </div>
              </div>
              <div class="mb-4">
                <label for="email" class="form-label fs-6">Email address</label>
                <div class="input-group">
                  <input id="email" class="form-control form-control-lg" type="email" v-model="email" placeholder="Enter your email">
                  <span class="input-group-text"><i class="fas fa-envelope"></i></span>
                </div>
              </div>
              <div class="mb-4">
                <label for="password" class="form-label fs-6">Password</label>
                <div class="input-group">
                  <input id="password" class="form-control form-control-lg" type="password" v-model="password" placeholder="Enter your password">
                  <span class="input-group-text"><i class="fas fa-lock"></i></span>
                </div>
              </div>
              <div class="row">
                <div class="col-md-12">
                  <div class="alert alert-success text-white fs-6" v-if="successMessage">{{ successMessage }}</div>
                  <div class="alert alert-danger text-white fs-6" v-if="errorMessage">{{ errorMessage }}</div>
                </div>
              </div>
              <hr class="horizontal dark" />
            </div>
          </div>
        </div>

      </div>
    </div>
  </main>
</template>
