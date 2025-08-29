module.exports = {
  devServer: {
    proxy: {
      "/api": {
        target: "http://localhost:8080",
      },
    },
  },
  lintOnSave: false,
  chainWebpack: (config) => {
    // progress 플러그인 완전 제거
    config.plugins.delete("progress");

    // friendly-errors 플러그인도 제거
    config.plugins.delete("friendly-errors");

    // case-sensitive-paths 플러그인 제거
    config.plugins.delete("case-sensitive-paths");
  },
  configureWebpack: {
    plugins: [],
    stats: "minimal",
  },
};
