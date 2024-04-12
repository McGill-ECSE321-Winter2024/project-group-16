/* eslint no-undef: "off" */
import { createRouter, createWebHistory } from "vue-router";


import NotFoundPage from "../components/NotFoundPage.vue";
import AccessForbidden from "../components/AccessForbidden.vue";

import Dashboard from ".././components/Dashboard.vue";
import Customers from ".././components/Customers.vue";
import Instructors from ".././components/Instructors.vue";


import Profile from ".././components/Profile.vue";
import Signup from ".././components/Signup.vue";
import Signin from ".././components/Signin.vue";
import Registrations from ".././components/Registrations.vue";
import CourseTypes from ".././components/CourseTypes.vue";
import ScheduledCourses from ".././components/ScheduledCourses.vue";
import CourseRegistration from ".././components/CourseRegistration.vue";
import Classes from ".././components/Classes.vue"
const routes = [
  {
    path: "/",
    name: "/",
    redirect: "/dashboard-default",
  },
  {
    path: "/dashboard-default",
    name: "Dashboard",
    component: Dashboard,
  },
  {
    path: "/customers",
    name: "Customers",
    component: Customers,
  },
  {
    path: "/instructors",
    name: "Instructors",
    component: Instructors,
  },
  {
    path: "/courseTypes",
    name: "Course Types",
    component: CourseTypes,
  },
  {
    path: "/schedule",
    name: "Schedule",
    component: ScheduledCourses,
  },


  {
    path: "/profile",
    name: "Profile",
    component: Profile,
  },
  {
    path: "/signin",
    name: "Signin",
    component: Signin,
  },
  {
    path: "/signup",
    name: "Signup",
    component: Signup,
  },
  {
    path: "/courseregistration",
    name: "CourseRegistration",
    component: CourseRegistration,
  },
  {
  path: "/customer/registrations",
  name: "registrations",
  component: Registrations,
  },
  {
    path: "/:catchAll(.*)",
    name: "404NotFound",
    component: NotFoundPage,
  },
  {
    path: "/403",
    name: "403Forbidden",
    component: AccessForbidden,
  },
  {
    path: "/classes",
    name: "classes",
    component: Classes,
  }

];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
  linkActiveClass: "active",
});

export default router;
