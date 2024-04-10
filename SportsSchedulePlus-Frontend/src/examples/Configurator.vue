<script setup>
import { computed } from "vue";
import { useStore } from "vuex";

const store = useStore();

const isNavFixed = computed(() => store.state.isNavFixed);
const sidebarType = computed(() => store.state.sidebarType);
const toggleConfigurator = () => store.commit("toggleConfigurator");

const navbarFixed = () => store.commit("navbarFixed");
const setSidebarType = (type) => store.commit("sidebarType", type);

const sidebarColor = (color = "success") => {
  document.querySelector("#sidenav-main").setAttribute("data-color", color);
};
</script>
