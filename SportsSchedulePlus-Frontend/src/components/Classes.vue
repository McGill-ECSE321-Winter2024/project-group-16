<script setup>
import WeeklySchedule from '../components/WeeklySchedule.vue';
</script>



<template>
      <div class="classes-section">
        <button class="add-course-btn" @click="toggleModal">+</button>
      <div class="header">
        <div class="title-container">
        <h1>Our Classes</h1>
        <p>Unlock your full potential with our diverse range of courses tailored to empower and inspire. Step into a world of opportunity and discover the perfect course that resonates with your passion and goals</p>
      </div>
    </div>
    
    <!-- Modal Start -->
    <div class="modal" v-if="showModal">
      <div class="modal-content">
        <span class="close" @click="toggleModal">&times;</span>
        <form @submit.prevent="createCourseType(newCourse)">
          <input type="text" v-model="newCourse.name" placeholder="Name">
          <input type="text" v-model="newCourse.description" placeholder="Description">
          <input type="text" v-model="newCourse.image" placeholder="Image URL">
          <input type="number" v-model="newCourse.price" placeholder="Price">
          <button type="submit">Create Course Type</button>
        </form>
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
    <h2>{{ selectedCourse?.name }}</h2>
    <img :src="selectedCourse?.image" class="class-image" alt="Selected Course Image"/>
    <p class="class-description">{{ selectedCourse?.description }}</p>
    <WeeklySchedule
      displayType='courseType'
      :courseTypeId="selectedCourse?.id"
    />
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
      showModal: false,
      newCourse: {  // Initialize newCourse object for form binding
        name: '',
        description: '',
        image: '',
        price: null
      },
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
    toggleModal() {
      this.showModal = !this.showModal;
    }, 
    async createCourseType(courseData) {
      try {
        const response = await axios.post('http://localhost:8080/courseTypes', courseData);
        console.log(response.data);
        this.loadCourseTypes();
        this.toggleModal();
      } catch (error) {
        console.error('Error creating course type: ', error);
      }
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
.add-course-btn {
    position: absolute; /* Position the button absolutely within classes-section */
    left: 100px; /* Align to the left */
    top: 100px; /* Position at the center vertically */
    transform: translateY(-50%); /* Offset by half of its height for exact centering */
    background-color: #fff; /* Background color */
    border: 1px solid #ccc; /* Border properties */
    border-radius: 50%; /* Round shape */
    width: 3rem; /* Width of the button */
    height: 3rem; /* Height of the button */
    font-size: 2rem; /* Size of the plus symbol */
    line-height: 3rem; /* Center the plus symbol vertically */
    text-align: center; /* Center the plus symbol horizontally */
    cursor: pointer; /* Pointer cursor on hover */
    box-shadow: 0px 2px 5px rgba(0,0,0,0.2); /* Box shadow for some depth */
    outline: none; /* Remove outline for a clean look */
  }

.header {
  text-align: center;
}
.title-container p {
  font-size: 1.2rem;
  background-color: white;
  padding: 1rem; /* Adjust the padding as needed */
  border-radius: 1rem; /* This will give you the rounded corners */
  display: inline-block; /* This will shrink the width of the container to fit its content */
  margin: auto; /* This will center the container */
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1); /* Optional: adds shadow for depth */
}

.content {
    display: flex;
    align-items: flex-start; /* Align items to the start of the flex container */
    gap: 20px; /* This will keep left and right sections properly spaced */
  }

  .classes-list {
    width: auto;
    display: flex;
    flex-direction: column;
    align-items: flex-start; 
  }
  .modal {
  display: flex; /* Use flexbox for centering */
  justify-content: center; /* Center horizontally */
  align-items: center; /* Center vertically */
  position: fixed;
  z-index: 10; /* Make sure it's above other content */
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  overflow: auto;
  background-color: rgba(0,0,0,0.5); /* Dim the background */
}

