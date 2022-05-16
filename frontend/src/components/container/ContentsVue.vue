<template>
  <div class="contents">
    <div class="title">
      <div class="titleImage" ></div>
      <span class="titleId">{{postData.writer.userId}}</span>
    </div>

    <div v-if="modalOpen || searchData != '' " :class="`image`" :style="getImages()"></div>
    <div v-else :class="`${postData.filter} image`" :style="getImages()"></div>

    <div class="comment">
      <div class="likes">{{postData.likes}} Likes</div>
      <span class="commentId">{{postData.writer.userId}}</span>
      <span class="commentData">{{postData.comment}}</span>
    </div>
  </div>
</template>

<script>
import { useStore } from "vuex";
import { computed } from "vue";

export default {
  name: "ContentsVue",
  props: {
    postData: Object,
    zIndexVal: Number
  },
  setup(props){
    const store = useStore();
    const searchData = computed(() => store.state.userStore.searchData);
    const modalOpen = computed(() => store.state.postStore.modalOpen);
    const getImages = () =>  { return {backgroundImage : `url(${props.postData.image})`} }
    //  const getImages = () =>  { return {backgroundImage : `url(${require('upload/'+props.postData.image)})`} }
    return {modalOpen, getImages, searchData}
  },
}
</script>

<style>
  @import "../../css/content.css";
</style>