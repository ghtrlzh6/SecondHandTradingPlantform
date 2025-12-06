window.onload = function() {
  //<editor-fold desc="Changeable Configuration Block">

  // the following lines will be replaced by docker/configurator, when it runs in a docker-container
  window.ui = SwaggerUIBundle({
    url: "../openapi.yaml",
    dom_id: '#swagger-ui',
    deepLinking: true,
    presets: [
      SwaggerUIBundle.presets.apis,
      SwaggerUIStandalonePreset
    ],
    plugins: [
      SwaggerUIBundle.plugins.DownloadUrl
    ],
    layout: "StandaloneLayout",
    
    // 自定义配置
    docExpansion: "list", // 默认展开所有操作
    defaultModelRendering: "example", // 默认显示示例而非模型
    displayRequestDuration: true, // 显示请求持续时间
    filter: true, // 启用顶部过滤器
    syntaxHighlight: {
      activated: true,
      theme: "agate"
    },
    validatorUrl: null, // 禁用在线验证
    
    // 中文化支持
    operationsSorter: "alpha", // 按字母顺序排序操作
    tagsSorter: "alpha" // 按字母顺序排序标签
  });

  //</editor-fold>
};
