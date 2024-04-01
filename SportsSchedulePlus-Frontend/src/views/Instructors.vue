<template>
    <div class="card">
      <div class="card-header pb-0">
        <h6>Instructors table</h6>
      </div>
      <div class="card-body px-0 pt-0 pb-2">
        <div class="table-responsive p-0">
          <table class="table align-items-center mb-0">
            <thead>
              <tr>
                <th
                  class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7"
                >
                  Name
                </th>
                <th
                  class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7 ps-2"
                >
                  Email
                </th>
                <th
                  class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7"
                >
                  Applied
                </th>
               
                <th class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">
                  Delete
                </th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(instructor, index) in instructors" :key="index">
                <td>
                  <div class="d-flex px-2 py-1">
                    <div>
                      <img
                        src="../assets/img/user.png"
                        class="avatar avatar-sm me-3"
                        :alt="instructor.name"
                      />
                    </div>
                    <div class="d-flex flex-column justify-content-center">
                      <h6 class="mb-0 text-sm">{{ instructor.name }}</h6>
                      <p class="text-xs text-secondary mb-0">{{ instructor.email }}</p>
                    </div>
                  </div>
                </td>
                <td>
                  <p class="text-xs font-weight-bold mb-0">{{ instructor.email }}</p>
                  <p class="text-xs text-secondary mb-0">{{ instructor.organization }}</p>
                </td>
                <td class="align-middle text-center text-sm">
                  <span :class="{'badge': true, 'badge-sm': true, 'bg-gradient-success': instructor.personRoleDto.hasApplied === true, 'bg-gradient-secondary':  instructor.personRoleDto.hasApplied  === false}">{{ instructor.personRoleDto.hasApplied  }}</span>
                </td>
            
                <td class="align-middle text-center">
                <button
                  type="button"
                  class="control btn btn-danger  badge mb-0 btn-pill fw-bold"
                  @click="deleteInstructor(instructor.id)"
                  data-toggle="tooltip"
                  data-original-title="Delete user"
                >
                  Delete
                </button>
               </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        instructors: []
      };
    },
    mounted() {
      this.loadInstructors();
    },
    methods: {
      async loadInstructors() {
        const axiosClient = axios.create({
          baseURL: "http://localhost:8080"
        });
        try {
          const response = await axiosClient.get('/instructors');
          this.instructors = response.data.persons;
          console.log(response.data.persons);
          
        } catch (error) {
          console.error('Error loading instructors: ', error);
        }
      },
      async deleteInstructor(instructorId) {
        const axiosClient = axios.create({
          baseURL: "http://localhost:8080"
        });
        try {
          await axiosClient.delete(`/instructors/${instructorId}`);
          // Optionally, you can reload the instructors list after deleting the instructor
          this.loadInstructors();
          console.log("Instructor deleted successfully!");
        } catch (error) {
          console.error('Error deleting instructor:', error);
        }
      }
    }
  };
  </script>
  
  <style scoped>
  /* Add your custom CSS styles here */
  </style>
  