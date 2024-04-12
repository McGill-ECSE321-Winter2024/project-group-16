<template>
  <div class="classes-section">
    <div style="text-align:center;">
      <ArgonButton v-if="isLoggedIn && (userData.role === 'Owner' || userData.role === 'Instructor')"
                   style="color: white; background-color: #E2725B; display: inline-block; width: 250px;"
                   @click="toggleModal"> Suggest a course type
      </ArgonButton>
      <div class="title-container">
        <h1 class="header-text-classes" style="color: #E2725B;">Our Classes</h1>
        <p class="content">Gold Gym offers a wide variety of classes taught by trained professionals</p>
      </div>
    </div>
    <v-divider></v-divider>

    <v-dialog v-model="showModal" width="40%">
      <v-card
        prepend-icon="mdi-update"
        title="Add a New Course Type"
      >
        <v-form @submit.prevent="createCourseType(newCourse)">
          <v-text-field style="padding: 20px "
                        :rules="[rules.required]"
                        v-model="newCourse.name" label="Name" placeholder="Class Name"></v-text-field>
          <v-text-field style="padding: 20px " v-model="newCourse.description" label="Description"
                        :rules="[rules.required]"
                        placeholder="Description"></v-text-field>
          <v-text-field style="padding: 20px " v-model="newCourse.image" label="Image URL"
                        :rules="[rules.required]"
                        placeholder="Image URL"></v-text-field>
          <v-text-field style="padding: 20px " v-model.number="newCourse.price" label="Price"
                        :rules="[rules.required, rules.price]" type="number"
                        placeholder="Price"></v-text-field>
          <v-row justify-sm="center">
            <v-btn :disabled="!areFieldsFilled"
                   style="margin:20px; height: 50px; width:200px; background-color: #E2725B; color:white ;"
                   type="submit">Create Course Type
            </v-btn>
          </v-row>
          <v-divider></v-divider>
          <v-row justify-sm="center">
            <v-btn style="margin:20px; width:200px;" @click="toggleModal">Cancel</v-btn>
          </v-row>

          <div class="row">
            <div class="alert alert-success text-white fs-6" v-if="successMessage">{{ successMessage }}</div>
            <div class="alert alert-danger text-white fs-6" v-if="errorMessage">{{ errorMessage }}</div>
          </div>
        </v-form>
      </v-card>
    </v-dialog>


    <div class="container-fluid content" style="font-family: 'Open Sans', sans-serif;">
      <div
        style="max-height: 800px; flex-grow: 1; text-align: left;"
        class="classes-list">
        <div
          v-for="courseType in courseTypes"
          :key="courseType.id"
          class="class-item"
          @click="selectCourse(courseType)"
        >
          <img :src="courseType.image" class="class-icon" alt="Course Image"/>
          <span class="class-name" style="font-weight: bold;">{{ courseType.name }}</span>
        </div>
      </div>
      <div style="max-width: 80%; flex-grow: 1; padding: 20px; margin: 30px; text-align: center;" class="class-details"
           v-if="selectedCourse">
        <h2 style="font-size: 30px; color: #E2725B; font-weight: bold;" class="class-description">{{ selectedCourse?.name }}</h2>
        <img :src="selectedCourse?.image" class="class-image" alt="Selected Course Image"/>
        <p class="class-description" style="font-weight: bold;">{{ selectedCourse?.description }}</p>
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
import {computed, reactive, ref, onMounted} from 'vue';

const userData = JSON.parse(localStorage.getItem("userData"));
const isLoggedIn = localStorage.getItem("loggedIn");


const courseTypes = ref([]);
let selectedCourse = ref(null);
let showModal = ref(false);
const newCourse = reactive({
  name: '',
  description: '',
  image: '',
  price: null
});
const errorMessage = ref("");
const successMessage = ref("");

const areFieldsFilled = computed(() => {
  return (
    newCourse.name &&
    newCourse.description &&
    newCourse.image &&
    newCourse.price
  );
});

const loadCourseTypes = async () => {
  try {
    const response = await axios.get('http://localhost:8080/courseTypes/approvedByOwner/' + "true");
    courseTypes.value = response.data.courseTypes;
    if (courseTypes.value.length > 0) {
      selectedCourse.value = courseTypes.value[0];
    }
  } catch (error) {
    console.error('Error loading course types: ', error);
  }
};

const selectCourse = async (courseType) => {
  selectedCourse.value = courseType;
};

const rules = {
  required: value => !!value || 'Field is required',
  price: value => (value !== null && !isNaN(value) && value >= 0) || 'Price must be a valid number greater than or equal to zero'
};

const toggleModal = () => {
  showModal.value = !showModal.value;
};

const createCourseType = async (courseData) => {
  try {
    const response = await axios.post('http://localhost:8080/courseTypes', courseData);
    successMessage.value = 'Course Type Added Successfully!';
    errorMessage.value = '';
    setTimeout(() => {
      successMessage.value = '';
    }, 2000);
    loadCourseTypes();
    toggleModal();
  } catch (error) {
    console.error('Error creating course type: ', error.response.data.errors[0]);
    successMessage.value = '';
    errorMessage.value = error.response.data.errors[0];
    setTimeout(() => {
      errorMessage.value = '';
    }, 2000);
  }
};

onMounted(() => {
  loadCourseTypes();
});
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
  flex-direction: row;
  align-items: flex-start;
}

.classes-list {
  max-width: 300px;
  display: flex;
  flex-direction: column;
}


.class-item {
  width: 100%;
  padding: 20px;
  background-color: #fff;
  color: #E2725B;
  border-radius: 1rem;
 
  font-size: 20px;
  
  margin-bottom: 10px;
}

.class-item:hover {
  transform: scale(1.05);
}

.class-item.active {
  background-color: #f0f0f0;
}

.class-details {
  flex-grow: 1;
  width: 70%;
  max-width: 1200px;
  background-color: #fff;
  border-radius: 1rem;
  background-color: #fff;
  margin: 40px auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.class-details h2, .class-details .class-description, .class-details .weekly-schedule {
  max-width: 100%;
  background-color: #fff;
  border-radius: 1rem;
  padding: 20px;
  
  font-size: 1em;
  font-weight: 400;
  letter-spacing: 0.075em;
  text-transform: uppercase;
}

.class-details .class-image {
  width: 100%;
  border-radius: 1rem;

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
  border-radius: 1rem;
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
  border-radius: 1rem;

}

.header-text-classes {
  font-size: 2em;
  font-weight: bold;
  letter-spacing: 0.075em;
  text-transform: uppercase;
  margin: 20px;
}

.content {
  margin: 0 30px;
  color: #6a6868;
  

}
</style>

