<script setup>
import ArgonButton from "@/argon_components/ArgonButton.vue";
import ModifyDailySchedule from "@/components/ModifyDailySchedule.vue";
import {ref, onMounted} from "vue";
import axios from 'axios';

const dailySchedule = ref([]);
const isLoggedIn = ref(localStorage.getItem('loggedIn') || undefined)
const userData = JSON.parse(localStorage.getItem("userData"));
const isOwner = isLoggedIn.value && userData != null && userData.role === "Owner";
const dailyScheduleDictionary = {
  0: "Monday",
  1: "Tuesday",
  2: "Wednesday",
  3: "Thursday",
  4: "Friday",
  5: "Saturday",
  6: "Sunday"
};

const axiosClient = axios.create({
  baseURL: "http://localhost:8080"
});

const loadDailySchedule = async () => {
  try {
    const response = await axiosClient.get('/openingHours');
    dailySchedule.value = response.data.dailySchedules;
  } catch (error) {
    console.error('Error loading daily schedule: ', error);
  }
};

const reloadDashboard = () => {
  console.log("made it");
  window.location.reload();
};

onMounted(loadDailySchedule);

</script>

<template>
  <div class="py-3 container-fluid">
    <!--    Video-->
    <div class="video-wrapper">
      <video controls
             autoplay
             loop
             muted
             preload="auto"
             class="video">
        <source src="@/assets/importedpng/gym-video.mp4" type="video/mp4">
      </video>
      <div class="video-overlay">
        <div class="overlay-content">
          <p class="overlay-subheader">WORK HARDER, GET STRONGER, LIVE LONGER</p>
          <h1 class="overlay-header">MADE EASY AT <strong>GOLD GYM</strong></h1>
          <div class="button-container">
            <a href="/signup" v-if="!isLoggedIn">
              <ArgonButton style="background-color: #E2725B; color: white; display: inline-block; width: 150px;">Join
                Now
              </ArgonButton>
            </a>
          </div>
          <a href="/signin" v-if="!isLoggedIn">
            <ArgonButton style="background-color: white; color: #E2725B; display: inline-block; width: 150px;">Log In
            </ArgonButton>
          </a>
        </div>
      </div>
    </div>

    <!--    About Us Section-->
    <div class="container-fluid">
      <div class="d-flex flex-wrap">
        <div style="max-width: 35%; flex-grow: 1; padding-right: 20px; text-align: center;">
          <v-sheet
            style="margin-top: 20px; margin-bottom: 20px; justify-content: flex-start; border-radius: 1rem;"
            class="d-flex flex-wrap text-center"
            elevation="4"
            height="100%"
            width="100%"
            rounded
          >
            <div class="content">
              <h1 class="header-text" style="font-size: 30px;"><strong style="color: #E2725B; font-family: 'Open sans', sans-serif">ABOUT US</strong></h1>
              <p class="header-text" style="font-family: 'Open sans', sans-serif">welcome to gold gym, where fitness meets community </p>
              <p style="text-align: justify;">At Gold Gym, we're more than just a gym; we're a community of <strong>fitness
                enthusiasts</strong> dedicated to helping you achieve your <strong> health</strong> and <strong>
                wellness</strong> goals. Established with a vision to revolutionize the fitness experience, we strive to
                provide a welcoming and motivating environment for individuals of all fitness levels.</p>
              <a href="/classes">
                <Argon-Button style="background-color: white; color: #E2725B; border: 2px solid #E2725B;"> View Classes
                  Offered
                </Argon-Button>
              </a>

            </div>
          </v-sheet>
        </div>
        <div style="max-width: 65%; flex-grow: 1;">
          <div
            style="width: 100%; height:100%; display: flex; justify-content: flex-end; margin-top: 20px; margin-bottom: 20px;">
            <img src="../assets/importedpng/about-us.jpg" alt="About us page picture"
                 style="max-width: 100%; border-radius: 1rem;">
          </div>
        </div>
      </div>
    </div>

    <!-- Get in Touch Section -->
    <div class="container-fluid" style="margin-top: 20px;">
      <div class="d-flex flex-wrap">
        <v-sheet
          style="margin-top: 20px; margin-bottom: 20px; justify-content: flex-start; position: relative; border-radius: 1rem;"
          class="d-flex flex-wrap text-center"
          elevation="4"
          height="100%"
          width="100%"
          rounded
        >
          <!-- First Column: Business Hours -->
          <div class="column content">
            <h1 class="header-text" style="font-size: 20px; font-family: 'Open sans', sans-serif"><strong style="color: #E2725B;">Business Hours</strong>
            </h1>
            <div v-for="(day, index) in dailySchedule" :key="index">
              <p style="width: 100%; display:grid;"><strong>{{ dailyScheduleDictionary[index] }}</strong> {{ day.openingTime }}AM - {{ day.closingTime }}PM
              </p>
            </div>
            <ModifyDailySchedule v-if="isOwner" @scheduleUpdated="reloadDashboard"/>
          </div>

          <!-- Second Column: Address -->
          <div class="column content">
            <h1 class="header-text" style="font-size: 20px; font-family: 'Open sans', sans-serif"><strong style="color: #E2725B;">Address</strong>
            </h1>
            <p>123 Riverside Drive</p>
            <p>Saint-Henri, QC, J4P 3J5</p>
            <p> Suite 106</p>
          </div>

          <!-- Third Column: Phone Number and Email -->
          <div class="column content">
            <h1 class="header-text" style="font-size: 20px;"><strong style="color: #E2725B;font-family: 'Open sans', sans-serif">Contact Us</strong>
            </h1>
            <p class="header-text">Phone</p>
            <p>123-456-7890</p>
            <p class="header-text">Email </p>
            <p>sports.schedule.plus@gmail.com</p>
            <p class="header-text">Owner </p>
            <p> Jessie Galasso-Carbonnel</p>
          </div>

          <!-- Vertical Dividers -->
          <div style="position: absolute; top: 0; bottom: 0; left: 33.33%; width: 1px; background-color: #ccc;"></div>
          <div style="position: absolute; top: 0; bottom: 0; left: 66.66%; width: 1px; background-color: #ccc;"></div>
        </v-sheet>
      </div>
    </div>


  </div>

</template>

<style>
.column {
  flex: 1;
  padding: 20px;
}

.content {
  margin: 0 30px;
  color: #6a6868;
  font-family: 'Montserrat', sans-serif;
}

.overlay-header,
.overlay-subheader {
  font-family: 'Montserrat', sans-serif;
}

.video {
  display: block;
  max-width: 100%;
  height: auto;
  border-radius: 15px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.video-wrapper {
  position: relative;
}

.video-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  text-align: center;
  border-radius: 15px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.video::-webkit-media-controls {
  display: none !important;
}

.video::-webkit-media-controls-enclosure {
  display: none !important;
}

.video::-webkit-media-controls-panel {
  display: none !important;
}


.overlay-content {
  position: relative;
  top: 35%;
  width: 100%;
  height: 100%;

}

.overlay-header {
  color: white;
  font-size: 56px;
  font-weight: bold;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.overlay-subheader {
  color: white;
  font-size: 18px;
  font-weight: bold;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.button-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 15px;
}

.header-text {
  font-family: "Roboto Slab", serif;
  font-size: 1em;
  font-weight: 400;
  letter-spacing: 0.075em;
  text-transform: uppercase;
  margin: 20px;
}

</style>
