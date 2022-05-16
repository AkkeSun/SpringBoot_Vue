import { createStore } from "vuex";
import {postStore} from "@/store/module/postStore";
import {userStore} from "@/store/module/userStore";
import createPersistedState from 'vuex-persistedstate';


export default createStore({
    // 모듈 등록
    modules: { postStore, userStore },

    // 모듈의 변수를 localstorage 에 저장하여 관리하도록 설정 ( npm install vuex-persistedstate ) -> 새로고침해도 날라가지 않도록
    plugins: [
        createPersistedState( {paths : ['userStore']} )
    ]
});