<script setup>
import {ref, computed, watch, defineEmits} from 'vue'
import {VTimePicker} from 'vuetify/labs/VTimePicker'
import ArgonButton from "@/argon_components/ArgonButton.vue";
import axios from "axios";
import {useRouter} from 'vue-router'

const daysSelected = ref([]);

let startTime = ref(null);
let endTime = ref(null);
const dialog = ref(false)
let startTimeModal = ref(false);
let endTimeModal = ref(false);
let isDaySelected = computed(() => daysSelected.value.length > 0);
const dailyScheduleDictionary = {
  "Monday": 0,
  "Tuesday": 1,
  "Wednesday": 2,
  "Thursday": 3,
  "Friday": 4,
  "Saturday": 5,
  "Sunday": 6
};
const success = ref(false);
const emit = defineEmits(['scheduleUpdated'])
watch(success, () => {
  emit('scheduleUpdated')
});


const axiosClient = axios.create({
  baseURL: "http://localhost:8080"
});

const updateDailySchedule = async () => {

  try {
    const startTimeWithSeconds = startTime.value + ":00";
    const endTimeWithSeconds = endTime.value + ":00";
    const hoursSelected = {openingTime: startTimeWithSeconds, closingTime: endTimeWithSeconds};
    for (let day of daysSelected.value) {
      await axiosClient.put(`/openingHours/${dailyScheduleDictionary[day]}`, hoursSelected);

    }
    dialog.value = false;
    daysSelected.value = [];
    startTime.value = null;
    endTime.value = null;
    success.value = true;

  } catch (error) {
    console.error('Error updating daily schedule:', error);
  }
}
const rules = {
  required: value => !!value || 'Field is required',
};
const isButtonDisabled = computed(() => {
  return daysSelected.value.length === 0 || startTime.value === null || endTime.value === null;
});
</script>


<template>
  <div class="text-center pa-4">
    <Argon-Button @click="dialog = true" style="background-color: white; color: #E2725B; border: 2px solid #E2725B; ">
      Modify Schedule
    </Argon-Button>

    <v-dialog v-model="dialog" width="50%">
      <v-card
        prepend-icon="mdi-update"
        title="Modify Opening & Closing Hours"
      >
        <v-container>
          <v-select
            v-model="daysSelected"
            label="Select Day"
            :items="['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday']"
            variant="outlined"
            style="padding: 30px 30px 10px 30px;"
            multiple
          ></v-select>
        </v-container>

        <v-text-field
          v-model="startTime"
          :active="startTimeModal"
          :focused="startTimeModal"
          :label="!isDaySelected ? 'Select Day to Enable Changes': 'Select Start Time'"
          prepend-icon="mdi-clock-time-four-outline"
          readonly
          :disabled="!isDaySelected"
          :rules="[rules.required]"

          style="padding: 10px 30px;"

        >
          <v-dialog v-model="startTimeModal" activator="parent" width="auto" height="auto">
            <v-card>
              <v-card-text>
                <v-container>
                  <v-time-picker format="24hr" :scrollable="true" v-if="startTimeModal" v-model="startTime"
                                 :allowed-minutes="[15,30,45]"
                  ></v-time-picker>
                </v-container>
              </v-card-text>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn @click="startTimeModal = false">Confirm Selection</v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </v-text-field>

        <v-text-field
          v-model="endTime"
          :active="endTimeModal"
          :focused="endTimeModal"
          :label="!isDaySelected ? 'Select Day to Enable Changes': 'Select End Time'"
          prepend-icon="mdi-clock-time-four-outline"
          readonly
          :disabled="!isDaySelected"
          style="padding: 10px 30px;"
          :rules="[rules.required]"


        >
          <v-dialog v-model="endTimeModal" activator="parent" width="auto" height="auto">
            <v-card>
              <v-card-text>
                <v-container>
                  <v-time-picker :min="startTime" format="24hr" :scrollable="true" v-if="endTimeModal" v-model="endTime"
                                 fullWidth
                                 :allowed-minutes="[15,30,45]"></v-time-picker>
                </v-container>
              </v-card-text>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn
                  @click="endTimeModal = false">Confirm Selection
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </v-text-field>

        <template #actions>
          <v-col>
            <v-btn @click="dialog = false">Cancel</v-btn>
          </v-col>
          <v-divider style="color: white;"></v-divider>
          <v-col>
            <v-btn :disabled="isButtonDisabled" @click="updateDailySchedule"
                   style="background-color: #E2725B; color:white ;">Save Changes
            </v-btn>
          </v-col>
        </template>

      </v-card>
    </v-dialog>
  </div>
</template>

