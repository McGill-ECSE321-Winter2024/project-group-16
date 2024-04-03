<template>
    <div class="classes-section">
      <div class="header">
        <h1>Our Classes</h1>
        <p>Nunc urna sem, laoreet ut metus id, aliquet consequat magna. Sed viverra ipsum dolor, ultricies fermentum massa consequat eu.</p>
      </div>
      <div class="content">
        <div class="classes-list">
          <div
            v-for="course in courses"
            :key="course.id"
            class="class-item"
            :class="{ 'active': selectedCourse && course.id === selectedCourse.id }"
            @click="selectCourse(course)"
          >
            <span class="class-icon">🏋️</span>
            <span class="class-name">{{ course.name }}</span>
          </div>
        </div>
        <div class="class-details" v-if="selectedCourse">
          <h2>{{ selectedCourse.name }}</h2>
          <p>{{ selectedCourse.description }}</p>
          <img :src="selectedCourse.imageUrl" alt="Class image" />
          <button @click="viewSchedule">View Schedule</button>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        courses: [
          { id: 1, name: 'Boxing Class', description: 'This class is dedicated for beginners who would like to learn how to box', imageUrl: '/Screenshot_4.png' },
          { id: 2, name: 'CrossFit', description: 'Class description here...', imageUrl: '/Screenshot_1.png' },
          { id: 3, name: 'Swim', description: 'Class description here...', imageUrl: '/Screenshot_3.png' },
          { id: 4, name: 'Bicycle', description: 'Class description here...', imageUrl: '/Screenshot_2.png' }

          
        ],
        selectedCourse: null,
      };
    },
    methods: {
      selectCourse(course) {
        this.selectedCourse = course;
      },
      viewSchedule() {
        // logic to view the schedule
      },
    },
  };
  </script>
  
  <style scoped>
  .classes-section {
    display: flex;
    flex-direction: column;
  }
  
  .header {
    text-align: center;
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
    margin: 10px;
    padding: 10px;
    background-color: #fff;
    border: 1px solid #ccc;
    border-radius: 4px;
  }
  
  .class-item.active {
    background-color: #f0f0f0;
  }
  
  .class-details {
    flex-basis: 70%;
    padding: 10px;
  }
  
  .class-icon {
    /* Add styles for your class icon */
  }
  
  .class-name {
    /* Add styles for your class name */
  }
  
  img {
    max-width: 100%;
    height: auto;
    border-radius: 4px;
  }
  </style>
  