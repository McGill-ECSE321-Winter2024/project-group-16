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
                    ></v-time-picker> -->
                    <!-- <v-text-field
                      v-model="startTime"
                      :active="startTimeModal"
                      :focused="startTimeModal"
                      prepend-icon="mdi-clock-time-four-outline"
                      readonly
                      style="padding: 10px 30px;"
                    >
                      <v-dialog v-model="startTimeModal" activator="parent" width="auto" height="auto">
                        <v-card>
                          <v-card-text>
                            <v-container>
                              <v-time-picker format="24hr" :scrollable="true" v-if="startTimeModal" v-model="startTime"
                                            :allowed-minutes="[0,15,30,45]"
                              ></v-time-picker>
                            </v-container>
                          </v-card-text>
                          <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn @click="startTimeModal = false">Confirm Selection</v-btn>
                          </v-card-actions>
                        </v-card>
                      </v-dialog>
                    </v-text-field> -->
                    <v-text-field
                      v-model="startTime"
                      prepend-icon="mdi-clock-time-four-outline"
                      readonly
                      style="padding: 10px 30px;"
                    ></v-text-field>
                  </div>
                  <div class="col">
                    <label for="endTime">End Time</label>
                    <!-- <v-text-field
                      v-model="endTime"
                      :active="endTimeModal"
                      :focused="endTimeModal"
                      prepend-icon="mdi-clock-time-four-outline"
                      readonly
                      style="padding: 10px 30px;"
                    >
                      <v-dialog v-model="endTimeModal" activator="parent" width="auto" height="auto">
                        <v-card>
                          <v-card-text>
                            <v-container>
                              <v-time-picker format="24hr" :scrollable="true" v-if="endTimeModal" v-model="endTime"
                                            :allowed-minutes="[0,15,30,45]"
                              ></v-time-picker>
                            </v-container>
                          </v-card-text>
                          <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn @click="endTimeModal = false">Confirm Selection</v-btn>
                          </v-card-actions>
                        </v-card>
                      </v-dialog>
                    </v-text-field> -->
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
                  <button type="submit" class="btn btn-primary btn-lg px-5 mt-3 text-white">Update Class</button>
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
    import {ref, computed, watch, defineEmits} from 'vue'
  
    export default {
      data() {
        return {
            courseTypes: [],
            courseId: null,
            courseTypeObjects:[],
            selectedCourseTypeId: null,
            isCourseTypeValid: true,
            isLocationValid: true,
            date: null,
            startTime: null,
            endTime: null,
            startTimeModal: false,
            endTimeModal: false,
            location: null,
            instructorId: null,
            successMessage: null,
            courseDetails: {
                courseId: '',
                courseType: '',
                courseTypeId: 0,
                instructor: '',
                instructorId: 0,
                price: '',
                location: '',
                date: '',
                startTime: '',
                endTime: ''
            },
        };
      },
      mounted() {
        this.loadScheduledCourse();
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
            // extract available course Types
            const courseTypeResponse = await axiosClient.get('/courseTypes/approvedByOwner/' + true);
            const courseTypesData = courseTypeResponse.data.courseTypes;
            this.courseTypeObjects = courseTypesData;
            // extract instructor data
            const userDataString = localStorage.getItem("userData");
            let userData;
            if (userDataString !== null) {
              userData = JSON.parse(userDataString);
            } else {
              console.error("No user data found in localStorage");
            }
            this.instructorId = userData.id;
          } catch (error) {
            console.log(error);
          }
        },
        loadScheduledCourse() {
            const scheduledCourseDetails = localStorage.getItem("courseDetails");
            if (scheduledCourseDetails !== null) {
                this.courseDetails = JSON.parse(scheduledCourseDetails);
            } else {
                console.error("No course details found in localStorage");
            }
            this.courseId = this.courseDetails.courseId;
            this.selectedCourseTypeId = this.courseDetails.courseTypeId;
            this.date = new Date(`${this.courseDetails.date} ${this.courseDetails.startTime}`);
            this.startTime = this.courseDetails.startTime;
            this.endTime = this.courseDetails.endTime;
            this.location = this.courseDetails.location;
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
              startTime: this.startTime + ":00",
              endTime: this.endTime + ":00",
              location: this.location,
              instructorId: 0,
              courseType: courseTypeData,
            };
            console.log(courseData);
            console.log(this.startTime);
            try {
              const scheduledCourseResponse = await axiosClient.put('/scheduledCourses/' + this.courseId, courseData);
              console.log(scheduledCourseResponse);
              this.successMessage = 'Scheduled course updated successfully.';
              location.reload();
            } catch (error) {
              console.log(error);
            }
          }
        }
      }
    }
  </script> 