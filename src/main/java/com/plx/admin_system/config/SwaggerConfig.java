package com.plx.admin_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;

/**
 * @author plx
 */
@Configuration
@EnableOpenApi
public class SwaggerConfig {

    /**
     * 创建API应用
     * apiInfo() 增加API相关信息
     * 通过select()函数返回一个ApiSelectorBuilder实例,用来控制哪些接口暴露给Swagger来展现，
     * 本例采用指定扫描的包路径来定义指定要建立API的目录。
     *
     * @return
     */
    //配置swagger的Docket的bean实例
    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //RequestHandlerSelectors,配置要扫描接口的方式
                //basePackage:指定要扫描的包
                //any()：扫描全部
                //none():不扫描
                //.withClassAnnotation():扫描类上的注解
                //.withMethodAnnotation():扫描方法上的注解
                .apis(RequestHandlerSelectors.basePackage("com.plx.admin_system.controller"))
                //paths()过滤什么路径
                .paths(PathSelectors.any())
                .build();
    }


    /**
     * 创建该API的基本信息（这些基本信息会展现在文档页面中）
     * 访问地址：http://ip:port/swagger-ui/index.html
     *
     * @return
     */
    Contact contact = new Contact("plx","https://home.cnblogs.com/u/cony1","1175817624@qq.com");

    //配置Swagger 信息 = ApiInfo
    private ApiInfo apiInfo()
    {
        return new ApiInfo("plx的Api文档",
                "加油干！",
                "1.0",
                "https://home.cnblogs.com/u/cony1",
                contact,
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList<>());
    }
}

