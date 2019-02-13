package org.lance.swagger.configuration;

import org.lance.swagger.properties.SwaggerInfo;
import org.lance.swagger.properties.SwaggerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Map;

@Configuration
@EnableSwagger2
@ConditionalOnProperty(prefix = "swagger", value = "enable", matchIfMissing = true)
@EnableConfigurationProperties(SwaggerProperties.class)
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
public class SwaggerAutoConfiguration{

    private final SwaggerProperties properties;

    private final DefaultListableBeanFactory beanFactory;

    @Autowired
    public SwaggerAutoConfiguration(SwaggerProperties properties, DefaultListableBeanFactory beanFactory) {
        this.properties = properties;
        this.beanFactory = beanFactory;
        register();
    }

    public void register(){
        for (Map.Entry<String, SwaggerInfo> entry : properties.getGroup().entrySet()) {
            String group = entry.getKey();
            Docket docket = docket(group, entry.getValue());
            beanFactory.registerSingleton(group + "Docket", docket);
        }
    }

    private Docket docket(String group, SwaggerInfo info){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(group).apiInfo(apiInfo(info))
                .useDefaultResponseMessages(false)
                .forCodeGeneration(false).pathMapping("/").select()
                .paths(PathSelectors.ant(info.getAntPaths()))
                .build();
    }

    private ApiInfo apiInfo(SwaggerInfo info){
        defaultInfo(info);
        return new ApiInfoBuilder()
                .title(info.getTitle())
                .description(info.getDescription())
                .version(info.getVersion())
                .license(info.getLicense())
                .licenseUrl(info.getLicenseUrl())
                .termsOfServiceUrl(info.getTermsOfServiceUrl())
                .contact(new Contact(info.getAuthor(), info.getUrl(), info.getEmail()))
                .build();
    }

    private void defaultInfo(SwaggerInfo info){
        if (info.getTitle() == null)
            info.setTitle(properties.getTitle());
        if (info.getDescription() == null)
            info.setDescription(properties.getDescription());
        if (info.getVersion() == null)
            info.setVersion(properties.getVersion());
        if (info.getLicense() == null)
            info.setLicense(properties.getLicense());
        if (info.getLicenseUrl() == null)
            info.setLicenseUrl(properties.getLicenseUrl());
        if (info.getTermsOfServiceUrl() == null)
            info.setTermsOfServiceUrl(properties.getTermsOfServiceUrl());
        if (info.getAuthor() == null)
            info.setAuthor(properties.getAuthor());
        if (info.getUrl() == null)
            info.setUrl(properties.getUrl());
        if (info.getEmail() == null)
            info.setEmail(properties.getEmail());
    }

}
