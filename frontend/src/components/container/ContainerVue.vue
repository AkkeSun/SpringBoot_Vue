<template>

  <HeaderVue/>
  <CreateModal v-if="modalOpen"/>
  <ContentsVue v-for="postData in postList" :key="postData.id" :postData="postData"/>

</template>

<script>
import ContentsVue from "@/components/container/ContentsVue";
import CreateModal from "@/components/container/CreateModal";
import HeaderVue from "@/components/container/HeaderVue";

import { computed, onBeforeUnmount, onBeforeMount } from "vue";
import { useStore } from "vuex";

export default {
  name: "ContainerVue",
  components: { HeaderVue, ContentsVue, CreateModal },

  setup() {
    const store = useStore();
    const modalOpen = computed(() => store.state.postStore.modalOpen);
    const postList = computed(() => store.state.postStore.postList);

    const addPostFunc = () => {
        if((window.innerHeight + window.scrollY) >= document.body.offsetHeight)
          store.dispatch('postStore/addPostList');
    };

    onBeforeMount ( () => {
      store.commit('postStore/setPage', 0);
      store.commit('userStore/setSearchData',"");
      store.dispatch('postStore/getPostList');
      window.addEventListener('scroll', addPostFunc)
    });

    onBeforeUnmount(() => {
      window.removeEventListener('scroll', addPostFunc)
    });

    return {modalOpen, postList}
  },
}
</script>

<style>
@import "../../css/createModal.css";
</style>