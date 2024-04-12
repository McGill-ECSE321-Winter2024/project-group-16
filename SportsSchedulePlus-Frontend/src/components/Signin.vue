<template>
  <main class="main-content mt-0">
    <div class="page-header align-items-start min-vh-45 pt-5 ml-3 mr-3 border-radius-lg text-center"
         :style="{backgroundImage: 'url(' + imagePath + ')', backgroundPosition: 'top'}">
    </div>
    <div class="container">
      <div class="row mt-lg-n10 mt-md-n11 mt-n10 justify-content-center">
        <div class="col-xl-4 col-lg-5 col-md-7 mx-auto">
          <div class="card shadow">
            <div class="z-index-0">
              <div class="card-headers text-center pt-4">
                <h5 class="card-title">Log In</h5>
              </div>
            </div>
            <v-form v-model="form" @submit.prevent="signIn">
              <div class="card-body">


                <v-text-field
                  v-model="email"
                  id="Email"
                  color="#E2725B"
                  label="Email"
                  variant="underlined"
                  aria-label="Email"
                  placeholder="johnsmith@gmail.com"
                  :rules="[rules.required]"
                ></v-text-field>

                <div style="display: flex; align-items: center; position: relative;">
                <v-text-field
                  v-model="password"
                  color="#E2725B"
                  :type="passwordFieldType"
                  label="Password"
                  aria-label="Email"
                  variant="underlined"
                  placeholder="Enter your password"
                  :rules="[rules.required]"
                  :hint="passwordHints"

                ></v-text-field>
                <v-icon @click="toggleVisibility" style="margin-left: 10px; color: #E2725B; position: relative;">
                  {{ showPassword ? 'mdi-eye' : 'mdi-eye-off' }}
                </v-icon>
                </div>

                <v-divider></v-divider>

                <div class="card-body">
                  <v-btn
                    :disabled="!form"
                    :loading="loading"
                    color="#E2725B"
                    size="large"
                    type="submit"
                    variant="elevated"
                    block
                  >
                    Sign In
                  </v-btn>
                  <v-divider></v-divider>
                  <p style="text-align: center; font-style: italic;">Don't have an account?</p>
                  <v-btn
                    color="#E2725B"
                    size="large"
                    type="Create account"
                    variant="elevated"
                    @click="redirectToSignIn"
                    block
                  >
                    Register
                  </v-btn>
                </div>

                <div class="alert" role="alert" v-if="errorMessage">
                  <v-alert
                    color="error"
                    variant="outlined"
                    :text="errorMessage"
                  >
                  </v-alert>
                </div>

              </div>
            </v-form>
          </div>
        </div>
      </div>
    </div>

  </main>
</template>

<script setup>
import {computed, ref} from 'vue';
import axios from 'axios';
import {useRouter} from 'vue-router'
import image from '../assets/importedpng/signin_and_signup_top.png';
import {useStore} from "vuex";

const store = useStore();


const email = ref('');
const password = ref('');
const showPassword = ref(false);
const imagePath = image;
const loading = ref(false);
const form = ref(false)
const errorMessage = ref('');
const rules = {
  required: value => !!value || 'Field is required',
};

const toggleVisibility = () => {
  showPassword.value = !showPassword.value;
};
const router = useRouter()

const axiosClient = axios.create({
  baseURL: "http://localhost:8080"
});

const signIn = async () => {
  userRole = "Customer";
  try {
    const user = { email: email.value, password: password.value };
    await axiosClient.post('/authentication/login',user);
    console.log("pwd",password);

    const response = await axiosClient.get(`/customers/email/${email.value}`);

    try {
    let instructorResponse = await axiosClient.get(`/instructors/${email.value}`);
    // Handle the response here
    console.log("Instructor found: ",instructorResponse.data);
    userRole = "Instructor";

    } catch (error) {
        // Handle any errors that occur during the request
        console.error('Error fetching instructor data:', error);
    }


    var userRole;
    if(email.value === "sports.schedule.plus@gmail.com"){
       userRole = "Owner";
    }

    // Assign response data to userData variable
    var userData = {
      id: response.data.id,
      name: response.data.name,
      email: response.data.email,
      role: userRole,
      password: password.value
    };
    console.log(response.data);
    // Save user data to local storage
    localStorage.setItem('userData', JSON.stringify(userData));
    // Save login status to local storage
    localStorage.setItem('loggedIn', true);
    await store.dispatch('login', userData);

    setTimeout(() => {
      router.go('/profile');
    })
    router.push('/profile');
    // You can do something after successful signup, like redirecting the user to another page.
  } catch (error) {
    console.error('Signin failed:', error.response.data);
    errorMessage.value = error.response.data;
  }
};


const redirectToSignIn = () => {
  router.push('/signup');
}
const passwordFieldType = computed(() => showPassword.value ? 'text' : 'password');


</script>
