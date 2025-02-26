package com.quwen.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * http://localhost:20235/api/swagger-ui/index.html
 */
@Configuration
public class Swagger3Config {
    @Bean
    public Docket createRestApi(Environment environment) {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
//                根据环境判断是否启用Swagger
                .enable(environment.acceptsProfiles(Profiles.of("dev","test")))
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.quwen"))//扫描的包路径
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("quwen")
                .description("趣问 | Springboot | Swagger3")
//                .termsOfServiceUrl("url")
                .version("1.0")
//                .contact(new Contact("name", "url", "email"))
                .build();
    }
}
