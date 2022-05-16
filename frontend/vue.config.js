const path = require('path');

module.exports = {
  outputDir: "../src/main/resources/static",  // 빌드 타겟 디렉토리

  /**
   * server Port 와 client port는 같을 수 없으므로 client port는 8081로 설정될것이다
   * 개발하고 있을 때에는 server와 client를 모두 돌리고 배포할 땐 server만 돌리자
   */
  devServer: {
    proxy: process.env.VUE_APP_API_URL
  },

  // alias setting
  configureWebpack: {
    resolve: {
      alias: {
        'upload': path.resolve(`'${process.env.uploadPath}'`, '/upload'),
      }
    }
  }
};