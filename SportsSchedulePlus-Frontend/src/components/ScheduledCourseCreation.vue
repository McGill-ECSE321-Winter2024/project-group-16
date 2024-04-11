<template>
  <div class="container mt-5">
    <div class="row justify-content-center">
      <div class="col">
        <div class="card-body">
          <form @submit.prevent="submitForm">
            <div class="form-group">
              <label for="courseType">Class Type</label>
              <v-select
                label="Select a class type"
                :items="courseTypeObjects"
                v-model="selectedCourseTypeId"
                item-value="id"
                item-title="name"
                variant="outlined"
              ></v-select>
              <small v-if="!isCourseTypeValid" class="text-danger">Please select a class type.</small>
            </div>
            <div class="form-group">
              <div class="row">
                <div class="col">
                  <label for="startTime">Start Time</label>
                  <!-- <v-time-picker
                    v-model="startTime"
                    format="24hr"
                    no-title
                    readonly
                  ></v-time-picker> -->
                  <v-text-field
                    v-model="startTime"
                    prepend-icon="mdi-clock-time-four-outline"
                    readonly
                    style="padding: 10px 30px;"
                  ></v-text-field>
                </div>
                <div class="col">
                  <label for="endTime">End Time</label>
                  <!-- <v-time-picker
                    v-model="endTime"
                    format="24hr"
                    no-title
                    readonly
                  ></v-time-picker> -->
                  <v-text-field
                    v-model="endTime"
                    prepend-icon="mdi-clock-time-four-outline"
                    readonly
                    style="padding: 10px 30px;"
                  ></v-text-field>
                </div>
              </div>
            </div>
            <div class="form-group">
              <div class="row">
                <div class="col d-flex justify-content-center">
                  <label for="date">Date</label>
                  <v-date-picker
                    v-model="date"
                    no-title
                  ></v-date-picker>
                </div>
              </div>
            </div>
            <div class="form-group">
              <label for="location">Location</label>
              <input type="text" class="form-control" v-model="location" placeholder="Enter location">
              <small v-if="!isLocationValid" class="text-danger">Please enter a location.</small>
            </div>
            <div class="form-group">
              <div class="text-center">
                <button type="submit" class="btn btn-primary btn-lg px-5 mt-3 text-white">Schedule Class</button>
              </div>
            </div>
            <div class="form-group">
              <label for="successMessage" v-if="successMessage" class="text-success">{{ successMessage }}</label>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>



  
<script>
  import axios from 'axios';
  import { VTimePicker } from 'vuetify/labs/VTimePicker'

  export default {
    data() {
      return {
        courseTypes: [],
        courseTypeObjects:[],
        selectedCourseTypeId: null,
        isCourseTypeValid: true,
        isLocationValid: true,
        date: null,
        startTime: null,
        endTime: null,
        location: null,
        instructorId: null,
        successMessage: null,
      };
    },
    mounted() {
      this.loadCourseTypes();
    },
    components: {
      // VTimePicker,
    },
    methods: {
      async loadCourseTypes() {
        const axiosClient = axios.create({
          baseURL: "http://localhost:8080"
        });
        try {
          // extract event time information from local storage
          const startDate = JSON.parse(localStorage.getItem("startTime"));
          const endDate = JSON.parse(localStorage.getItem("endTime"));
          this.startTime = startDate.split("T")[1];
          this.endTime = endDate.split("T")[1];
          this.date = new Date(startDate);
          // extract available course Types
          const courseTypeResponse = await axiosClient.get('/courseTypes/approvedByOwner/' + true);
          const courseTypesData = courseTypeResponse.data.courseTypes;
          this.courseTypeObjects = courseTypesData;
          // extract instructor data
          const userData = JSON.parse(localStorage.getItem("userData"));
          this.instructorId = userData.id;
        } catch (error) {
          console.log(error);
        }
      },
      validateCourseType() {
        if (this.selectedCourseTypeId === null) {
          this.isCourseTypeValid = false;
        } else {
          this.isCourseTypeValid = true;
        }
      },
      validateLocation() {
        if (this.location === null) {
          this.isLocationValid = false;
        } else {
          this.isLocationValid = true;
        }
      },
      async submitForm() {
        const axiosClient = axios.create({
          baseURL: "http://localhost:8080"
        });
        this.validateCourseType();
        this.validateLocation();
        if (this.isCourseTypeValid && this.isLocationValid) {
          const courseTypeData = {
            id: this.selectedCourseTypeId,
          };
          const courseData = {
            date: this.date.toISOString().split("T")[0],
            startTime: this.startTime,
            endTime: this.endTime,
            location: this.location,
            instructorId: this.instructorId,
            courseType: courseTypeData,
          };
          try {
            const scheduledCourseResponse = await axiosClient.post('/scheduledCourses', courseData);
            this.successMessage = 'Scheduled course created successfully.';
            location.reload();
          } catch (error) {
            console.log(error);
          }
        }
      }
    }
  }
</script> 