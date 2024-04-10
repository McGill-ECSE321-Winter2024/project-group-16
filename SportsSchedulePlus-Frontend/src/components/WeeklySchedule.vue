<script setup>
import CourseRegistration from './CourseRegistration.vue';
import ScheduledCourseCreation from './ScheduledCourseCreation.vue';
</script>

<template>
  <div style="margin-left: 1.5%;">
    <h6>Week of {{ selectedDayFormatted }}</h6>
  </div>
  <div class="wrap">
    <div class="left">
      <DayPilotNavigator id="nav" :config="navigatorConfig" />
    </div>
    <div class="content">
      <DayPilotCalendar id="dp" :config="config" ref="calendar" />
    </div>
  </div>
  <template>
    <div>
      <v-dialog v-model="registerDialogVisible" persistent>
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
            <CourseRegistration />
          </v-card-text>
          <v-card-actions>
            <v-btn @click="registerDialogVisible = false" color="#E2725B">
              Close
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </div>
  </template>
  <template>
    <div>
      <v-dialog v-model="creationDialogVisible" persistent>
        <v-card class="popup"> <!-- change the style of this to be rounded corners like all other cards-->
          <v-card-title>
            Schedule a Class
          </v-card-title>

          <v-card-text>
            <ScheduledCourseCreation />
          </v-card-text>
          <v-card-actions>
            <v-btn @click="creationDialogVisible = false" color="#E2725B">
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

const userData = JSON.parse(localStorage.getItem("userData"));
let email = ref('');
email.value = userData.email;
let name = ref('');
name.value =  userData.name;
let userID = userData.id;

