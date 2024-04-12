<template>
  <!-- Edit Dialog component -->
  <v-dialog v-model="editDialog" max-width="500px">
    <v-card>
      <v-card-title class="headline">Edit course type</v-card-title>
      <v-card-text>
        <v-text-field
          v-model="editedCourseType.name"
          label="Name"
        ></v-text-field>
        <v-text-field
          v-model="editedCourseType.description"
          label="Description"
        ></v-text-field>
        <v-text-field
          v-model="editedCourseType.image"
          label="Image URL"
        ></v-text-field>
        <v-text-field
          v-model="editedCourseType.price"
          label="Price"
          type="number"
        ></v-text-field>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="error" text @click="editDialog = false">Cancel</v-btn>
        <v-btn color="success" text @click="saveEditedCourseType">Save</v-btn>
      </v-card-actions>
      <!-- Message component for displaying errors -->
      <div
        v-if="editErrorMessage"
        class="alert alert-dismissible fade show"
        role="alert"
      >
        {{ editErrorMessage }}
      </div>
    </v-card>
  </v-dialog>

  <!-- Dialog component -->
  <v-dialog v-model="dialog" max-width="500px">
    <v-card>
      <v-card-title class="headline">Confirm Deletion</v-card-title>
      <v-card-text>
        Are you sure you want to delete this course type?
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="error" text @click="dialog = false">Cancel</v-btn>
        <v-btn color="success" text @click="confirmDelete">Delete</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
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
                Edit
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
                      :src="courseType.image"
                      class="avatar avatar-sm me-3"
                    />
                  </div>
                  <div class="d-flex flex-column justify-content-center">
                    <h6 class="mb-0 text-sm">{{ courseType.name }}</h6>
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
                  class="bg-info border badge text-white badge-lg edit-course"
                  data-toggle="modal"
                  @click="showEditDialog(courseType.id)"
                  data-target="#editCourseModal"
                  data-original-title="Edit course type"
                >
                  Edit
                </button>
              </td>

              <td class="align-middle text-center">
                <button
                  type="button"
                  class="bg-gradient-danger border badge text-white badge-lg"
                  @click="showDialog(courseType.id)"
                  data-toggle="tooltip"
                  data-original-title="Delete course type"
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
      message: null,
      dialog: false,
      editDialog: false,
      courseTypeIdToDelete: null,
      editErrorMessage: null, // Property to hold edit error message
      courseTypeIdToEdit: null, // New variable to store the courseTypeId being edited
      editedCourseType: { // Object to store the edited course type fields
        name: '',
        description: '',
        image: '',
        price: 0
      }
    };
  },
    mounted() {
      this.loadCourseTypes();
    },
    methods: {

      async showEditDialog(courseTypeId) {
      // Set the courseTypeIdToEdit before opening the dialog
      this.courseTypeIdToEdit = courseTypeId;
      this.editDialog = true;
    },

    async saveEditedCourseType() {

  try {
    const axiosClient = axios.create({
          baseURL: "http://localhost:8080"
        });
    const { name, price,image, description } = this.editedCourseType;
    const updatedCourseType = { name,price,image,description};

    const response = await axiosClient.put(`/courseTypes/${this.courseTypeIdToEdit}`, updatedCourseType);
    console.log(response.data);
    console.log('Course type updated successfully:', response.data);
    // Close the edit dialog
    this.editDialog = false;
    // Reload the course types
    this.loadCourseTypes();
  } catch (error) {
    console.log(error.data);
    //console.error('Error updating course type:', error);
    //this.showMessage(error.response.data.errors[0], 'alert-info');
      // Display error message
      this.editErrorMessage = error.response.data.errors[0];
        console.error('Error updating course type:', error);
  }
},

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
          this.showMessage('Could not delete course type, course type must not have associated scheduled courses.', 'alert-warn');
          console.error('Error deleting course type:', error);
        }
      },
      async showDialog(courseTypeId) {
            this.courseTypeIdToDelete = courseTypeId;
            this.dialog = true;
          },
      async confirmDelete() {
            if (this.courseTypeIdToDelete ) {
              await this.deleteCourseType(this.courseTypeIdToDelete);
              // Reset courseTypeIdToDelete and close dialog
              this.courseTypeIdToDelete = null;
              this.dialog = false;
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
