<template>
    <div class="card">
      <div class="card-header pb-0">
        <h6>Course types table</h6>
      </div>
      <div class="card-body px-0 pt-0 pb-2">
        <div class="table-responsive p-0">
          <table class="table align-items-center mb-0">
            <thead>
              <tr>
                <th
                  class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7"
                >
                  Description
                </th>
                <th
                  class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7 ps-2"
                >
                  Price
                </th>
                <th
                  class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7"
                >
                  Status
                </th>
                <th
                  class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7"
                >
                  Approve
                </th>
                <th
                  class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7"
                >
                  Reject
                </th>
                <th
                  class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7"
                >
                  Delete
                </th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(courseType, index) in courseTypes" :key="index">
                <td>
                  <div class="d-flex px-2 py-1">
                    <div>
                      <img
                        src="../assets/img/circle.png"
                        class="avatar avatar-sm me-3"
                      />
                    </div>
                    <div class="d-flex flex-column justify-content-center">
                      <h6 class="mb-0 text-sm">{{ courseType.description }}</h6>
                    </div>
                  </div>
                </td>
                <td>
                  <p class="text-xs font-weight-bold mb-0">
                    {{ courseType.price.toLocaleString('en-US', { style: 'currency', currency: 'USD' }) }}
                  </p>
                </td>
  
                <td class="align-middle text-center text-sm">
                  <span
                    :style="{
                      'background': courseType.state.toUpperCase() === 'APPROVED' ? 'linear-gradient(to right, #85D4E3, #3498db)' :
                                  courseType.state.toUpperCase() === 'REJECTED' ? 'linear-gradient(to right, #F7CAC9, #FF6F61)' :
                                  courseType.state.toUpperCase() === 'NONE' ? 'linear-gradient(to right, #D3D3D3, #808080)' :
                                  courseType.state.toUpperCase() === 'PENDING' ? 'linear-gradient(to right, #FFDEAD, #FFA500)' : '',
                              'color': 'white',
                              'width':'100px',
                              'padding': '10px 10px',
                              'border-radius': '5px',
                              'background-size': '100% 100%'
                  }"
                    class="badge badge-lg"
                    >{{ courseType.state }}
                  </span>
                </td>
  
                <td class="align-middle text-center">
                  <button
                    type="button"
                    class="bg-gradient-success border badge text-white badge-lg"
                    @click="approveCourseType(courseType.id)"
                    :disabled="courseType.state.toUpperCase() === 'APPROVED' || !!courseType.approvedByOwner"
                    data-toggle="tooltip"
                    data-original-title="Approve course type"
                  >
                    Approve
                  </button>
                </td>
                <td class="align-middle text-center">
                  <button
                    type="button"
                    class="bg-gradient-warning border badge text-white badge-lg"
                    @click="rejectCourseType(courseType.id)"
                    :disabled="courseType.state.toUpperCase() === 'APPROVED' || courseType.state.toUpperCase() === 'REJECTED' || !!courseType.approvedByOwner"
                    data-toggle="tooltip"
                    data-original-title="Reject course type"
                  >
                    Reject
                  </button>
                </td>
                <td class="align-middle text-center">
                  <button
                    type="button"
                    class="bg-gradient-danger border badge text-white badge-lg"
                    @click="deleteCourseType(courseType.id)"
                    data-toggle="tooltip"
                    data-original-title="Delete user"
                  >
                    Delete
                  </button>
                </td>
              </tr>
  
              <!-- Add empty rows to fill the table -->
              <tr
                v-for="index in Math.max(0,5)"
                :key="'empty' + index"
                class="empty-row"
              >
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <!-- Message component to display messages -->
      <div
        v-if="message"
        class="alert alert-dismissible mb-2 px-2 fade show"
        :class="message.type"
        role="alert"
      >
        {{ message.text }}
        <button
          type="button"
          class="btn-close"
          @click="clearMessage"
          aria-label="Close"
        ></button>
      </div>
      <!-- End of Message component -->
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        courseTypes: [],
        message: null
      };
    },
    mounted() {
      this.loadCourseTypes();
    },
    methods: {
      async loadCourseTypes() {
        const axiosClient = axios.create({
          baseURL: "http://localhost:8080"
        });
        try {
          const response = await axiosClient.get('/courseTypes');
          this.courseTypes = response.data.courseTypes;
        } catch (error) {
          this.showMessage('Error loading course types', 'alert-danger');
          console.error('Error loading course types: ', error);
        }
      },
      async approveCourseType(courseTypeId) {
        const axiosClient = axios.create({
          baseURL: "http://localhost:8080"
        });
        try {
          await axiosClient.put(`/courseTypes/approve/${courseTypeId}`);
          this.loadCourseTypes(); // Reload the course type list after approval
          this.showMessage('Course type approved successfully!', 'alert-warn');
        } catch (error) {
          this.showMessage('Error approving course type', 'alert-danger');
          console.error('Error approving course type:', error);
        }
      },
      async rejectCourseType(courseTypeId) {
        const axiosClient = axios.create({
          baseURL: "http://localhost:8080"
        });
        try {
          await axiosClient.put(`/courseTypes/reject/${courseTypeId}`);
          this.loadCourseTypes(); // Reload the course type list after rejection
          this.showMessage('Course type rejected successfully!', 'alert-warn');
        } catch (error) {
          this.showMessage('Error rejecting course type', 'alert-danger');
          console.error('Error rejecting course type:', error);
        }
      },
      async deleteCourseType(courseTypeId) {
        const axiosClient = axios.create({
          baseURL: "http://localhost:8080"
        });
        try {
          await axiosClient.delete(`/courseTypes/${courseTypeId}`);
          this.loadCourseTypes(); // Reload the course type list after deleting the course type
          this.showMessage('Course type deleted successfully!', 'alert-warn');
        } catch (error) {
          this.showMessage('Error deleting course type, course type must not have associated scheduled courses.', 'alert-warn');
          console.error('Error deleting course type:', error);
        }
      },
      clearMessage() {
        // Clear the message
        this.message = null;
      },
      showMessage(text, type) {
        // Display message with the provided text and type
        this.message = { text, type };
  
        // Clear the message after 3 seconds
        setTimeout(this.clearMessage, 3000);
      }
    }
  };
  </script>
  
  <style scoped>
  .empty-row td {
    height: 50px; /* Adjust as needed */
  }
  </style>
  