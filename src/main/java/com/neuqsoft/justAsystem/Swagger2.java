package com.neuqsoft.justAsystem;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 在线文档配置类 V2.0
 * 从配置文件获取文档上的基本信息
 * @author baidh@neuqsoft.com
 * ========================================
 * 配置文件示例
 * info:
 *   app:
 *     name: "@project.name@"
 *     description: "@project.description@"
 *     version: "@project.version@"
 */
@Configuration
@EnableSwagger2
public class Swagger2 {
	private String BASE_PACKAGE = Swagger2.class.getPackage().getName();

//	@Value("${info.app.name:DEMO}")
	private String appName = "DEMO";

//	@Value("${info.app.description:DEMO}")
	private String appDescription = "DEMO";

//	@Value("${info.app.version:DEMO}")
	private String appVersion = "DEMO";

	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE)).paths(PathSelectors.any()).build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title(appName)
				.description(appDescription)
				.termsOfServiceUrl("http://www.neuqosft.com")
				.version(appVersion)
				.build();
	}
	
	/**
	 * FIX Swagger2 AND Eureka Conflicts
	 * @author baidonghuiPC
	 * 
	 * https://github.com/Netflix/eureka/issues/859
	 * https://github.com/spring-cloud/spring-cloud-netflix/issues/1398
	 * @return
	 */ 
	@Bean
	public static PropertySourcesPlaceholderConfigurer swaggerProperties() {
		PropertySourcesPlaceholderConfigurer propertiesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
		propertiesPlaceholderConfigurer.setIgnoreUnresolvablePlaceholders(true);
		return propertiesPlaceholderConfigurer;
	}
}
