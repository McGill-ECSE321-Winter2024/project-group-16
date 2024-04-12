<template>
  <div class="container min-vh-90">
    <h2 class="mb-4 text-center">Customer's Registrations</h2>
    <div v-if="registrations.length === 0" class="text-center text-muted">
      <p>No registrations found.</p>
    </div>
    <template v-for="(registration, index) in registrations" :key="index">
      <div class="card mb-4 registration-card">
        <div class="card-header bg-dark text-white">
          <h5 class="mb-0">{{ registration.scheduledCourse.courseType.name }}</h5>
        </div>
        <div class="card-body">
          <ul class="list-group list-group-flush">
            <li class="list-group-item border-top-0">
              <strong>Confirmation Number:</strong> #{{ registration.confirmationNumber }}
            </li>
            <li class="list-group-item">
              <strong>Course Type:</strong> {{ registration.scheduledCourse.courseType.name}}
            </li>
            <li class="list-group-item">
              <strong>Date:</strong> {{ formatDate(registration.scheduledCourse.date) }}
            </li>
            <li class="list-group-item">
              <strong>Time:</strong> {{ formatTime(registration.scheduledCourse.startTime) }} - {{ formatTime(registration.scheduledCourse.endTime) }}
            </li>
            <li class="list-group-item">
              <strong>Location:</strong> {{ registration.scheduledCourse.location }}
            </li>
            <li class="list-group-item">
              <strong>Price:</strong> ${{ registration.scheduledCourse.courseType.price.toFixed(2) }}
            </li>
          </ul>
        </div>
      </div>
    </template>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

const userData = JSON.parse(localStorage.getItem("userData"));
const customerId = userData.id;
const registrations = ref([]);
const axiosClient = axios.create({
  baseURL: "http://localhost:8080"
});

const fetchRegistrations = async () => {
  try {
    const response = await axiosClient.get(`/customers/${customerId}/registrations`);
    registrations.value = response.data.registrations;
    console.log(response.data.registrations);
  } catch (error) {
    console.error('Error fetching registrations:', error);
  }
};

onMounted(() => {
  fetchRegistrations();
});

const formatDate = (dateString) => {
  return new Date(dateString).toLocaleDateString('en-US', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' });
};

const formatTime = (timeString) => {
  return new Date('1970-01-01T' + timeString).toLocaleTimeString('en-US', { hour: 'numeric', minute: '2-digit', hour12: true });
};
</script>

<style scoped>
.container {
  padding: 20px;
}

.registration-card {
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

.card-body {
  padding: 20px;
}

.list-group-item {
  border-color: rgba(0, 0, 0, 0.125);
}

.card-header {
  border-radius: 12px 12px 0 0;
}

.card-header h5 {
  margin-bottom: 0;
}

.btn-primary {
  background-color: #007bff;
  color: #fff;
  border: none;
  border-radius: 5px;
  padding: 8px 16px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.btn-primary:hover {
  background-color: #0056b3;
}
</style>
