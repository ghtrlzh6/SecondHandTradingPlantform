window.onload = function() {
  //<editor-fold desc="Changeable Configuration Block">

  // the following lines will be replaced by docker/configurator, when it runs in a docker-container
  window.ui = SwaggerUIBundle({
    url: "../openapi-v2.yaml",
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
    docExpansion: "list", // 默认展开所有标签操作列表
    defaultModelsExpandDepth: 3, // 默认展开模型深度
    defaultModelExpandDepth: 3, // 默认展开示例深度
    defaultModelRendering: "example", // 默认显示示例而非模型
    displayOperationId: false, // 不显示operationId
    displayRequestDuration: true, // 显示请求持续时间
    filter: true, // 启用顶部搜索/过滤器
    showExtensions: true, // 显示扩展
    showCommonExtensions: true, // 显示通用扩展
    syntaxHighlight: {
      activated: true,
      theme: "monokai" // 代码高亮主题：agate, arta, monokai, nord, obsidian等
    },
    tryItOutEnabled: true, // 默认启用"Try it out"
    requestSnippetsEnabled: true, // 启用请求代码片段
    validatorUrl: null, // 禁用在线验证器
    
    // 中文化和排序支持
    operationsSorter: "alpha", // 按字母顺序排序操作: alpha, method
    tagsSorter: "alpha", // 按字母顺序排序标签
    
    // 支持的提交内容类型
    supportedSubmitMethods: ['get', 'post', 'put', 'delete', 'patch', 'options', 'head'],
    
    // 持久化授权
    persistAuthorization: true,
    
    // 请求拦截器
    requestInterceptor: (request) => {
      // 可以在这里添加自定义请求头或修改请求
      console.log('API Request:', request);
      return request;
    },
    
    // 响应拦截器
    responseInterceptor: (response) => {
      // 可以在这里处理响应
      console.log('API Response:', response);
      return response;
    },
    
    // 自定义渲染
    onComplete: () => {
      console.log('Swagger UI 加载完成');
      
      // 添加版本信息和更新时间
      const infoSection = document.querySelector('.information-container .info');
      if (infoSection) {
        const versionInfo = document.createElement('div');
        versionInfo.className = 'version-info';
        versionInfo.style.cssText = 'margin-top: 20px; padding: 15px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; border-radius: 8px; text-align: center;';
        versionInfo.innerHTML = `
          <p style="margin: 0; font-size: 14px;">
            📅 最后更新: ${new Date().toLocaleDateString('zh-CN', { year: 'numeric', month: 'long', day: 'numeric' })}
          </p>
          <p style="margin: 5px 0 0 0; font-size: 12px; opacity: 0.9;">
            © 2025 校园书友会. 如有问题请联系技术支持.
          </p>
        `;
        infoSection.appendChild(versionInfo);
      }
      
      // 确保页脚显示
      setTimeout(() => {
        const footer = document.querySelector('.api-footer');
        if (footer) {
          footer.style.display = 'block';
        }
      }, 500);
    }
  });

  //</editor-fold>
};
