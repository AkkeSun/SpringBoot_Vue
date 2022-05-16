
<template>
  <ul class="popupFilter" >
    <li :class="`${filterItem} filterBox`" v-for="(filterItem, i) in filter" :key="i"
        :style="`background-image:url(${preViewUrl})`"
        @click="setCheckedFilter(filterItem)">
    </li>
  </ul>
</template>

<script>
import { computed } from "vue";
import { useStore } from "vuex";

export default {
  name: "FilterBox",
  props:{ filter:Array },
  setup(){
    const store = useStore();
    const preViewUrl = computed(() => store.state.postStore.preViewUrl);
    const setCheckedFilter = (item) => store.commit('postStore/setCheckedFilter', item);
    return { preViewUrl, setCheckedFilter }
  },
}
</script>