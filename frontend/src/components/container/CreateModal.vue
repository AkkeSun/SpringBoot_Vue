<template>
  <div class="popupBack">
    <div class="popup">

      <div :class="newContent.checkedFilter" class="popupImage" :style="`background-image:url(${preViewUrl})`">
        <span class="closeBtn" @click="modalClose">X</span>
      </div>

      <div v-if="tab == 1">
        <FilterBox :filter="filter"/>
        <button class="btn-sm btn btn-primary mb-5 nextBtn" @click="setTab(2)">다음</button>
      </div>

      <div v-if="tab == 2">
        <textarea class="commentInput" @input="setComment($event.target.value)">내용을 입력하세요</textarea>
        <button class="btn-sm btn btn-primary mb-5 myByn" @click="saveNewContent">작성완료</button>
      </div>

    </div>
  </div>
</template>

<script>
import { computed } from "vue";
import { useStore } from "vuex";
import FilterBox from "@/components/container/FilterBox";

export default {
  name: "CreateModal",
  components:{ FilterBox },
  setup(){
    const store = useStore();
    const tab = computed(() => store.state.postStore.tab);
    const filter = computed(() => store.state.postStore.filter);
    const newContent = computed(() => store.state.postStore.newContent)
    const preViewUrl = computed(() => store.state.postStore.preViewUrl)

    const setComment = (comment) => store.commit('postStore/setComment', comment);
    const setTab = (tab) => store.commit('postStore/setTab', tab);
    const saveNewContent = () => store.dispatch('postStore/saveNewContent');
    const modalClose = () => {
      store.dispatch('postStore/setModalClose');
      store.commit('postStore/setFile', "");
    }
    return {tab, filter, newContent, preViewUrl, modalClose, setComment, setTab, saveNewContent}
  }
}
</script>

<style>
  @import '../../css/createModal.css';
</style>