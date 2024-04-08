
<template>
  <div class="card card-profile">
    <img
      src="../assets/importedpng/profile_page_middle.png"
      alt="Image placeholder"
      class="card-img-top"
    />
    <div class="row justify-content-center">
      <div class="col-4 col-lg-4 order-lg-2">
        <div class="mt-n4 mt-lg-n6 mb-4 mb-lg-0">
          <a href="javascript:;">
            <img
              src="../assets/importedpng/user.png"
              class="rounded-circle img-fluid"
            />
          </a>
        </div>
      </div>
    </div>
    <div class="card-header text-center border-0 pt-0 pt-lg-2 pb-4 pb-lg-3">
      <div class="justify-content-between">
        <div class="btn-group-vertical">
        <a href="javascript:;" class="btn  btn-block" v-if="userData.role === 'Customer'" @click="applyForInstructor" style="background-color: #E2725B; color: white;">Apply to become an instructor</a>
        <a href="javascript:;" class="btn  btn-block" v-if="userData.role === 'Instructor' || userData.role === 'Customer'" style="background-color: white ; color: #E2725B;" @click="deleteAccount">Delete my account</a>
      </div>
      </div>
    </div>
    <div class="card-body pt-0">
      <div class="row">
        <div class="col">
          <div class="d-flex justify-content-center">
            <div class="d-grid text-center" v-if="userData.role === 'Instructor' || userData.role === 'Customer'">
              <span class="text-lg font-weight-bolder">{{ scheduledCourses.length }}</span>
              <span class="text-sm opacity-8">Scheduled courses</span>

            </div>
            <div class="d-grid text-center mx-4" v-if="userData.role === 'Instructor'">
              <span class="text-lg font-weight-bolder">{{ supervisedCourses.length }}</span>
              <span class="text-sm opacity-8">Supervised courses</span>
            </div>
            <div class="d-grid text-center" v-if="userData.role === 'Instructor' || !!(userData.role === 'Owner')">
              <span class="text-lg font-weight-bolder">{{ suggestedCourses.length }}</span>
              <span class="text-sm opacity-8">Course types</span>
            </div>
          </div>
        </div>
      </div>
      <div class="text-center mt-4">
        <div class="h6 mt-4">
          <i class="ni business_briefcase-24 mr-2"></i> {{ userData.role }}
        </div>
        <h5>
          <span class="font-weight-light">{{ userData.name }}</span>
        </h5>
        <div class="h6 font-weight-300">
          <i class="ni location_pin mr-2"></i>{{ userData.email }}
        </div>
      </div>
      <br>
      <div class="justify-content-between">
          <!-- Error Message Template -->
<div v-if="errorMessage" class="alert alert-danger text-white" role="alert">
  {{ errorMessage }}
</div>

<!-- Success Message Template -->
<div v-if="successMessage" class="alert alert-success text-white" role="alert">
  {{ successMessage }}
</div>
</div>
    </div>

  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import axios from 'axios';
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()

// Retrieve userData from localStorage
const userData = JSON.parse(localStorage.getItem('userData'));

const axiosClient = axios.create({
  baseURL: "http://localhost:8080"
});

// Reactive variables for success and error messages
const successMessage = ref('');
const errorMessage = ref('');

var userID = userData.id;

// Function to delete user account
const deleteAccount = async () => {
  try {
    await axiosClient.delete(`/customers/${userID}`);
    console.log("Account deleted");
    localStorage.setItem("loggedIn",false);
    localStorage.setItem("userData",null);
    successMessage.value = "Account deleted successfully."
    // Redirect after a short delay
       setTimeout(() => {
      router.push("/signup");}, 2000); // Redirect after 2 seconds
  } catch (error) {
    console.error('Error deleting account:', error);
    errorMessage.value = 'There was an error deleting your account.'; // Set error message
  }
};

// Function to apply for instructorship
const applyForInstructor = async () => {
  try {
    const response = await axiosClient.put(`/customers/${userID}/apply`);
    console.log("Applied for instructor:", response.data);
    successMessage.value = "Applied for instructor successfully.";
    // Optionally, clear error message if set
    errorMessage.value = '';
  } catch (error) {
    console.error('Error applying for instructor:', error);
    errorMessage.value = 'There was an error applying for instructorship.'; // Set error message
    // Optionally, clear success message if set
    successMessage.value = '';
  }
};
import { onMounted } from "vue";

const scheduledCourses = ref([]);
const supervisedCourses = ref([]);
const suggestedCourses = ref([]);

// Function to fetch scheduled courses
const fetchScheduledCourses = async () => {
  try {
    const response = await axiosClient.get(`/customers/${userID}/registrations`);
    scheduledCourses.value = response.data.registrations;
  } catch (error) {
    console.error('Error fetching scheduled courses:', error);
  }
};


// Function to fetch supverised courses
const fetchSupervisedCourses = async () => {
  var response;
  try {
   response = await axiosClient.get(`/scheduledCourses/instructors/${userID}`);
     console.log(response.response.status)
    supervisedCourses.value = response.data.supervisedCourses;
    suggestedCourses.value = response.data.instructorSuggestedCourseTypes;
  } catch (error) {
    console.error('Error fetching supervised courses:', error);
  }
};


// Function to fetch supverised courses
const fetchOwnerSuggestedCourses = async () => {
  try {
    const response = await axiosClient.get(`/courseTypes/approvedByOwner/true`);
    suggestedCourses.value = response.data.courseTypes;
  } catch (error) {
    console.error('Error fetching owner course types:', error);
  }
};


onMounted(() => {
  if(userData.role === "Instructor" || userData.role === "Customer" ){
  fetchScheduledCourses();
  }
  if(userData.role === "Instructor"){
  fetchSupervisedCourses();
  }
  if(userData.role === "Owner"){
  fetchOwnerSuggestedCourses();
  }

});


</script>
