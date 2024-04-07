<template>
    <!-- Dialog component -->
      <v-dialog v-model="dialog" max-width="500px">
        <v-card>
          <v-card-title class="headline">Confirm Deletion</v-card-title>
          <v-card-text>
            Are you sure you want to delete this instructor?
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
      <h6>Customers table</h6>
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
              <th
                class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7"
              >
                Status
              </th>
              <th  class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">
                Approve
              </th>
              <th class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">
                Delete
              </th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(customer, index) in customers" :key="index">
              <td>
                <div class="d-flex px-2 py-1">
                  <div>
                    <img
                      src="../assets/img/user.png"
                      class="avatar avatar-sm me-3"
                      :alt="customer.name"
                    />
                  </div>
                  <div class="d-flex flex-column justify-content-center">
                    <h6 class="mb-0 text-sm">{{ customer.name }}</h6>
                    <p class="text-xs text-secondary mb-0">{{ customer.email }}</p>
                  </div>
                </div>
              </td>
              <td>
                <p class="text-xs font-weight-bold mb-0">{{ customer.email }}</p>
                <p class="text-xs text-secondary mb-0">{{ customer.organization }}</p>
              </td>
              <td class="align-middle text-center text-sm">
                <span :class="{'badge': true, 'badge-sm': true, 'bg-gradient-success': customer.personRoleDto.hasApplied === true, 'bg-gradient-secondary':  customer.personRoleDto.hasApplied  === false}">{{ customer.personRoleDto.hasApplied  }}</span>
              </td>
              <td class="align-middle text-center text-sm">
                <span :class="{'badge': true, 'badge-sm': true, 'bg-gradient-warn':  'Approved', 'bg-gradient-secondary':  'Rejected'}">{{ 'Pending' }}</span>
              </td>
              <td class="align-middle text-center">
                <button
                  type="button"
                  class="btn control btn-info  badge mb-0 btn-pill fw-bold text-white"
                  @click="approveCustomer(customer)"
                  :disabled="!customer.personRoleDto.hasApplied"
                  data-toggle="tooltip"
                  data-original-title="Approve user"
                >
                  Approve
                </button>
              </td>
              <td class="align-middle text-center">
                <button
                  type="button"
                  class="control btn btn-danger  badge mb-0 btn-pill fw-bold"
                  @click="showDialog(customer.id)"
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
      <!-- Message component to display messages -->
      <div v-if="message" class="alert alert-dismissible fade show" :class="message.type" role="alert">
        {{ message.text }}
        <button type="button" class="btn-close" @click="clearMessage" aria-label="Close"></button>
      </div>
      <!-- End of Message component -->
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      customers: [],
      dialog: false,
      customerIdToDelete: null
    };
  },
  mounted() {
    this.loadCustomers();
  },
  methods: {
    async loadCustomers() {
      const axiosClient = axios.create({
        baseURL: "http://localhost:8080"
      });
      try {
        const response = await axiosClient.get('/customers');
        this.customers = response.data.persons;
        console.log(response.data.persons);

      } catch (error) {
        console.error('Error loading customers: ', error);
      }
    },
          async showDialog(customerId) {
                this.customerIdToDelete = customerId;
                this.dialog = true;
              },
          async confirmDelete() {
                if (this.customerIdToDelete) {
                  await this.deleteCustomer(this.customerIdToDelete);
                  // Reset customerIdToDelete and close dialog
                  this.customerIdToDelete = null;
                  this.dialog = false;
                }
           },
    async approveCustomer(customerId) {
      const axiosClient = axios.create({
        baseURL: "http://localhost:8080",

      });
      try {
        const response = await axiosClient.put(`/customers/approve/${customerId}`);


        this.loadCustomers(); // Reload the customer list after approval
        console.log("Customer approved successfully!");
      } catch (error) {
        console.error('Error approving customer:', error);
      }
    },
    clearMessage() {
    // Clear the message
    this.message = null;
  },
    async showMessage(text, type) {
    // Display message with the provided text and type
    this.message = { text, type };

    // Clear the message after 3 seconds
    setTimeout(this.clearMessage, 3000);
   },

    async deleteCustomer(customerId) {
      const axiosClient = axios.create({
        baseURL: "http://localhost:8080"
      });
      try {
        await axiosClient.delete(`/customers/${customerId}`);
        // Optionally, you can reload the customers list after deleting the user
        this.loadCustomers();
        console.log("User deleted successfully!");
      } catch (error) {
        console.error('Error deleting user:', error);
      }
    }
  }f
};


</script>

<style scoped>
/* Add your custom CSS styles here */
</style>
