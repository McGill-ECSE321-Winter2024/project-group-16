<template>
  <div class="text-center pa-4">
    <Argon-Button @click="dialog = true" style="background-color: white; color: #E2725B; border: 2px solid #E2725B; ">
      Modify Schedule
    </Argon-Button>

    <v-dialog v-model="dialog" width="auto">
      <v-card
        max-width="800"
        prepend-icon="mdi-update"
        title="Modify Opening & Closing Hours"
      >
        <v-select
          v-model="daysSelected"
          label="Select Day"
          :items="['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday']"
          variant="outlined"
          style="padding: 30px 30px 10px 30px;"
          multiple
        ></v-select>

        <v-text-field
          v-model="startTime"
          :active="modal"
          :focused="modal"
          :label="!isDaySelected ? 'Select Day to Enable Changes': 'Select Start Time'"
          prepend-icon="mdi-clock-time-four-outline"
          readonly
          :disabled="!isDaySelected"
          style="padding: 10px 30px;"
        >
          <v-dialog v-model="modal" activator="parent" width="auto" height="auto">
            <v-card>
              <v-card-text>
                <v-container>
                  <v-time-picker format="24hr" :scrollable="true" v-if="modal" v-model="startTime"
                                 :allowed-minutes="[15,30,45]"></v-time-picker>
                </v-container>
              </v-card-text>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn @click="modal = false">Confirm Selection</v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </v-text-field>

        <v-text-field
          v-model="endTime"
          :active="modal2"
          :focused="modal2"
          :label="!isDaySelected ? 'Select Day to Enable Changes': 'Select End Time'"
          prepend-icon="mdi-clock-time-four-outline"
          readonly
          :disabled="!isDaySelected"
          style="padding: 10px 30px;"
        >
          <v-dialog v-model="modal2" activator="parent" width="auto" height="auto">
            <v-card>
              <v-card-text>
                <v-container>
                  <v-time-picker :min="startTime" format="24hr" :scrollable="true" v-if="modal2" v-model="endTime"
                                 fullWidth
                                 :allowed-minutes="[15,30,45]"></v-time-picker>
                </v-container>
              </v-card-text>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn @click="modal2 = false">Confirm Selection</v-btn>
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
            <v-btn @click="dialog = false" style="background-color: #E2725B; color:white ;">Save Changes</v-btn>
          </v-col>
        </template>

      </v-card>
    </v-dialog>
  </div>
</template>

<script setup>
import {ref, computed} from 'vue'
import {VTimePicker} from 'vuetify/labs/VTimePicker'
import ArgonButton from "@/argon_components/ArgonButton.vue";

const daysSelected = ref([]);
const isDaySelected = computed(() => daysSelected.value.length > 0);

const dialog = ref(false)
let startTime = ref(null);
let endTime = ref(null);
let modal = ref(false);
let modal2 = ref(false);


</script>

<style>

</style>

