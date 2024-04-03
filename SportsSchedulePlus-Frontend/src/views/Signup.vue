<template>
  <div class="container top-0 position-sticky z-index-sticky">
    <div class="row">
      <div class="col-12">
        <Navbar isBtn="bg-gradient-light" />
      </div>
    </div>
  </div>
  <main class="main-content mt-0">
    <div class="page-header align-items-start min-vh-50 pt-5 pb-11 m-3 border-radius-lg"
      style="background-image: url('https://raw.githubusercontent.com/creativetimofficial/public-assets/master/argon-dashboard-pro/assets/img/signup-cover.jpg'); background-position: top;">
      <span class="mask bg-gradient-dark opacity-6"></span>
      <div class="container">
        <div class="row justify-content-center">
          <div class="col-lg-5 text-center mx-auto">
            <h1 class="text-white mb-2 mt-5">Welcome!</h1>
            <p class="text-lead text-white">Welcome to our site! Create an account to discover sports courses and register for them.</p>

          </div>
        </div>
      </div>
    </div>
    <div class="container">
      <div class="row mt-lg-n10 mt-md-n11 mt-n10 justify-content-center">
        <div class="col-xl-4 col-lg-5 col-md-7 mx-auto">
          <div class="card z-index-0">
            <div class="card-header text-center pt-4">
              <h5 class="card-title">Sign Up</h5>

            </div>
            <div class="card-body">
              <form role="form" @submit.prevent="signUp">
                <ArgonInput v-model="name" id="name" type="text" placeholder="Name" aria-label="Name" />
                <ArgonInput v-model="email" id="email" type="email" placeholder="Email" aria-label="Email" />
                <ArgonInput v-model="password" id="password" type="password" placeholder="Password"
                  aria-label="Password" />
                <ArgonCheckbox v-model="agreementChecked" id="agreement" />
                <label class="form-check-label" for="agreement">
                  I agree the
                  <a href="javascript:;" class="text-dark font-weight-bolder">Terms and Conditions</a>
                </label>
                <div class="text-center">
                  <ArgonButton fullWidth color="dark" variant="gradient"
                    class="my-4 mb-2" type="submit">Sign up</ArgonButton>
                </div>
                <div class="alert" role="alert" v-if="errorMessage">{{ errorMessage }}</div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </main>
  <AppFooter />
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';
import Navbar from '@/examples/PageLayout/Navbar.vue';
import ArgonInput from '@/components/ArgonInput.vue';
import ArgonCheckbox from '@/components/ArgonCheckbox.vue';
import ArgonButton from '@/components/ArgonButton.vue';

const name = ref('');
const email = ref('');
const password = ref('');
const agreementChecked = ref(false);
const errorMessage = ref('');
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()

const axiosClient = axios.create({
  baseURL: "http://localhost:8080"
});

const signUp = async () => {
  try {
    const response = await axiosClient.post('/authentication/signup', {
      name: name.value,
      email: email.value,
      password: password.value,
    });
    console.log('Signup successful:', response.data.message);
    router.push('/signin');
    // You can do something after successful signup, like redirecting the user to another page.
  } catch (error) {
    console.error('Signup failed:', error.response.data.errors);
    errorMessage.value = error.response.data.errors[0];
  }
};
</script>
