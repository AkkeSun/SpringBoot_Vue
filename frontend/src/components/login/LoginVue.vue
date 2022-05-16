<template>
  <!-- 로그인 -->
  <form v-if="loginData.loginTab==0" class="mt-5 pl-5 pr-5">
    <h1 class="loginHead mb-5">LOGIN PAGE</h1>
    <div class="form-group">
      <label>User ID</label>
      <input type="text" class="form-control" aria-describedby="emailHelp" @input="setUserId($event.target.value)">
      <small class="form-text text-muted">We'll never share your UserId with anyone else.</small>
    </div>
    <div class="form-group">
      <label>Password</label>
      <input type="password" class="form-control" @input="setPassword($event.target.value)">
    </div>
    <div class="btn btn-primary mr-2" @click="loginProcess">Login</div>
    <button class="btn btn-primary" @click="setLoginTab(1)">Register</button>
  </form>

 <!-- 회원가입 -->
  <form v-if="loginData.loginTab==1" class="mt-5 pl-5 pr-5">
    <h1 class="loginHead mb-5">REGISTER PAGE</h1>
    <div class="form-group">
      <label>User ID</label>
      <input type="text" class="form-control"  @input="setUserId($event.target.value)">
      <small class="form-text text-danger">{{userAlert.IdAlert}}</small>
    </div>
    <div class="form-group">
      <label >Password</label>
      <input type="password" @input="setPassword($event.target.value)" class="form-control">
      <small class="form-text text-danger">{{userAlert.PwdAlert}}</small>
    </div>
    <div class="form-group">
      <label>Password Check</label>
      <input type="password" @input="setPasswordCheck($event.target.value)" class="form-control">
      <small class="form-text text-danger">{{userAlert.PwdCkAlert}}</small>
    </div>
    <div class="form-group">
      <label>Role</label>
      <input type="text" class="form-control" @input="setRole($event.target.value)">
      <small class="form-text text-danger">{{userAlert.RoleAlert}}</small>
    </div>
    <button class="btn btn-primary mr-2" @click="setLoginTab(0)">LOGIN PAGE</button>
    <div class="btn btn-primary" @click="userRegister">SAVE</div>
  </form>


</template>

<script>
import { computed } from "vue";
import { useStore } from "vuex";

export default {
  name: "LoginVue",
  setup(){
    const store = useStore();

    const loginData = computed(() => store.state.userStore.loginData);
    const userAlert = computed(() => store.state.userStore.userAlert)
    const setLoginTab = (data) => store.commit('userStore/setLoginTab', data);
    const setRole = (data) => store.commit('userStore/setRole', data);
    const setUserId = (data) => store.commit('userStore/setUserId', data);
    const setPassword = (data) => store.commit('userStore/setPassword', data);
    const setPasswordCheck = (data) => store.commit('userStore/setPasswordCheck', data);
    const userRegister = () => store.dispatch('userStore/userRegister');
    const loginProcess = () => store.dispatch('userStore/loginProcess');

    return {loginData, userAlert, setLoginTab, setRole, setUserId, setPassword, setPasswordCheck, userRegister, loginProcess}
  }
}
</script>

<style>
.loginHead{
  text-align:center;
}
</style>