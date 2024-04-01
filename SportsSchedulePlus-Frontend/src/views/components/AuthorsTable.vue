<template>
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
                Status
              </th>
              <th
                class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7"
              >
                Joined
              </th>
              <th class="text-secondary opacity-7"></th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(customer, index) in customers" :key="index">
              <td>
                <div class="d-flex px-2 py-1">
                  <div>
                    <img
                      src="/../assets/img/user.png"
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
                <p class="text-xs font-weight-bold mb-0">{{ customer.function }}</p>
                <p class="text-xs text-secondary mb-0">{{ customer.organization }}</p>
              </td>
              <td class="align-middle text-center text-sm">
                <span :class="{'badge': true, 'badge-sm': true, 'bg-gradient-success': customer.status === 'Online', 'bg-gradient-secondary': customer.status === 'Offline'}">{{ customer.status }}</span>
              </td>
              <td class="align-middle text-center">
                <span class="text-secondary text-xs font-weight-bold">{{ customer.employed }}</span>
              </td>
              <td class="align-middle">
                <a
                  href="javascript:;"
                  class="text-secondary font-weight-bold text-xs"
                  data-toggle="tooltip"
                  data-original-title="Edit user"
                >Edit</a>
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
      customers: []
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
      } catch (error) {
        console.error('Error loading customers: ', error);
      }
    }
  }
};
</script>

<style scoped>
/* Add your custom CSS styles here */
</style>
