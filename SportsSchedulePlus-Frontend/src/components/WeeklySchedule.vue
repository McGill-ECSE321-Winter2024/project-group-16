<script setup>
import CourseRegistration from './CourseRegistration.vue';
</script>

<template>
  <div style="margin-left: 1.5%; margin-bottom: 20px;">
    <h6>Week of {{ selectedDayFormatted }}</h6>
  </div>
  <div class="wrap" style="margin-top: 20px;">
    <div class="left">
      <DayPilotNavigator id="nav" :config="navigatorConfig" />
    </div>
    <div class="content">
      <DayPilotCalendar id="dp" :config="config" ref="calendar" />
    </div>
  </div>
<template>

    <div>
      <v-dialog v-model="registerDialogVisible">
        <v-card class="popup"> <!-- change the style of this to be rounded corners like all other cards-->
          <v-card-title>
            Register for a Class
          </v-card-title>

          <v-card-text>
            Class: {{ courseDetails.courseType }}<br>
            Price: {{ courseDetails.price }}<br>
            Instructor: {{ courseDetails.instructor }}<br>
            Start Time: {{ courseDetails.startTime }}<br>
            End Time: {{ courseDetails.endTime }}<br>
            <CourseRegistration 
              :customerID="userID"
              :courseID="courseDetails.courseId"
            />
          </v-card-text>
          <!-- <v-card-actions>
            <v-btn @click="" color="#E2725B">
              Register
            </v-btn>
          </v-card-actions> -->
          <v-card-actions>
            <v-btn @click="registerDialogVisible = false" color="#E2725B">
              Close
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </div>
  </template>
</template>

<script>
import axios from 'axios';
import {DayPilotCalendar, DayPilotNavigator} from '@daypilot/daypilot-lite-vue';
import { ref } from 'vue';

try{
  const userData = JSON.parse(localStorage.getItem("userData"));
  let email = ref('');
  email.value = userData.email;
  let name = ref('');
  name.value =  userData.name;
  let userID = userData.id;
} catch (error) {
  console.log(error);
}


