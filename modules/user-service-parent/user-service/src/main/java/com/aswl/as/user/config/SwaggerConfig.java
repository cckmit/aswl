package com.aswl.as.user.config;

import com.aswl.as.common.security.constant.SecurityConstant;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger配置
 *
 * @author aswl.com
 * @date 2019/3/26 16:26
 */
@Configuration
@EnableSwagger2
@EnableWebMvc
public class SwaggerConfig implements WebMvcConfigurer {

    @Bean
    public Docket createRestApi() {
        List<Parameter> parameterList = new ArrayList<>();
        parameterList.add(authorizationParameter());
        parameterList.add(tenantCodeParameter());
        parameterList.add(projectIdParameter());
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .ignoredParameterTypes(OAuth2Authentication.class)
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(parameterList);
    }

    /**
     * Authorization 请求头
     *
     * @return Parameter
     */
    private Parameter authorizationParameter() {
        ParameterBuilder tokenBuilder = new ParameterBuilder();
        tokenBuilder.name("Authorization")
                .description("access_token")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false).build();
        return tokenBuilder.build();
    }

    /**
     * Tenant-Code 请求头
     *
     * @return Parameter
     */
    private Parameter tenantCodeParameter() {
        ParameterBuilder tokenBuilder = new ParameterBuilder();
        tokenBuilder.name("Tenant-Code")
                .defaultValue(SecurityConstant.DEFAULT_TENANT_CODE)
                .description("租户标识")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false).build();
        return tokenBuilder.build();
    }

    /**
     * Project-Id 请求头
     *
     * @return Parameter
     */
    private Parameter projectIdParameter(){
        ParameterBuilder tokenBuilder = new ParameterBuilder();
        tokenBuilder.name("Project-Id")
                .defaultValue("")
                .description("项目id")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false).build();
        return tokenBuilder.build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Swagger API")
                .description("https://gzaswl.net/as-microservice-as")
                .termsOfServiceUrl("https://gzaswl.net/as-microservice-as")
                .contact(new Contact("aswl", "https://gzaswl.net/as-microservice-as", "22305143@qq.com"))
                .version("2.0")
                .build();
    }

    /**
     * 显示swagger-ui.html文档展示页，还必须注入swagger资源：
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
