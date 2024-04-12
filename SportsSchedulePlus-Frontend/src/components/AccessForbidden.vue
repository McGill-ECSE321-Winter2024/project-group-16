<template>

<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">


	<link href="https://fonts.googleapis.com/css?family=Cabin:400,700" rel="stylesheet">
	<link href="https://fonts.googleapis.com/css?family=Montserrat:900" rel="stylesheet">

	<link type="text/css" rel="stylesheet" href="css/style.css" />

	<div id="notfound">
		<div class="notfound">
			<div class="notfound-404">
				<h3>Nuh uh! Access Denied/Forbidden</h3>
				<h1><span>4</span><span>0</span><span>3</span></h1>
			</div>
			<h2>you currently do not have access to this page, Please try again later</h2>
			<div class="image-container">
				<img src="../assets/importedpng/404.png" alt="404 Not Found" />
			</div>
			<div class="btn-container">
				<ArgonButton style="background-color: #E2725B; color: white;" @click="goToDashboard">Go Home</ArgonButton>
			</div>
		</div>
	</div>


</template>

<script setup>
import ArgonButton from "@/argon_components/ArgonButton.vue";
import { useRouter } from 'vue-router';

const router = useRouter();

const goToDashboard = () => {
  router.push({ name: 'Dashboard' });
};

router.beforeEach((to, from, next) => {
  console.log('Navigation to:', to.name);
  const isLoggedIn = localStorage.getItem('loggedIn');
  console.log('isLoggedIn:', isLoggedIn);
  
  const restrictedRoutesUsers = ['Instructors', 'Coursetypes', 'Customers'];
  
  if (isLoggedIn) {
    const userData = JSON.parse(localStorage.getItem('userData'));
    console.log('userData:', userData);
    
    if (userData.role === 'Owner') {
        if (to.name === 'registrations') {
            console.log('Redirecting to 403Forbidden');
            router.push({ name: '403Forbidden' });
        } else {
            next();
        }
    } else if (userData.role === 'Instructor' || userData.role === 'Customer') {
        if (restrictedRoutesUsers.includes(to.name)) {
            console.log('Redirecting to 403Forbidden');
            router.push({ name: '403Forbidden' });
        } else {
            next();
        }
    } else {
        router.push({ name: 'Dashboard' });
    }
  } else {
        if (restrictedRoutesUsers.includes(to.name) || to.name === 'registrations' || to.name === 'Profile') {
            console.log('Redirecting to 403Forbidden');
            router.push({ name: '403Forbidden' });
        } else {
            next();
        }
    }
});

</script>

<style >
* {
  margin:0;
  padding: 0;
}
body{
  background: #233142;
  
}
.whistle{
  width: 20%;
  fill: #E2725B;
  margin: 100px 40%;
  text-align: left;
  transform: translate(-50%, -50%);
  transform: rotate(0);
  transform-origin: 80% 30%;
  animation: wiggle .2s infinite;
}

@keyframes wiggle {
  0%{
    transform: rotate(3deg);
  }
  50%{
    transform: rotate(0deg);
  }
  100%{
    transform: rotate(3deg);
  }
}
.error-code {
  margin-top: -100px;
  margin-bottom: 20px;
  color: #000000;
  text-align: center;
  font-family: 'Raleway';
  font-size: 200px;
  font-weight: 800;
}
h2{
  color: #000000;
  text-align: center;
  font-family: 'Raleway';
  font-size: 100px;
  text-transform: uppercase;
}
</style>