export default {
  name: 'Calendar',
  data: function() {
    const today = new Date().toISOString().split('T')[0];
    return {
      selectedDayFormatted: new Date().toLocaleDateString('en-US', { month: 'long', day: 'numeric', year: 'numeric' }),
      registerDialogVisible: false,
      creationDialogVisible: false,
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
        headerDateFormat:"dddd",
        startDate: today,
        durationBarVisible: false,
        timeRangeSelectedHandling: "Enabled",
        eventDeleteHandling: "Enabled",
        eventMoveHandling: "Enabled",
        eventClickHandling: "Enabled",
        eventResizeHandling: "Enabled",
        businessBeginsHour: 8,
        businessEndsHour: 18,
        heightSpec: "BusinessHoursNoScroll",
        onEventClick: (args) => {
          this.registerDialogVisible = true;
          this.updateScheduledCourseInfo(args);
        },
        onEventMove: (args) => {
          this.updateScheduledCourse(args);
        },
        onEventResize: (args) => {
          this.updateScheduledCourse(args);
        },
        onEventDelete: (args) => {
          this.deleteScheduledCourse(args);
        },
        onTimeRangeSelected: (args) => {
          this.creationDialogVisible = true;
          localStorage.setItem("startTime", JSON.stringify(args.start));
          localStorage.setItem("endTime", JSON.stringify(args.end));
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
            endpoint = '/scheduledCourses/' + today; // scheduled course controller
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
        localStorage.setItem("scheduledCourseId", scheduledCourseResponse.data.id)
      } catch (error) {
        console.error('Error loading classes: ', error);
      }

    },
    async updateScheduledCourse(args) {
      const axiosClient = axios.create({
        baseURL: "http://localhost:8080"
      });
      const scheduledCourseId = args.e.id();
      const newDate = args.newStart.toString().split('T')[0];
      const newStartTime = args.newStart.toString().split('T')[1].split('.')[0];
      const newEndTime = args.newEnd.toString().split('T')[1].split('.')[0];
      try {
        const updateResponse = await axiosClient.put('/scheduledCourses/' + scheduledCourseId, {
          id: scheduledCourseId,
          date: newDate,
          startTime: newStartTime,
          endTime: newEndTime,
          location: "",
          instructorId: 0,
          courseType: null,
        });
        console.log(updateResponse);
      } catch (error) {
        console.error('Error moving class: ', error);
      }
      this.loadScheduledCourses();
    },
    async deleteScheduledCourse(args) {
      const axiosClient = axios.create({
        baseURL: "http://localhost:8080"
      });
      const scheduledCourseId = args.e.id();
      try {
        const deleteResponse = await axiosClient.delete('/scheduledCourses/' + scheduledCourseId);
        console.log(deleteResponse);
      } catch (error) {
        console.error('Error deleting class: ', error);
      }
      this.loadScheduledCourses();
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
.wrap {
  display: flex;
  margin: 0 1.5% 1.5% 1.5%;
}

.left {
  margin-right: 10px;
}

.content {
  flex-grow: 1;
}

.popup {
  position: fixed;
  top: 50%;
  left: 30%;
  width: 50%;
  /* transform: translate(-50%, -50%); */
}


.swag_main 
{
	border: 1px solid #ffffff;
}
.swag_main, .swag_main td 
{
	font-size: 0.875rem;
  text-transform: uppercase;
}
/* Event Styling */
.swag_event {
  color: #fff;
  font-weight: bold;
  border-radius: 10px; /* Rounded corners */
  background-color: #4f69ec; /* Pretty color */
  padding: 20px; /* Increased padding for better appearance */
  max-width: 250px; /* Adjust width as needed */
  margin: 20px; /* Adjust margin as needed */
  box-shadow: 0px 4px 8px rgba(76, 175, 80, 0.2); /* Add a subtle shadow for depth */
  transition: transform 0.2s ease; /* Add smooth transition effect */
}

.swag_event:hover {
  transform: translateY(-5px); /* Add a subtle hover effect */
}

.swag_event_inner {
  position: relative;
}

.swag_event_title {
  font-size: 1.2rem;
  margin-bottom: 10px;
}

.swag_event_details {
  font-size: 1rem;
  margin-bottom: 10px;
}

.swag_event_time {
  font-weight: bold;
}

.swag_event_description {
  line-height: 1.4;
}

.swag_event_link {
  display: inline-block;
  margin-top: 10px;
  color: #1a73e8; /* Adjust link color */
  text-decoration: none;
  transition: color 0.3s ease;
}

.swag_event_link:hover {
  color: #0d47a1; /* Adjust link hover color */
}

.swag_alldayevent { 
}
.swag_alldayevent_inner { 
	position: absolute;
	overflow: hidden;
	left: 1px;
	right: 0px;
	top: 1px;
	bottom: 0px;
	margin: 0px;
	color: #333333;
	background-color: #ffffff;
	background: -webkit-gradient(linear, left top, left bottom, from(#ffffff), to(#fafafa));  
	background: -webkit-linear-gradient(top, #ffffff 0%, #fafafa);
	background: -moz-linear-gradient(top, #ffffff 0%, #fafafa);
	background: -ms-linear-gradient(top, #ffffff 0%, #fafafa);
	background: -o-linear-gradient(top, #ffffff 0%, #fafafa);
	background: linear-gradient(top, #ffffff 0%, #fafafa);
	filter: progid:DXImageTransform.Microsoft.Gradient(startColorStr="#ffffff", endColorStr="#fafafa");
	padding: 2px;
	border: 1px solid #cccccc;
	text-align: left;
}
.swag_alldayheader_inner
{
	text-align: center; 
	position: absolute;
	top: 0px;
	left: 0px;
	bottom: 0px;
	right: 0px;
	border-right: 1px solid #ffffff;
	border-bottom: 1px solid  #ffffff;
	color: #000000;
	background: #f8f9fa;
}
.swag_colheader_inner {
  text-align: center;
  padding: 8px; /* Increased padding for better spacing */
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  border-right: 1px solid #ffffff;
  border-bottom: 1px solid #ffffff;
  color: #000000;
  background-color: #f8f9fa;
}

.swag_rowheader_inner {
  font-size: 20px;
  text-align: right;
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  border-right: 1px solid #ffffff;
  border-bottom: 1px solid #ffffff;
  color: #000000;
  background-color: #f8f9fa;
  padding: 8px; /* Increased padding for better spacing */
}

.swag_rowheader_minutes 
{
	font-size:10px; 
	vertical-align: super; 
	padding-left: 2px;
	padding-right: 2px;
}
.swag_corner_inner
{
	position: absolute;
	top: 0px;
	left: 0px;
	bottom: 0px;
	right: 0px;
	border-right: 1px solid #ffffff;
	border-bottom: 1px solid  #ffffff;
	color: #000000;
	background: #f8f9fa;
}
.swag_cornerright_inner
{
	position: absolute;
	top: 0px;
	left: 0px;
	bottom: 0px;
	right: 0px;
	border-right: 1px solid #ffffff;
	border-bottom: 1px solid  #ffffff;
	color: #000000;
	background: #f8f9fa;
}
.swag_rowheader_inner {
	padding: 2px;
}
.swag_cell_inner
{
	position: absolute;
	top: 0px;
	left: 0px;
	bottom: 0px;
	right: 0px;
	border-right: 1px solid #f2f2f2;
	border-bottom: 1px solid #f2f2f2;
	background: #ffffff;
}
.swag_cell_business .swag_cell_inner {
	background: #ffffff;
}
.swag_message 
{
	padding: 10px;
	opacity: 0.9;
	filter: alpha(opacity=90);
	color: #ffffff;
	background: #ffa216;
}
.swag_shadow_inner 
{
	background-color: #666666;
	opacity: 0.5;
	filter: alpha(opacity=50);
	height: 100%;
	-moz-border-radius: 5px;
	-webkit-border-radius: 5px;
	border-radius: 5px;
}
.swag_event_bar
{
	top: 0px;
	left: 0px;
	bottom: 0px;
	width: 4px;
	background-color: #1a764e;
}
.swag_event_bar_inner  
{
	position: absolute;
	width: 4px;
	background-color: #1a764e;
}
.swag_event_delete {
	background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAsAAAALCAYAAACprHcmAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAAadEVYdFNvZnR3YXJlAFBhaW50Lk5FVCB2My41LjExR/NCNwAAAI5JREFUKFNtkLERgCAMRbmzdK8s4gAUlhYOYEHJEJYOYOEwDmGBPxC4kOPfvePy84MGR0RJ2N1A8H3N6DATwSQ57m2ql8NBG+AEM7D+UW+wjdfUPgerYNgB5gOLRHqhcasg84C2QxPMtrUhSqQIhg7ypy9VM2EUZPI/4rQ7rGxqo9sadTegw+UdjeDLAKUfhbaQUVPIfJYAAAAASUVORK5CYII=) center center no-repeat; 
	opacity: 0.6; 
	-ms-filter:'progid:DXImageTransform.Microsoft.Alpha(Opacity=60)'; 
	cursor: pointer;
}
.swag_event_delete:hover {
	opacity: 1;
	-ms-filter: none;
}
.swag_scroll_up {
	background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAB3RJTUUH2wESDiYcrhwCiQAAAAlwSFlzAAAuIwAALiMBeKU/dgAAAARnQU1BAACxjwv8YQUAAACcSURBVHjaY2AgF9wWsTW6yGMlhi7OhC7AyMDQzMnBXIpFHAFuCtuaMTP+P8nA8P/b1x//FfW/HHuF1UQmxv+NUP1c3OxMVVhNvCVi683E8H8LXOY/w9+fTH81tF8fv4NiIpBRj+YoZtZ/LDUoJmKYhsVUpv0MDiyMDP96sIYV0FS2/8z9ICaLlOhvS4b/jC//MzC8xBG0vJeF7GQBlK0xdiUzCtsAAAAASUVORK5CYII=);
}
.swag_scroll_down {
	background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAALiMAAC4jAXilP3YAAACqSURBVChTY7wpam3L9J+xmQEP+PGPKZZxP4MDi4zI78uMDIwa2NT+Z2DYovrmiC+TI8OBP/8ZmEqwGvif4e8vxr+FIDkmEKH25vBWBgbG0+iK/zEwLtF+ffwOXCGI8Y+BoRFFIdC030x/WmBiYBNhpgLdswNJ8RSYaSgmgk39z1gPUfj/29ef/9rwhQTDHRHbrbdEbLvRFcGthkkAra/9/uMvhkK8piNLAgCRpTnNn4AEmAAAAABJRU5ErkJggg==);
}
.swag_now { background-color: red; }
.swag_now:before { content: ''; top: -5px; border-width: 5px; border-color: transparent transparent transparent red; border-style: solid; width: 0px; height:0px; position: absolute; -moz-transform: scale(.9999); }
.swag_shadow_forbidden .swag_shadow_inner { background-color: red; }
.swag_shadow_top { box-sizing: border-box; padding:2px;border:1px solid #ccc;background:#fff;background: -webkit-gradient(linear, left top, left bottom, from(#ffffff), to(#eeeeee));background: -webkit-linear-gradient(top, #ffffff 0%, #eeeeee);background: -moz-linear-gradient(top, #ffffff 0%, #eeeeee);background: -ms-linear-gradient(top, #ffffff 0%, #eeeeee);background: -o-linear-gradient(top, #ffffff 0%, #eeeeee);background: linear-gradient(top, #ffffff 0%, #eeeeee);filter: progid:DXImageTransform.Microsoft.Gradient(startColorStr="#ffffff", endColorStr="#eeeeee"); }
.swag_shadow_bottom { box-sizing: border-box; padding:2px;border:1px solid #ccc;background:#fff;background: -webkit-gradient(linear, left top, left bottom, from(#ffffff), to(#eeeeee));background: -webkit-linear-gradient(top, #ffffff 0%, #eeeeee);background: -moz-linear-gradient(top, #ffffff 0%, #eeeeee);background: -ms-linear-gradient(top, #ffffff 0%, #eeeeee);background: -o-linear-gradient(top, #ffffff 0%, #eeeeee);background: linear-gradient(top, #ffffff 0%, #eeeeee);filter: progid:DXImageTransform.Microsoft.Gradient(startColorStr="#ffffff", endColorStr="#eeeeee"); }
.swag_crosshair_vertical, .swag_crosshair_horizontal, .swag_crosshair_left, .swag_crosshair_top { background-color: gray; opacity: 0.2; filter: alpha(opacity=20); }
.swag_loading { background-color: orange; color: white; padding: 2px; }
.swag_scroll { background-color: #f3f3f3; }

.swagnavigator_main { border-left: 1px solid #ffffff;border-right: 1px solid #ffffff;border-bottom: 1px solid #ffffff; font-size: 0.875rem; }
.swagnavigator_main *, .swagnavigator_main *:before, .swagnavigator_main *:after { box-sizing: border-box; }
.swagnavigator_line { border-bottom: 1px solid #ffffff; }
/* month header */ 
.swagnavigator_title, .swagnavigator_titleleft, .swagnavigator_titleright { 
	border-top: 1px solid #ffffff;
	color: #000000;
	background: #ffa216;
  font-weight: bold;
}
.swagnavigator_title { text-align: center;  }
.swagnavigator_titleleft, .swagnavigator_titleright { text-align: center; }
/* day headers */
.swagnavigator_dayheader { 
	color: #000000;
	background: a;
	padding: 0px;
	text-align: center;
}
/* day cells */
.swagnavigator_cell { 
	color: #000000;
	background: #ffffff;
	text-align: center;
}
.swagnavigator_cell_text {
	padding: 0px;
}
.swagnavigator_weeknumber { 
	color: #000000;
	background: #f8f9fa;
	text-align: center;
	padding: 0px;
}
.swagnavigator_weekend { 
	color: #000000;
	background: #f8f9fa;
	text-align: center;
	padding: 0px;
}
.swagnavigator_dayother { color: gray; }
.swagnavigator_todaybox { border: 1px solid black; }
.swagnavigator_busy { font-weight: bold; }
.swagnavigator_select .swagnavigator_cell_box { background-color: #E2725B; opacity: 1; }



</style>