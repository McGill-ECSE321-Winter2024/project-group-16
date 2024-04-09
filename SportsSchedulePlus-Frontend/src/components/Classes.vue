<script setup>
import WeeklySchedule from '../components/WeeklySchedule.vue';
</script>



<template>
      <div class="classes-section">
      <div class="header">
        <div class="title-container">
        <h1>Our Classes</h1>
        <p>Unlock your full potential with our diverse range of courses tailored to empower and inspire. Step into a world of opportunity and discover the perfect course that resonates with your passion and goals</p>
      </div>
    </div>
    <div class="content">
    <div class="classes-list">
      <div
        v-for="courseType in courseTypes"
        :key="courseType.id"
        class="class-item"
        @click="selectCourse(courseType)"
      >
        <img :src="courseType.image" class="class-icon" alt="Course Image"/> 
        <span class="class-name">{{ courseType.name }}</span> 
        <!-- ... -->
      </div>
    </div>
    <div class="class-details" v-if="selectedCourse">
    <h2>{{ selectedCourse.name }}</h2> <!-- Course name displayed above the image -->
    <img :src="selectedCourse.image" class="class-image" alt="Selected Course Image"/> <!-- Main image displayed here -->
    <p class="class-description">{{ selectedCourse.description }}</p> <!-- Description displayed directly below the image -->
    <WeeklySchedule
      displayType='courseType'
      :courseTypeId="selectedCourse.id"
    />
          <!-- Display price and state if course is selected -->
          <!-- Image and schedule button are omitted for simplicity -->
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        courseTypes: [], 
        selectedCourse: null,
      };
    },
    mounted() {
      this.loadCourseTypes(); 
    },
    methods: {
      async loadCourseTypes() {
        try {
          const response = await axios.get('http://localhost:8080/courseTypes'); 
          this.courseTypes = response.data.courseTypes; 
          console.log(this.courseTypes);
        } catch (error) {
          console.error('Error loading course types: ', error);
        }
      },
      async selectCourse(courseType) {
        this.selectedCourse = courseType;
        console.log(this.selectedCourse.id);
      },
    }
  };
  </script>
  
  <style scoped>
.classes-section {
  display: flex;
  flex-direction: column;
}
.header-box {
  background-color: #fff;
  margin: 10px;
  padding: 30px;
  border-radius: 1rem;
  box-shadow: 0px 0px 10px 0px rgba(0,0,0,0.1); /* Optional: Adds a subtle shadow like the course boxes */
}

.header {
  text-align: center;
}
.title-container {
  background-color: white;
  padding: 1rem; /* Adjust the padding as needed */
  border-radius: 1rem; /* This will give you the rounded corners */
  display: inline-block; /* This will shrink the width of the container to fit its content */
  margin: auto; /* This will center the container */
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1); /* Optional: adds shadow for depth */
}

.content {
  display: flex;
  margin-top: 20px;
}

.classes-list {
  flex-basis: 30%;
  padding: 10px;
}

.class-item {
  cursor: pointer;
  margin: 1cm;
  padding: 30px; /* Increased padding for a bigger box */
  background-color: #fff;
  border-radius: 1rem; /* Rounded edges */
  transition: transform 0.3s ease; /* Optional: add hover effect */
  font-size: 30px;
  
}

.class-item:hover {
  transform: scale(1.05); /* Optional: scale up on hover */
}

.class-item.active {
  background-color: #f0f0f0;
}

.class-details {
  flex-basis: 70%;
  padding: 10px;
}
.class-details h2 {
  text-align: center; /* Centers the text horizontally */
}
.image-placeholder {
    width: 100%; /* Full width of the details panel */
    height: 300px; /* Or any height you prefer */
    background-color: #eaeaea; /* A light grey background to look like a placeholder */
    display: flex;
    justify-content: center;
    align-items: center;
    border: 2px dashed #ccc; /* A dashed border to make it look like a placeholder box */
    border-radius: 10px; /* Consistent with the class-details box */
    margin-bottom: 10px; /* Add some margin below the placeholder */
  }

  .class-icon {
    width: 50px; /* Example size, adjust as needed */
    height: 50px; /* Example size, adjust as needed */
    object-fit: cover;
    border-radius: 50%; /* Makes the image round */
    margin-right: 10px; /* Adds space between image and text */
  }

  .class-image {
    max-width: 75%;
    height: auto;
    border-radius: 4px;
    margin-bottom: 1rem;
    display: block; /* Ensures the image is block level for centering */
    margin-left: auto; /* These two lines center the image */
    margin-right: auto;
  }

  .class-description {
    margin-bottom: 3rem;
  }

img {
  max-width: 100%;
  height: auto;
  border-radius: 4px;
}
</style>

  