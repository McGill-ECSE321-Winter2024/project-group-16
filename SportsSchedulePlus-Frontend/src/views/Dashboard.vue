<script setup>
import WeeklySchedule from '../components/WeeklySchedule.vue';

</script>

<template>
  <div class="py-4 container-fluid">
    <div class="row">
      <div class="col-lg-12">
        <div class="row mt-4">
          <WeeklySchedule
            displayType='instructor'
            instructorId=3355
          />
        </div>
      </div>
    </div>
  </div>
</template>
