import axios from 'axios';

export const postStore = {
    namespaced: true, // 로드할 때 namespace 사용

    // data 정의
    state(){
        return {
            modalOpen : false,
            tab : 1,
            preViewUrl : '',

            postList: [],
            page : 0,

            newContent : {
                image: '',
                comment : '',
                likes: '',
                file : '',
                userId: '',
                checkedFilter: ''
            },

            filter: [ "aden", "_1977", "brannan", "brooklyn", "clarendon", "earlybird", "gingham", "hudson",
                      "inkwell", "kelvin", "lark", "lofi", "maven", "mayfair", "moon", "nashville", "perpetua",
                      "reyes", "rise", "slumber", "stinson", "toaster", "valencia", "walden", "willow", "xpro2"],
        }
    },

    // 데이터 수정 방법 정의
    mutations: {
        setModalOpen(state){
            state.modalOpen = true;
        },
        setModalClose(state){
            state.modalOpen = false;
        },
        setTab(state, payload){
            state.tab = payload;
        },
        setPreViewUrl(state, payload) {
            state.preViewUrl = payload;
        },
        setFile(state, payload) {
            state.newContent.file = payload;
        },
        setUploadFileName(state, payload) {
            state.newContent.image = payload;
        },
        setComment(state, payload) {
            state.newContent.comment = payload;
        },
        setPage(state, payload) {
            state.page = payload;
        },
        setCheckedFilter(state, payload){
            state.newContent.checkedFilter = payload;
        },
        setPostList(state, payload){
            state.postList = payload;
        },
        addPostList(state, payload){
            state.postList = [
                ...state.postList,
                ...payload
            ];
        },
        setNewContentUserId(state, payload){
            state.newContent.userId = payload;
        },
        setUserId(state, payload){
            state.newContent.userId = payload;
        },
    },


    // 비동기 처리
    actions:{

        fileInputFunc({commit}, payload){
            let url = URL.createObjectURL(payload[0]);
            commit('setFile', payload);
            commit('setPreViewUrl', url);
            commit('setModalOpen');
        },



        setModalClose({commit}){
            commit('setModalClose');
            commit('setTab', 1);
        },



        newContentInitialize({commit}){
            commit('setUploadFileName', '');
            commit('setFile', '');
            commit('setCheckedFilter', '');
            commit('setPreViewUrl', '');
            commit('setComment', '');
        },



        saveNewContent({state, commit, dispatch}){
            let formData = new FormData;
            formData.append("file", state.newContent.file[0]);

            // 파일 업로드
            axios.post(`${process.env.VUE_APP_API_URL}/api/aws/upload`, formData, {headers:{'content-Type' : 'multipart/form-data'}} )
                 .then(obj => {

                     // userStore 에 저장된 로그인 아이디 가져오기
                     dispatch('getLoginUserId', null, { root: true }).then(data => {

                         commit('setNewContentUserId', data);
                         commit('setUploadFileName', obj.data.fileName);

                         // DB Save
                         axios.post(`${process.env.VUE_APP_API_URL}/api/post`, JSON.stringify(state.newContent), {headers:{'content-Type' : 'application/json'}})
                             .then( () => {
                                 dispatch('newContentInitialize');
                                 dispatch('setModalClose');
                                 dispatch('getPostList');
                                 commit('setPreViewUrl', "");
                             })
                     })
                 });
        },



        // 로그인 & 데이터업데이트 시 최초 데이터 출력
        getPostList({state, commit, dispatch}){

            // userStore 에 저장된 로그인 아이디 가져오기
            dispatch('getLoginUserId', null, { root: true }).then(data => {
                // ajax 통신
                axios.get(`${process.env.VUE_APP_API_URL}/api/post`, {params:{id:data, page:state.page}})
                    .then( obj => {
                        if(obj.data.page.totalElements == 0)
                            console.log('not Post List');
                        else{
                            commit('setPostList', obj.data._embedded.postList);
                        }
                    })
                    .catch(err => {
                        console.log(err);
                    })
            })
        },



        // 스크롤 맨 하단으로 이동시 추가 데이터 로드
        addPostList({state, commit, dispatch}){

            // userStore에 저장된 로그인 아이디 가져오기
            dispatch('getLoginUserId', null, { root: true }).then(data => {

                commit('setPage', state.page+1);
                axios.get(`${process.env.VUE_APP_API_URL}/api/post`, {params:{id:data, page:state.page}})
                    .then( obj => {
                        if(obj.data._embedded != null){
                            commit('addPostList', obj.data._embedded.postList);
                        }
                    });
            })
        },
    }
}
