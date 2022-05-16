<template>
  <div>
    {{searchData.searchId}}
    <!-- 팔로우 했다면 언팔로우 버튼을 출력 -->
    <span v-if="!itsMe">
      <span v-if="followResult"  class="btn btn-sm btn-danger folBtn" @click="followBtn('unFollow')">UnFollow</span>
      <span v-if="!followResult" class="btn btn-sm btn-primary folBtn" @click="followBtn('follow')">Follow</span>
    </span>
  </div>
</template>

<script>
import {ref, toRefs, onUpdated, getCurrentInstance} from 'vue';
import { useStore } from "vuex";

export default {
  name: "UserSearch",
  props:{searchData : Object, loginSuccessData : Object},
  setup(props) {
    let { loginSuccessData, searchData } = toRefs(props);
    const {proxy} = getCurrentInstance();
    const store = useStore();
    const followResult = ref(false); // 팔로우 상태
    const itsMe = ref(false);        // 검색대상이 자기 자신인지 확인

    // 팔로우 유무 체크
    const followCheck = () => {
      followResult.value = searchData.value.followCheck;
    };

    // 검색 대상이 자기 자신인지 확인
    const itsMeCheckFunc = () => {
      let loginId = loginSuccessData.value.data.userId;
      let targetId = searchData.value.searchId;
      if(loginId == targetId)
        itsMe.value = true;
    };

    // 팔로우 & 언팔로우 버튼 클릭
    const followBtn = (type) => {

      let req = {
        targetId :  loginSuccessData.value.data.userId,
        followingId : searchData.value.searchId,
        type : type
      };

      store.dispatch('userStore/followProcess', req).then( () => {
        proxy.$emit('followFunc'); //  follow 유무 리로드를 위해 부모 컴포넌트(HeaderVue) 에서 유저검색 재시도
      });
    };

    onUpdated( () => { followCheck(); itsMeCheckFunc();});

    return { followResult, followBtn, itsMe }
  }
}

</script>

<style>
.folBtn{
  margin-left:30px;
}
</style>