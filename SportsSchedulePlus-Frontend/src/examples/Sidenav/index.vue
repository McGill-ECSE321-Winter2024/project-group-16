<script setup>
import {computed} from "vue";
import {useStore} from "vuex";
import SidenavList from "./SidenavList.vue";

const store = useStore();
const layout = computed(() => store.state.layout);
const sidebarType = computed(() => store.state.sidebarType);
const darkMode = computed(() => store.state.darkMode);
</script>
<template>
  <div
    v-show="layout === 'default'"
    class="min-height-300 position-absolute w-100"
    :class="`$'bg-transparent'`"
  />

  <aside
    class="my-3 overflow-auto border-0 sidenav navbar navbar-vertical navbar-expand-xs border-radius-xl"
    :class="`${'fixed-start ms-3'}
      ${
        layout === 'landing' ? 'bg-transparent shadow-none' : ' '
      } ${sidebarType}`"
    id="sidenav-main"
  >
    <div class="sidenav-header">
      <router-link class="m-0 navbar-brand" to="/">
        <span class="ms-2 font-weight-bold me-2">DASHBOARD</span>
      </router-link>
    </div>

    <hr class="mt-0 horizontal dark"/>

    <sidenav-list/>
  </aside>
</template>