.modal-content {
  background-color: #fff;
  padding: 20px;
  border-radius: 8px; /* Rounded corners */
  box-shadow: 0 4px 8px rgba(0,0,0,0.1); /* Shadow for depth */
  width: 30%; /* Adjust width as necessary */
  max-width: 450px; /* Adjust max width as necessary */
  margin: auto; /* For centering if not using flexbox in .modal */
}

/* Style the close button */
.close {
  color: #aaa;
  float: right;
  font-size: 28px;
  font-weight: bold;
}

.close:hover,
.close:focus {
  color: black;
  text-decoration: none;
  cursor: pointer;
}

/* Additional styles for form inputs */
input[type="text"],
input[type="email"],
input[type="password"],
input[type="number"] {
  width: 100%; /* Full width */
  padding: 10px;
  margin: 8px 0;
  display: inline-block;
  border: 1px solid #ccc;
  border-radius: 4px; /* Rounded corners for input fields */
  box-sizing: border-box; /* To make sure padding doesn't affect width */
}

/* Style the submit button */
button[type="submit"] {
  width: 100%;
  background-color: #f44336; /* Red color */
  color: white;
  padding: 14px 20px;
  margin: 8px 0;
  border: none;
  border-radius: 4px; /* Rounded corners for the button */
  cursor: pointer;
}

button[type="submit"]:hover {
  background-color: #d32f2f; /* Darker shade of red */
}

/* Adding icons for social media buttons */
.social-icons {
  display: flex;
  justify-content: center; /* Center the icons container */
  margin: 10px 0;
}

.social-icon {
  font-size: 24px; /* Size of the icons */
  margin: 0 10px; /* Spacing between icons */
  cursor: pointer; /* Change cursor on hover */
}

/* Social icon colors */
.facebook { color: #3b5998; }
.google { color: #dd4b39; }
.linkedin { color: #007bb6; }

/* Label for 'or use your account' */
.account-info {
  text-align: center;
  margin: 10px 0;
}

/* Forgot password text */
.forgot-password {
  text-align: center;
  display: block; /* Make it a block to occupy its own line */
  margin-top: 10px; /* Spacing above the text */
}

.class-item {
  width:500px;
  cursor: pointer;
  margin: 40px;
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
    flex-grow: 1; /* Allow this to grow to fill remaining space */
    width: 100%; /* Use 100% of the calculated max-width */
    max-width: 1500px;  /* Subtract the width of the class list and the gap */
    background-color: #fff;
    border-radius: 15px; /* This will allow the right container to fill the remaining space */
    background-color: #fff; /* White background for the entire right container */
    border-radius: 15px; /* Rounded corners */
    margin: 40px auto;
    padding: 20px; /* Padding inside the box */
    display: flex;
    flex-direction: column;
    align-items: center; /* This will center-align the items horizontally */
  }

  .class-details h2, .class-details .class-description, .class-details .weekly-schedule {
    max-width: 100%;
    background-color: #fff; /* White background for each section */
    border-radius: 10px; /* Rounded corners for each section */
    padding: 20px; /* Padding inside each section */
  }

  .class-details .class-image {
    width: 100%; /* This will make the image responsive */
    border-radius: 10px; /* Rounded corners for the image */
    
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
    width: auto; /* or 100% if you want it to fill the container */
    max-width: 800px; /* increase this value as needed */
    height: auto; /* to maintain aspect ratio */
    border-radius: 4px;
    margin-bottom: 1rem;
    display: block; /* Ensures the image is block level for centering */
    margin-left: auto; /* These two lines center the image horizontally */
    margin-right: auto;
  }

  .class-description {
    font-size: 1.25rem; /* Adjust the size as desired */
    margin-bottom: 2rem;
    text-align: justify;
    font-weight: bold;
  }

img {
  max-width: 100%;
  height: auto;
  border-radius: 4px;

}
</style>

  