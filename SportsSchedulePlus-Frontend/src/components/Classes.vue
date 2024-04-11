<template>
  <div class="classes-section">
    <div style="text-align:center;">
      <ArgonButton style="color: white; background-color: #E2725B; display: inline-block; width: 200px;"
                   @click="toggleModal"> Add Course Type
      </ArgonButton>
      <div class="title-container">
        <h1 class="header-text" style="color: #E2725B;">Our Classes</h1>
        <p class="content">Gold Gym offers a wide variety of classes taught by trained professionals</p>
      </div>
    </div>

    <v-dialog v-model="showModal" width="30%">
      <v-card
        prepend-icon="mdi-update"
        title="Add a New Course Type"
        @submit.prevent="createCourseType(newCourse)"
      >
<!--        <span class="close" @click="toggleModal">&times;</span>-->
        <v-form >
          <v-text-field style="padding: 20px "
                        v-model="newCourse.name" label="Name" placeholder="Class Name"></v-text-field>
          <v-text-field style="padding: 20px " v-model="newCourse.description" label="Description"
                        placeholder="Description"></v-text-field>
          <v-text-field style="padding: 20px " v-model="newCourse.image" label="Image URL"
                        placeholder="Image URL"></v-text-field>
          <v-text-field style="padding: 20px " v-model.number="newCourse.price" label="Price"
                        placeholder="Price"></v-text-field>
<!--          <v-btn type="submit">Create Course Type</v-btn>-->
        </v-form>
        <template #actions>
          <v-col>
            <v-btn @click="toggleModal" >Cancel</v-btn>
          </v-col>
          <v-divider style="color: white;"></v-divider>
          <v-col>
            <v-btn type="submit"
                   style="background-color: #E2725B; color:white ;">Submit
            </v-btn>
          </v-col>
        </template>

      </v-card>
    </v-dialog>



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
<script setup>
import WeeklySchedule from '../components/WeeklySchedule.vue';
import ArgonButton from "@/argon_components/ArgonButton.vue";
import axios from 'axios';
import {reactive, ref} from 'vue';

const courseTypes = ref([]);
let selectedCourse = ref(null);
let showModal = ref(false);
const newCourse = reactive({
  name: '',
  description: '',
  image: '',
  price: null
});

const loadCourseTypes = async () => {
  try {
    const response = await axios.get('http://localhost:8080/courseTypes');
    courseTypes.value = response.data.courseTypes;
    console.log(courseTypes.value);
  } catch (error) {
    console.error('Error loading course types: ', error);
  }
};

const selectCourse = async (courseType) => {
  selectedCourse.value = courseType;
  console.log(selectedCourse.value.id);
};

const toggleModal = () => {
  showModal.value = !showModal.value;
};

const createCourseType = async (courseData) => {
  try {
    const response = await axios.post('http://localhost:8080/courseTypes', courseData);
    console.log(response.data);
    loadCourseTypes();
    toggleModal();
  } catch (error) {
    console.error('Error creating course type: ', error);
  }
};
</script>

<style scoped>

.classes-section {
  display: flex;
  flex-direction: column;
}

.title-container p {
  font-size: 1.2rem;
  background-color: white;
  padding: 1rem;
  border-radius: 1rem;
  display: inline-block;
  margin: auto;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
}

.content {
  display: flex;
  align-items: flex-start;
  gap: 20px;
}

.classes-list {
  width: auto;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

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

input[type="text"],
input[type="email"],
input[type="password"],
input[type="number"] {
  width: 100%;
  padding: 10px;
  margin: 8px 0;
  display: inline-block;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
}

button[type="submit"] {
  width: 100%;
  background-color: #f44336;
  color: white;
  padding: 14px 20px;
  margin: 8px 0;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button[type="submit"]:hover {
  background-color: #d32f2f;
}


.class-item {
  width: 500px;
  cursor: pointer;
  margin: 40px;
  padding: 30px;
  background-color: #fff;
  border-radius: 1rem;
  transition: transform 0.3s ease;
  font-size: 30px;

}

.class-item:hover {
  transform: scale(1.05);
}

.class-item.active {
  background-color: #f0f0f0;
}

.class-details {
  flex-grow: 1;
  width: 100%;
  max-width: 1500px;
  background-color: #fff;
  border-radius: 15px;
  background-color: #fff;
  border-radius: 15px;
  margin: 40px auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.class-details h2, .class-details .class-description, .class-details .weekly-schedule {
  max-width: 100%;
  background-color: #fff;
  border-radius: 10px;
  padding: 20px;
}

.class-details .class-image {
  width: 100%;
  border-radius: 10px;

}

.overlay-subheader {
  color: white;
  font-size: 18px;
  font-weight: bold;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.class-icon {
  width: 50px;
  height: 50px;
  object-fit: cover;
  border-radius: 50%;
  margin-right: 10px;
}

.class-image {
  width: auto;
  max-width: 800px;
  height: auto;
  border-radius: 4px;
  margin-bottom: 1rem;
  display: block;
  margin-left: auto;
  margin-right: auto;
}

.class-description {
  font-size: 1.25rem;
  margin-bottom: 2rem;
  text-align: justify;
  font-weight: bold;
}

img {
  max-width: 100%;
  height: auto;
  border-radius: 4px;

}

.header-text {
  font-family: "Roboto Slab", serif;
  font-size: 2em;
  font-weight: bold;
  letter-spacing: 0.075em;
  text-transform: uppercase;
  margin: 20px;
}

.content {
  margin: 0 30px;
  color: #6a6868;
  font-family: 'Montserrat', sans-serif;
}
</style>

