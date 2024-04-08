<script setup>
import ArgonButton from "@/argon_components/ArgonButton.vue";
</script>

<template>
  <div class="py-4 container-fluid">
    <div class="video-wrapper">
      <video controls
             autoplay
             loop
             muted
             preload="auto"
             class="video">
        <source src="@/assets/importedpng/gym-video.mp4" type="video/mp4">
      </video>
      <div class="video-overlay">
        <div class="overlay-content">
          <p class="overlay-subheader">WORK HARDER, GET STRONGER, LIVE LONGER</p>
          <h1 class="overlay-header">MADE EASY AT OUR <span class="terracotta">GYM</span></h1>
          <div class="button-container">
            <a href="/signup" style="text-decoration: none;">
              <ArgonButton style="background-color: #E2725B; color: white;">Become a Member</ArgonButton>
            </a>
          </div>
          <a href="/signin" style="text-decoration: none;">
            <ArgonButton style="background-color: white; color: #E2725B;">Log In</ArgonButton>
          </a>
        </div>
      </div>
    </div>
  </div>
</template>

<style>
.overlay-header,
.overlay-subheader {
  font-family: 'Montserrat', sans-serif;
}

.video {
  display: block;
  max-width: 100%;
  height: auto;
}

.video-wrapper {
  position: relative;
}

.video-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5); /* Adjust the opacity here */
  text-align: center;

}

.video::-webkit-media-controls {
  display: none !important;
}

.video::-webkit-media-controls-enclosure {
  display: none !important;
}

.video::-webkit-media-controls-panel {
  display: none !important;
}


.overlay-content {
  position: relative;
  top: 35%;
  width: 100%;
  height: 100%;

}

.overlay-header {
  color: white;
  font-size: 56px;
  font-weight: bold;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.overlay-subheader {
  color: white;
  font-size: 18px;
  font-weight: bold;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.terracotta {
  color: #E2725B;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.4);
  font-weight: bold;
}

.button-container {
  display: flex;
  flex-direction: column;
  align-items: center; /* Align items (buttons) horizontally at the center */
  padding: 15px;
}

</style>
