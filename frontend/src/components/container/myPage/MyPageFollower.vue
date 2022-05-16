<template>
  <table class="table pl-5 pr-5">
    <thead>
    <tr>
      <th scope="col">#</th>
      <th scope="col">Follower</th>
    </tr>
    </thead>
    <tbody>
    <tr v-for="(follower, i) in followerList" :key="i">
      <th scope="row">{{i+1}}</th>
      <td>{{follower.content}}</td>
    </tr>
    </tbody>
  </table>
  <button class="btn btn-primary" @click="$router.go(-1)">이전</button>
</template>

<script>
import { onMounted, ref } from 'vue'
import { useStore } from "vuex";

export default {

  name: "MyPageFollower",
  setup(){
    const store = useStore();
    const followerList = ref([]);

    const getFollowList = () => {
      let list = store.dispatch("userStore/getFollowList");
      list.then(res => {
        console.log(res.data);
        followerList.value = res.data;
      })
    }


    onMounted( () =>getFollowList());

    return { followerList, getFollowList }
  },
}
</script>

<style >

</style>