export default {
  name: 'Calendar',
  data: function() {
    const today = new Date().toISOString().split('T')[0];
    return {
      selectedDayFormatted: new Date().toLocaleDateString('en-US', { month: 'long', day: 'numeric', year: 'numeric' }),
      registerDialogVisible: false,
      navigatorConfig: {
        showMonths: 1,
        skipMonths: 1,
        selectMode: "Week",
        startDate: today,
        selectionDay: today,
        theme: "swagnavigator",
        onTimeRangeSelected: args => {
          this.config.startDate = args.day;
          const startDate = new Date(args.start); // Convert args.start to Date object
          const sunday = this.getSundayOfWeek(startDate);
          this.selectedDayFormatted = this.formatDate(sunday);
        }
      },
      config: {
        lane: "auto",
        theme: "swag",
        viewType: "Week",
        startDate: today,
        durationBarVisible: false,
        timeRangeSelectedHandling: "Disabled",
        eventDeleteHandling: "Disabled",
        eventMoveHandling: "Disabled",
        eventClickHandling: "Enabled",
        eventResizeHandling: "Disabled",
        businessBeginsHour: 8,
        businessEndsHour: 18,
        headerDateFormat: "dddd",
        heightSpec: "BusinessHoursNoScroll",
        onEventClick: (args) => {
          this.registerDialogVisible = true;
          this.updateScheduledCourseInfo(args);
        },
      },
      courseDetails: {
        courseId: '',
        courseType: '',
        instructor: '',
        price: '',
        startTime: '',
        endTime: ''
      },
    };
  },
  mounted() {
    this.loadScheduledCourses();
  },
  props: {
    displayType: { // instructor, customer, courseType, anythin else will display all scheduled courses
        type: String,
        required: false,
    },
    customerId: {
        type: Number,
        required: false,
        default: 0
    },
    instructorId: {
        type: Number,
        required: false,
        default: 0
    },
    courseTypeId: {
        type: Number,
        required: false,
        default: 0
    },
  },
  components: {
    DayPilotCalendar,
    DayPilotNavigator
  },
  computed: {
    calendar() {
      return this.$refs.calendar.control;
    }
  },
  methods: {
    async loadScheduledCourses() {
      const axiosClient = axios.create({
        baseURL: "http://localhost:8080"
      });
      try {
        // update business hours
        const response = await axiosClient.get('/openingHours');
        const dailySchedules = response.data.dailySchedules;
        let minOpeningTime = dailySchedules[0].openingTime;
        let maxClosingTime = dailySchedules[0].closingTime;
        for (let i = 1; i < dailySchedules.length; i++) {
          const schedule = dailySchedules[i];
          if (schedule.openingTime < minOpeningTime) {
            minOpeningTime = schedule.openingTime;
          }
          if (schedule.closingTime > maxClosingTime) {
            maxClosingTime = schedule.closingTime;
          }
        }
        const todayDate = new Date();
        const minOpeningDateTime = new Date(todayDate.toDateString() + ' ' + minOpeningTime);
        const maxClosingDateTime = new Date(todayDate.toDateString() + ' ' + maxClosingTime);
        this.config.businessBeginsHour = minOpeningDateTime.getHours();
        this.config.businessEndsHour = maxClosingDateTime.getHours();

        // update events displayed
        const today = new Date().toISOString().split('T')[0];
        let endpoint = '';
        let events = [];
        if (this.displayType === 'instructor') {
            // add the courses that the instructor is registered for
            endpoint = '/customers/' + this.instructorId + '/registrations'; // registration controller
            const registrationsResponse = await axiosClient.get(endpoint);
            events = registrationsResponse.data.registrations.map(registration => ({
                id: registration.scheduledCourse.id,
                start: registration.scheduledCourse.date + 'T' + registration.scheduledCourse.startTime, // combine date and start time
                end: registration.scheduledCourse.date + 'T' + registration.scheduledCourse.endTime, // combine date and end time
                text: registration.scheduledCourse.courseType.description // display course type name as text
            }));

            // get the instructor email
            endpoint = '/persons/' + this.instructorId; // person controller
            const personResponse = await axiosClient.get(endpoint);
            const instructorEmail = personResponse.data.email;

            // add the courses that the instructor is teaching
            endpoint = '/instructors/' + instructorEmail + '/supervised-courses'; // instructor controller
            const scheduledCoursesResponse = await axiosClient.get(endpoint);
            events = events.concat(scheduledCoursesResponse.data.scheduledCourses.map(course => ({
                id: course.id,
                start: course.date + 'T' + course.startTime, // combine date and start time
                end: course.date + 'T' + course.endTime, // combine date and end time
                text: course.courseType.description // display course type name as text
            })));
        } else if (this.displayType === 'customer') { // if we want to get the scheduled courses of a customer
            endpoint = '/customers/' + this.customerId + '/registrations'; // registration controller
            const registrationsResponse = await axiosClient.get(endpoint);
            events = registrationsResponse.data.registrations.map(registration => ({
                id: registration.scheduledCourse.id,
                start: registration.scheduledCourse.date + 'T' + registration.scheduledCourse.startTime, // combine date and start time
                end: registration.scheduledCourse.date + 'T' + registration.scheduledCourse.endTime, // combine date and end time
                text: registration.scheduledCourse.courseType.description // display course type name as text
            }));
        } else if (this.displayType === 'courseType') { // if we want to get the schеduled courses of a course type
            endpoint = '/courseTypes/' + this.courseTypeId + '/scheduledCourses'; // scheduled course controller
            const scheduledCoursesResponse = await axiosClient.get(endpoint);
            events = scheduledCoursesResponse.data.scheduledCourses.map(course => ({
                id: course.id,
                start: course.date + 'T' + course.startTime, // combine date and start time
                end: course.date + 'T' + course.endTime, // combine date and end time
                text: course.courseType.description // display course type name as text
            }));
        } else {
            endpoint = '/scheduledCourses'; // scheduled course controller
            const scheduledCoursesResponse = await axiosClient.get(endpoint);
            events = scheduledCoursesResponse.data.scheduledCourses.map(course => ({
                id: course.id,
                start: course.date + 'T' + course.startTime, // combine date and start time
                end: course.date + 'T' + course.endTime, // combine date and end time
                text: course.courseType.description // display course type name as text
            }));
        }

        this.scheduledCourses = events;
        this.calendar.update({ events });
      } catch (error) {
        console.error('Error loading classes: ', error);
      }
    },
    async updateScheduledCourseInfo(args) {
      const axiosClient = axios.create({
        baseURL: "http://localhost:8080"
      });
      const scheduledCourseId = args.e.id();
      try {
        const scheduledCourseResponse = await axiosClient.get('/scheduledCourses/course/' + scheduledCourseId);
        const instructorResponse = await axiosClient.get('/instructors/supervised-course/' + scheduledCourseId);
        this.courseDetails = {
          courseId: scheduledCourseResponse.data.id,
          courseType: scheduledCourseResponse.data.courseType.name,
          instructor: instructorResponse.data.persons[0].name,
          price: scheduledCourseResponse.data.courseType.price,
          startTime: scheduledCourseResponse.data.startTime,
          endTime: scheduledCourseResponse.data.endTime
        };
      } catch (error) {
        console.error('Error loading classes: ', error);
      }

    },
    getSundayOfWeek(date) {
      var day = date.getDay();
      var diff = date.getDate() - day; // adjust when day is Sunday
      return new Date(date.setDate(diff));
    },
    formatDate(date) {
      var options = { year: 'numeric', month: 'long', day: 'numeric' };
      return date.toLocaleDateString('en-US', options);
    },
    clearMessage() {
      // Clear the message
      this.message = null;
    },
    async showMessage(text, type) {
      // display message with the provided text and type
      this.message = { text, type };
      // Clear the message after 3 seconds
      setTimeout(this.clearMessage, 3000);
    },
  }
};
</script>

