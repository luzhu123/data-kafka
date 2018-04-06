package com.keruyun.fintech.commons.swagger;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Profile("dev")
public class SwaggerConfig {
    @Bean
    Docket docket() {
        ApiInfoBuilder apiInfoBuilder = new ApiInfoBuilder();
        ApiInfo apiInfo = apiInfoBuilder.title("")
                .description("")
                .termsOfServiceUrl("")
                .version("1.0")
                .contact(new Contact("wallet", "www.keruyun.com", "on_wallet@keruyun.com"))
                .build();

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .build()
                .directModelSubstitute(java.time.LocalDateTime.class, java.sql.Date.class)
                .directModelSubstitute(java.time.LocalDateTime.class, java.util.Date.class)
                .apiInfo(apiInfo);
    }
}
