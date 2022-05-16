import { createWebHistory, createRouter } from "vue-router";

// 라우팅에 사용되는 컨테이너 로드
import ContainerVue from "@/components/container/ContainerVue.vue";
import MyPage from "@/components/container/myPage/MyPage.vue";
import MyPageBtn from "@/components/container/myPage/MyPageBtn.vue";
import MyPageUpdate from "@/components/container/myPage/MyPageUpdate.vue";
import MyPageFollower from "@/components/container/myPage/MyPageFollower.vue";
import NotFound from "@/components/errPage/NotFound";
import LoginVue from "@/components/login/LoginVue";
import AuthErr from "@/components/errPage/AuthErr";
import AdminVue from "@/components/admin/AdminVue";
import store from "@/store/index.js";

const routes = [

    {
        path: "/",
        component: ContainerVue,
        meta: { authorization: [] } // 로그인 필요한곳에 붙이기 : 접근권한 설정 [] => 누구나 접근 가능
    },
    {
        path: "/myPage/:id",
        component: MyPage,
        children : [
            { path : '', component: MyPageBtn },              // localhost:8080/sun
            { path : 'update', component: MyPageUpdate },     // localhost:8080/sun/update
            { path : 'follower', component: MyPageFollower }, // localhost:8080/sun/follower
        ],
        meta: { authorization: [] }
    },
    {
        path:'/admin',
        component: AdminVue,
        meta: { authorization: ['admin'] }
    },
    {
        path: '/login',
        component: LoginVue,
        beforeEnter : function(to, from, next){
            if(store.state.userStore.loginSuccessData != null)
                next('/');
            next();
        }
    },
    {
        path: '/:pathMatch(.*)*',
        redirect: "/notFound"
    },
    {
        path: '/notFound',
        component: NotFound
    },
    {
        path: '/authErr',
        component: AuthErr
    },
];



// make Router
const router = createRouter({
    history: createWebHistory(),
    routes
});



// 로그인 체크 인터셉터
router.beforeEach((to, from, next) => {

    // 목적한 페이지의 접근 권한정보
    const { authorization } = to.meta;

    // 로그인정보
    const loginData = store.state.userStore.loginSuccessData;

    // 로그인이 필요한데 로그인하지 않았다면
    if(authorization){

        // 로그인하지 않았다면
        if(loginData == null)
            next('/login');
        // 권한이 필요한데 권한이 올바르지 않다면
        else if(authorization.length && !authorization.includes(loginData.data.role))
            next("/authErr");
    }

    // 걸리는게 없다면 진행
    next();
})



// return
export default router;
