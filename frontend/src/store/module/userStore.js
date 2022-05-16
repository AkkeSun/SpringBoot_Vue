
import axios from 'axios';
import router from "@/router/router";

export const userStore = {
    namespaced: true,

    // data 정의
    state() {
        return {

            // 로그인 성공시 저장되는 데이터
            loginSuccessData: null,

            // 로그인 및 회원가입에 사용되는 데이터
            loginData: {
                loginTab: 0,
                role: '',
                userId: '',
                password: '',
                passwordCheck: '',
            },

            // 신규가입, 업데이트에 사용되는 경고메세지
            userAlert: {
                IdAlert: '',
                PwdAlert: '',
                PwdCkAlert: '',
                RoleAlert: '',
            },

            // 유저 검색내용을 저장
            searchData : "",
            findUser: [],
        }
    },

    // 데이터 수정 방법 정의
    mutations: {
        LoginDataInitialize(state){
            state.loginData.userId = '';
            state.loginData.password = '';
            state.loginData.passwordCheck = '';
            state.loginData.role = '';
            state.loginData.loginTab = 0;
        },
        setLoginTab(state, payload){
            state.loginData.userId        = '';
            state.loginData.password      = '';
            state.loginData.passwordCheck = '';
            state.loginData.role          = '';
            state.loginData.loginTab      = payload;
            state.userAlert.IdAlert       = '';
            state.userAlert.PwdAlert      = '';
            state.userAlert.PwdCkAlert    = '';
            state.userAlert.RoleAlert     = '';
        },
        setRole(state, payload){
            state.loginData.role = payload;
        },
        setPasswordCheck(state, payload){
            state.loginData.passwordCheck = payload;
        },
        setUserId(state, payload){
            state.loginData.userId = payload;
        },
        setPassword(state, payload){
            state.loginData.password = payload;
        },
        setIdAlert(state, payload){
            state.userAlert.IdAlert = payload;
        },
        setPwdAlert(state, payload){
            state.userAlert.PwdAlert = payload;
        },
        setPwdCkAlert(state, payload){
            state.userAlert.PwdCkAlert = payload;
        },
        setRoleAlert(state, payload){
            state.userAlert.RoleAlert = payload;
        },
        setLoginSuccessData(state, payload){
            state.loginSuccessData = payload;
        },
        setFindUserData(state, payload){
            state.findUser = payload;
        },
        setSearchData(state, payload){
            state.searchData = payload
        },
        alertSerialize(state) {
            state.userAlert.IdAlert = '';
            state.userAlert.PwdAlert = '';
            state.userAlert.PwdCkAlert = '';
            state.userAlert.RoleAlert = '';
        },
    },


    // 비동기 처리
    actions:{

        // ================== 신규가입 (최종) ===================
        userRegister( {state, dispatch, commit} ) {

            let loginData = state.loginData;

            // 1. 입력 유무 체크
            let nullCheckResult = false;
            commit('alertSerialize');

            if (loginData.userId == ''){
                commit('setIdAlert', '아이디를 입력하세요');
            }
            if (loginData.password == ''){
                commit('setPwdAlert', '비밀번호를 입력하세요');
            }
            if (loginData.passwordCheck == ''){
                commit('setPwdCkAlert', '비밀번호를 확인을 입력하세요');
            }
            if (loginData.role == ''){
                commit('setRoleAlert', '권한을 입력하세요');
            }
            if(loginData.userId != '' && loginData.password != '' && loginData.passwordCheck != '' && loginData.role != '')
                nullCheckResult = true;

            if(nullCheckResult)
            {
                // 2. Id 중복 체크
                dispatch('idDuplicateCheck').then( res => {

                    if(res.data.content == false)
                    {
                        commit('setIdAlert', '중복된 아이디가 존재합니다');
                    }
                    else
                    {
                        // 3. 비밀번호 & 비밀번호 확인 체크
                        if (loginData.password != loginData.passwordCheck)
                        {
                            commit('setPwdCkAlert', '비밀번호와 비밀번호 확인이 다릅니다');
                        }
                        else
                        {
                            // 4. db save
                            dispatch('saveNewUser').then( () => {
                                commit('LoginDataInitialize');
                                alert('성공적으로 가입되었습니다');
                                router.push('/login');
                            })
                        }
                    }
                })
            }
        },



        // ================== ID 중복 체크 (ajax) ===================
        idDuplicateCheck({state}){
            let formData = new FormData;
            formData.append("userId", state.loginData.userId);

            let callback = axios.post(`${process.env.VUE_APP_API_URL}/api/user/idCheck`, formData);
            return callback;
        },



        // ================== 신규등록 (ajax) ===================
        saveNewUser({state}){
            let callback = axios.post(`${process.env.VUE_APP_API_URL}/api/user`,
                                       JSON.stringify(state.loginData),
                                       {headers:{'content-Type' : 'application/json'}});
            return callback;
        },


        // ================== 로그인 (ajax) ===================
        loginProcess({state, commit}) {
            axios.post(`${process.env.VUE_APP_API_URL}/api/user/login`, JSON.stringify(state.loginData), {headers:{'content-Type' : 'application/json'}})
                .then(res =>{
                    commit('setLoginSuccessData', res);
                    commit('LoginDataInitialize');
                    router.push("/");
                })
                .catch(() => {
                    alert('아이디 혹은 비밀번호가 올바르지 않습니다');
                })
        },


        // ================== User Id 검색 ===================
        findUserProcess({commit, state}, payload){
            if(payload != ""){

                let loginId = state.loginSuccessData.data.userId

                axios.get( `${process.env.VUE_APP_API_URL}/api/user/search`, { params:{searchId:payload, targetId:loginId} } )
                    .then( res => {
                        commit('setFindUserData', res.data);
                    })
                    .catch(() => commit('setFindUserData', []))
            }
        },


        // ================== 팔로우 & 언팔로우 처리 ===================
        followProcess(context, req){
            switch(req.type) {
                case "follow" :
                    axios.post(`${process.env.VUE_APP_API_URL}/api/follow`, JSON.stringify(req), {headers: {"Content-Type": `application/json`}})
                        .catch(err => alert(err));
                    break;
                case "unFollow" :
                    axios.delete(`${process.env.VUE_APP_API_URL}/api/follow`, {data:{targetId : req.targetId, followingId: req.followingId}})
                        .catch(err => alert(err));
                    break;
            }
        },


        // ================== 유저의 팔로우 리스트 가져오기  ===================
        getFollowList({state}){
            let callback = axios.get(`${process.env.VUE_APP_API_URL}/api/follow`, {params : {targetId : state.loginSuccessData.data.userId}});
            return callback;
        },


        // ==================  postStore 에 loginUserId 를 전달하기 위한 action ===================
        getLoginUserId: {
            root:true,
            handler({ state }) {
               return state.loginSuccessData.data.userId;
            }
        }

    }
}
