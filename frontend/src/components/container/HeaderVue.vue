<template>

  <nav class="navbar navbar-expand-lg navbar-light bg-light pl-5 pr-5">
    <a class="navbar-brand ml-5" href="/">INSTAGRAM</a>
    <button ref='btn' class="navbar-toggler mr-5" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
      <ul class="navbar-nav mr-auto">
        <form class="form-inline my-2 my-lg-0">
          <input class="form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search" :value="searchData" @input="findUserProcess($event.target.value)">
        </form>
        <li class="nav-item">
          <div class="nav-link" @click="$router.push('/')">Home</div>
        </li>
        <li class="nav-item">
          <input type="file" ref="file" hidden @change="fileInputFunc">
          <div class="nav-link" @click="fileInputOpen">Create</div>
        </li>
        <li class="nav-item">
          <div class="nav-link" @click="goMyPage">MyPage</div>
        </li>
        <li class="nav-item">
          <div class="nav-link" @click="$router.push('/admin')">ADMIN</div>
        </li>
        <li class="nav-item">
          <div class="nav-link" @click="logoutProcess">Logout</div>
        </li>
      </ul>
    </div>
  </nav>

  <!-- 검색결과창 -->
  <div v-if="searchData != ''" class="back">
    <div class="searchResult">
      <UserSearch v-for="(data, i) in findUser" :key="i"
                  :searchData="data" :loginSuccessData="loginSuccessData"
                  @followFunc="followChange"/>
    </div>
  </div>
</template>

<script>
import { getCurrentInstance, computed} from "vue";
import { useStore } from "vuex";
import UserSearch from "@/components/container/UserSearch";

export default {
  name: "HeaderVue",
  components:{UserSearch},
  setup(){
    const store = useStore();
    const { proxy } = getCurrentInstance();
    const searchData = computed(() => store.state.userStore.searchData)
    const modalOpen = computed(() => store.state.postStore.modalOpen);
    const loginSuccessData = computed(() => store.state.userStore.loginSuccessData);
    const findUser = computed(() => store.state.userStore.findUser);
    const findUserProcess = (userId) => {
      store.commit('userStore/setSearchData' ,userId);      // "" 이면 검색내용 가린다
      store.dispatch('userStore/findUserProcess', userId); // 검색내용 ajax 로드
    }

    const logoutProcess = () => {
        store.commit('userStore/setLoginSuccessData', null);
        store.commit('userStore/LoginDataInitialize' );
        store.commit("postStore/setPostList", []);
        store.commit('postStore/setUserId', "");
        proxy.$router.push('/login');
    }

    const fileInputOpen = () => {
      proxy.$refs.file.value = "";
      proxy.$refs.file.click();
    }

    const fileInputFunc = (input) => {

      let file = input.target.files;
      let maxSize = 10 * 1024 * 1024; // 10MB
      let fileSize = file.size;

      if (fileSize > maxSize)
      {
        alert("첨부파일 사이즈는 50MB 이내로 등록 가능합니다.");
        return;
      }
      else
      {
        proxy.$store.dispatch('postStore/fileInputFunc', file);
        proxy.$refs.btn.click(); // navbar 끄기
      }
    };

    const goMyPage = () => {
      proxy.$refs.btn.click();// navbar 끄기
      proxy.$router.push('/myPage/' + loginSuccessData.value.data.userId);
    }

    const followChange = () => {
      setTimeout(() => findUserProcess(searchData.value), 100);
    }

    return {searchData, modalOpen, loginSuccessData, findUser,
            fileInputOpen, followChange, fileInputFunc, logoutProcess, goMyPage, findUserProcess}
  }
}
</script>

<style>
.nav-link{
  cursor: pointer;
}
.back{
  width:100%; height:100%;
  background: rgba(0,0,0,0.5);
  position: fixed;
  padding:20px;
}
.searchResult{
  padding-left:20px;
  padding-top:20px;
  font-size:20px;
  position:absolute;
  transform: translate(-50%, -200%);
  top:50%; left:50%;
  width:300px;
  height:200px;
  background-color:white;
}

</style>