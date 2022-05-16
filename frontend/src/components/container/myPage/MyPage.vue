<template>
  <HeaderVue/>

  <div class="myPage">
    <h3>Hello MyPage</h3>
    <router-view/>
  </div>
</template>

<script>
import HeaderVue from "@/components/container/HeaderVue";
export default {
  name: "MyPage",
  components: {HeaderVue}
}
</script>

<style>
.myPage{
  text-align: center;
  margin-top:20px;
  margin-bottom:20px;
}
</style>