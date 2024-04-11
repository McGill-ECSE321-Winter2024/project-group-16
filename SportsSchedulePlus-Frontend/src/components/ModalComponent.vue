<!-- ModalComponent.vue -->
<template>
  <div v-if="show" class="modal">
    <div class="modal-content">
      <span class="close" @click="close">&times;</span>
      <slot></slot>
    </div>
  </div>
</template>

<script setup>
import { ref, defineProps, defineEmits } from 'vue';

const props = defineProps({
  show: Boolean
});

const emits = defineEmits(['update:show']);

const close = () => {
  emits('update:show', false);
};
</script>

<style scoped>
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
  background-color: rgba(0, 0, 0, 0.5); /* Dim the background */
}

.modal-content {
  bbackground-color: #fff;
  padding: 20px;
  border-radius: 8px; /* Rounded corners */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* Shadow for depth */
  width: 30%; /* Adjust width as necessary */
  max-width: 450px; /* Adjust max width as necessary */
  margin: auto; /* For centering if not using flexbox in .modal */
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


</style>