<style>
/* Calendar Container */
.swag_main {
  border: 1px solid #ccc;
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* Add subtle shadow */
}

/* Column Header */
.swag_colheader_inner {
  text-align: center;
  padding: 8px 0;
  color: #333;
  font-weight: bold;
}

/* Row Header */
.swag_rowheader_inner {
  font-size: 14px;
  text-align: right;
  padding: 10px;
  color: #666;
}
/* Calendar Cell */
.swag_cell_inner {
  position: relative;
  border-right: 1px solid #ddd;
  border-bottom: 1px solid #ddd;
  background-color: #fff;
}

/* Event */
.swag_event {
  color: #fff;
  font-weight: bold;
  background-color: #ff6f61;
  border-radius: 6px;
  padding: 8px;
}

/* Event Inner */
.swag_event_inner {
  position: absolute;
  overflow: hidden;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  margin: 0;
  background-color: #ffffff;
  border: 1px solid #ddd;
  border-radius: 6px;
  padding: 8px;
}

/* Selected Week */
.swag_selected {
  background-color: salmon;
}

/* Hover Effect for Cells */
.swag_cell:hover {
  background-color: #f0f0f0;
}

/* Navigator Container */
.swagnavigator_main {
  border: 1px solid #ddd;
  font-size: 0.9rem;
  margin: 15px auto;
  max-width: 900px;
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

/* Navigator Text and Styling */
.swagnavigator_title,
.swagnavigator_titleleft,
.swagnavigator_titleright {
  color: #333;
  background: #f7f7f7;
  text-align: center;
  padding: 5px 0;
  font-weight: bold;
}
.swagnavigator_dayheader,
.swagnavigator_cell,
.swagnavigator_weeknumber,
.swagnavigator_weekend {
  color: black;
  background-color: white;
  text-align: center;
  font-weight:lighter;
  padding: 8px;
}

.swagnavigator_todaybox {
  border: 1px solid #333;
}
.swagnavigator_select .swagnavigator_cell_box { background-color: #E2725B; opacity: 1; }


</style>