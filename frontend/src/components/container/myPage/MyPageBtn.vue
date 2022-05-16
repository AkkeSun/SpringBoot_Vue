<template>
  <div class="btn btn-dark btn-lg mb-2" @click="goMyInfo">MyInfo</div>
  <br>
  <div class="btn btn-dark btn-lg mb-2" @click="goFollower">Follower</div>
</template>

<script>
import { getCurrentInstance } from "vue";

export default {
  name: "MyPageBtn",
  setup(){
    const { proxy } = getCurrentInstance();
    const goMyInfo = () => proxy.$router.push("/myPage/" + proxy.$route.params.id + "/update");
    const goFollower = () => proxy.$router.push("/myPage/" + proxy.$route.params.id + "/follower");

    return { goMyInfo, goFollower }
  },
}
</script>
