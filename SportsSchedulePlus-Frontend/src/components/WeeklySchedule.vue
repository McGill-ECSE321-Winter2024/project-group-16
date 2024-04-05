<!-- <script setup>
// defineProps({
//     // possible values: 'all', 'customer', 'instructor', 'courseType'
//     displayType: {
//         type: String,
//         required: false,
//     },
//     customerId: {
//         type: Number,
//         required: false,
//         default: 0
//     },
//     instructorId: {
//         type: Number,
//         required: false,
//         default: 0
//     },
//     courseTypeId: {
//         type: Number,
//         required: false,
//         default: 0
//     },
// });
</script> -->

<template>
    <div class="row">
          <div class="col-12">
            <div class="card">
              <div class="card-header pb-0">
                <h6>Week of {{ todayFormatted }}</h6>
              </div>
              <div class="wrap">
                <div class="left">
                  <DayPilotNavigator id="nav" :config="navigatorConfig" />
                </div>
                <div class="content">
                  <DayPilotCalendar id="dp" :config="config" ref="calendar" />
                </div>
              </div>
            </div>
          </div>
        </div>
</template>


<script>
import axios from 'axios';
import {DayPilotCalendar, DayPilotNavigator} from '@daypilot/daypilot-lite-vue';

export default {
  name: 'Calendar',
  data: function() {
    const today = new Date().toISOString().split('T')[0];
    return {
      todayFormatted: new Date().toLocaleDateString('en-US', { month: 'long', day: 'numeric', year: 'numeric' }),
      navigatorConfig: {
        showMonths: 1,
        skipMonths: 1,
        selectMode: "Week",
        startDate: today,
        selectionDay: today,
        theme: "swagnavigator",
        onTimeRangeSelected: args => {
          this.config.startDate = args.day;
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
        eventClickHandling: "Disabled",
        eventResizeHandling: "Disabled",
        businessBeginsHour: 8,
        businessEndsHour: 18,
        heightSpec: "BusinessHoursNoScroll",
      },
    };
  },
  mounted() {
    this.loadScheduledCourses();
  },
  props: {
    displayType: {
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
        console.log(events);
      } catch (error) {
        console.error('Error loading classes: ', error);
      }
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


.swag_main 
{
	border: 1px solid #ffffff;
}
.swag_main, .swag_main td 
{
	font-size: 0.875rem;
  text-transform: uppercase;
}
.swag_event { 
	color: #000;
  font-weight: 600;
	-moz-border-radius: 5px;
	-webkit-border-radius: 5px;
	border-radius: 5px;
}
.swag_event_inner { 
	position: absolute;
	overflow: hidden;
	left: 0px;
	right: 0px;
	top: 0px;
	bottom: 0px;
	margin: 0px;
	background-color: #f8f9fa;
	-moz-border-radius: 5px;
	-webkit-border-radius: 5px;
	border-radius: 5px;
	padding: 2px;
	padding-left: 6px;
	border: 1px solid #000;
  border-radius: 1rem;
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
.swag_colheader_inner
{
	text-align: center; 
	padding: 2px;
	position: absolute;
	top: 0px;
	left: 0px;
	bottom: 0px;
	right: 0px;
	border-right: 1px solid #ffffff;
	border-bottom: 1px solid #ffffff;
	color: #000000;
	background: #f8f9fa;
}
.swag_rowheader_inner
{
	font-size: 16pt;
	text-align: right; 
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
	background: #f8f9fa;
}
.swagnavigator_title { text-align: center; }
.swagnavigator_titleleft, .swagnavigator_titleright { text-align: center; }
/* day headers */
.swagnavigator_dayheader { 
	color: #000000;
	background: #f8f9fa;
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
.swagnavigator_select .swagnavigator_cell_box { background-color: #2dce89; opacity: 1; }

</style>