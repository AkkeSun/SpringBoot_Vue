<template>
  <form class="mt-5 pl-5 pr-5">
    <div class="form-group">
      <div class="myLabel">User ID</div>
      <div type="text" class="form-control updateId" >{{loginSuccessData.userId}}</div>
    </div>
    <div class="form-group">
      <div class="myLabel">Password</div>
      <input type="password" @input="changePassword($event.target.value)" class="form-control" :value="loginSuccessData.password">
      <small class="form-text text-danger">{{userAlert.regiPwdAlert}}</small>
    </div>
    <div class="form-group">
      <div class="myLabel">Password Check</div>
      <input type="password" @input="changePasswordCheck($event.target.value)" class="form-control" :value="loginSuccessData.password">
      <small class="form-text text-danger">{{userAlert.regiPwdCkAlert}}</small>
    </div>
    <div class="form-group">
      <div class="myLabel">Role</div>
      <input type="text" class="form-control" @input="changeRole($event.target.value)" :value="loginSuccessData.role">
      <small class="form-text text-danger">{{userAlert.regiRoleAlert}}</small>
    </div>
    <div class="btn btn-primary mr-2" @click="$router.go(-1)">뒤로가기</div>
    <div class="btn btn-primary" @click="userRegister">UPDATE</div>
  </form>
</template>

<script>
import {useStore} from "vuex";
import {computed} from "vue";

export default {
  name: "MyPageUpdate",
  setup(){
    const store = useStore();

    const loginSuccessData = computed(() => store.state.userStore.loginSuccessData.data);
    const userAlert = computed(() => store.state.userStore.userAlert);
    const changeRole = (data) => store.commit('userStore/changeRole', data);
    const changePassword = (data) => store.commit('userStore/changePassword', data);
    const changePasswordCheck = (data) => store.commit('userStore/changePasswordCheck', data);

    return {loginSuccessData, userAlert, changeRole, changePassword, changePasswordCheck}
  }
}
</script>
<style>
.myLabel {
  text-align: left;
  font-size:15px;
  margin-bottom: 5px;
}
.updateId{
  text-align: left;
}
</style>