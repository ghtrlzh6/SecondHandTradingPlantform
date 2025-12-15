package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;

import java.util.Arrays;

public class SwaggerConfig {
    public static OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Campus BookSwap API v2.0")
                        .description("校园二手书交易平台API")
                        .version("2.0.0")
                        .contact(new Contact()
                                .email("support@bookswap.example.com")
                                .name("API Support"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html"))
                        .termsOfService("校园二手书交易服务"))
                .servers(Arrays.asList(
                        new Server()
                                .url("http://localhost:8080/api")
                                .description("本地开发服务器"),
                        new Server()
                                .url("https://api.bookswap.example.com")
                                .description("生产环境服务器")))
                .tags(Arrays.asList(
                        new Tag()
                                .name("books")
                                .description("书籍相关操作"),
                        new Tag()
                                .name("users")
                                .description("用户相关操作"),
                        new Tag()
                                .name("orders")
                                .description("订单相关操作")));
    }
}
