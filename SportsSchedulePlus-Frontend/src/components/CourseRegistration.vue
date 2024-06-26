<template>
  <div class="container mt-5">
    <div class="row justify-content-center">
      <div class="">
        <div class="border-0">
          <div class="card-body">
            <form @submit.prevent="submitForm">
              <div class="form-group">
                <label for="cardholderName">Cardholder Name</label>
                <input type="text" class="form-control" id="cardholderName" v-model="cardholderName" placeholder="Enter cardholder name" required>
                <small v-if="!isCardholderNameValid" class="text-danger">Please enter cardholder name.</small>
              </div>
              <div class="form-group">
                <label for="expirationDate">Expiration Date</label>
                <input type="text" class="form-control" id="expirationDate" v-model="expirationDate" placeholder="MM/YY" required>
                <small v-if="!isExpirationDateValid" class="text-danger">Please enter a valid expiration date (MM/YY format).</small>
              </div>
              <div class="form-group">
                <label for="creditCardNumber">Credit Card Number</label>
                <input type="text" class="form-control" id="creditCardNumber" v-model="creditCardNumber" placeholder="Enter your credit card number" required>
                <small v-if="!isCreditCardNumberValid" class="text-danger">Please enter a valid Visa or MasterCard number.</small>
                 <!-- Visa and Mastercard logos -->
                <div class="row justify-content-center mt-3">
                  <div class="col-lg-11 text-right">
                    <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/0/04/Visa.svg/1200px-Visa.svg.png" alt="Visa Logo" style="height: 30px; margin-right: 10px;">
                    <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/2/2a/Mastercard-logo.svg/1200px-Mastercard-logo.svg.png" alt="Mastercard Logo" style="height: 30px;">
                  </div>
                </div>
              </div>
              <div class="text-center">
                <button type="submit" class="btn btn-primary btn-lg px-5 mt-3 text-white">Register</button>
              </div>
            </form>
            <div class="text-center">
              <small v-if="alreadyRegistered" class="text-danger">You have already registered for this class.</small>
              <small v-if="registrationSuccess" class="text-success">You've registered, can't wait to see you there</small>
            </div>
          </div>
        </div>
      </div>
    </div>
   
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import axios from 'axios';
// Data variables for cardholder name, expiry date, and credit card number
const cardholderName = ref("");
const expirationDate = ref("");
const creditCardNumber = ref("");

// Data variables for validation flags
const isCardholderNameValid = ref(true);
const isExpirationDateValid = ref(true);
const isCreditCardNumberValid = ref(true);
let alreadyRegistered = ref(false);
let registrationSuccess = ref(false);

// Function to handle form submission
const submitForm = async () => {
  // Validate input fields
  if (!cardholderName.value) {
    isCardholderNameValid.value = false;
  }

  if (!expirationDate.value || !/^(0[1-9]|1[0-2])\/\d{2}$/.test(expirationDate.value)) {
    isExpirationDateValid.value = false;
  }

  const visaPattern = /^4[0-9]{12}(?:[0-9]{3})?$/;
  const mastercardPattern = /^(?:5[1-5][0-9]{2}|222[1-9]|22[3-9][0-9]|2[3-6][0-9]{2}|27[01][0-9]|2720)[0-9]{12}$/;
  const formattedNumber = creditCardNumber.value.replace(/\D/g, '');

  if (!(visaPattern.test(formattedNumber) || mastercardPattern.test(formattedNumber))) {
    isCreditCardNumberValid.value = false;
  }

  // Computed properties for validation messages
  const cardholderNameErrorMessage = computed(() => !isCardholderNameValid.value ? "Please enter cardholder name." : "");
  const expirationDateErrorMessage = computed(() => !isExpirationDateValid.value ? "Please enter a valid expiration date (MM/YY format)." : "");
  const creditCardNumberErrorMessage = computed(() => !isCreditCardNumberValid.value ? "Please enter a valid Visa or MasterCard number." : "");

  // Computed property for overall form validity
  const isFormValid = computed(() => isCardholderNameValid.value && isExpirationDateValid.value && isCreditCardNumberValid.value);
  const axiosClient = axios.create({
    baseURL: "http://localhost:8080"
  });

  if (isFormValid.value == true && !alreadyRegistered.value){
    console.log("Valid form");
    const scheduledCourseId = parseInt(localStorage.getItem("scheduledCourseId"));
    const userData = JSON.parse(localStorage.getItem("userData"));
    let userID = parseInt(userData.id);
    try {
      await axiosClient.post("/registrations/" + userID + "/" + scheduledCourseId);
      console.log("Registration successful");
      alreadyRegistered.value = false;
      registrationSuccess.value = true;
      location.reload();
    } catch (error) {
      console.log(error);
      alreadyRegistered.value = true;
      registrationSuccess.value = false;
    }
  }
};
</script>

<style scoped>
.card {
  transition: transform 0.3s;
}
</style>
