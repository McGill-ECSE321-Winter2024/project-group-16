<template>
  <main class="main-content mt-0">
    <div class="page-header align-items-start min-vh-45 pt-5 ml-3 mr-3 border-radius-lg text-center"
         :style="{backgroundImage: 'url(' + imagePath + ')', backgroundPosition: 'top'}">
    </div>
    <div class="container">
      <div class="row mt-lg-n10 mt-md-n11 mt-n10 justify-content-center">
        <div class="col-xl-4 col-lg-5 col-md-7 mx-auto">
          <div class="card shadow">
            <div class="card z-index-0">
              <div class="card-header text-center pt-4">
                <h5 class="card-title">Sign Up</h5>
              </div>
            </div>
            <v-form v-model="form" @submit.prevent="signUp">
              <div class="card-body">
                <v-text-field
                  v-model="name"
                  id="Name"
                  color="#E2725B"
                  label="Name"
                  variant="underlined"
                  aria-label="Name"
                  placeholder="John Smith"
                  :rules="[rules.required]"
                ></v-text-field>

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

                <v-text-field
                  
                  :type="showPassword ? 'text' : 'password'"
                  :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
                  @click:append="togglePassword"
                  
                  v-model="password"
                  color="#E2725B"
                  label="Password"
                  aria-label="Email"
                  variant="underlined"
                  placeholder="Enter your password"
                  visible="false"
                  :rules="[rules.required]"
                  :hint="passwordHints"

                ></v-text-field>

                <v-divider></v-divider>

                <v-card-actions>
                  <v-btn
                    :disabled="!form"
                    :loading="loading"
                    color="#E2725B"
                    size="large"
                    type="submit"
                    variant="elevated"
                    block
                  >
                    Complete Registration
                  </v-btn>
                </v-card-actions>
                <div class="alert" role="alert" v-if="errorMessage">
                  <v-alert
                    color="error"
                    variant="outlined"
                    :text="errorMessage"
                  >
                  </v-alert>
                </div>
                <div v-if="success">
                  <v-alert
                    color="success"
                    variant="outlined"
                    text="Sign up successful. You will now be redirected to the sign in page."
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
import {ref} from 'vue';
import axios from 'axios';
import {useRouter} from 'vue-router';
import image from '../assets/importedpng/signin_and_signup_top.png';

const success = ref(false);
const imagePath = image;
const name = ref('');
const email = ref('');
const password = ref('');
const showPassword = ref(false);
const errorMessage = ref('');
const router = useRouter()
const loading = ref(false);
const form = ref(false)

const togglePassword = () => {
  showPassword.value = !showPassword.value;
};

const passwordHints = "Between 8-20 characters, \n" +
  "1 uppercase letter, \n" +
  "1 lowercase letter, \n" +
  "1 special character";

const rules = {
  required: value => !!value || 'Field is required',
};

const axiosClient = axios.create({
  baseURL: "http://localhost:8080"
});

const onSubmit = () => {
  if (!form.value) return;
  loading.value = true;
  setTimeout(() => (loading.value = false), 2000);
};

const signUp = async () => {
  errorMessage.value = '';
  try {
    const response = await axiosClient.post('/authentication/signup', {
      name: name.value,
      email: email.value,
      password: password.value,
    });
    console.log('Signup successful:', response.data.message);
    success.value = true
    setTimeout(() => {
      router.push('/signin');
    }, 3500);

  } catch (error) {
    console.error('Signup failed:', error.response.data.errors);
    errorMessage.value = error.response.data.errors[0];
  }
};
  
</script>
