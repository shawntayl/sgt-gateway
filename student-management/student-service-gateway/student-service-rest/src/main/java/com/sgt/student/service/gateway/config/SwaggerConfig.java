package com.sgt.student.service.gateway.config;

import com.fasterxml.classmate.TypeResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.lang.reflect.WildcardType;
import java.util.Collections;

import static springfox.documentation.schema.AlternateTypeRules.newRule;
import static com.google.common.collect.Lists.newArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private static final String STUDENTS = "students";
    private static final String COM_SGT_STUDENT_SERVICE_GATEWAY_CONTROLLER = "com.sgt.student.service.gateway.controller";
    private static final String STUDENTS_PATTERN = "/students/**";

    @Autowired
    private TypeResolver typeResolver;


    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(STUDENTS)
                .select()
                .apis(RequestHandlerSelectors.basePackage(COM_SGT_STUDENT_SERVICE_GATEWAY_CONTROLLER))
                .paths(PathSelectors.ant(STUDENTS_PATTERN))
                .build().apiInfo(apiInfo())
                .genericModelSubstitutes(ResponseEntity.class).
                        alternateTypeRules(newRule(typeResolver.resolve(DeferredResult.class,
                                typeResolver.resolve(ResponseEntity.class, WildcardType.class)),
                                typeResolver.resolve(WildcardType.class)))
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.GET,
                        newArrayList(new ResponseMessageBuilder()
                                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                        .message(HttpStatus.INTERNAL_SERVER_ERROR.name())
                                        .responseModel(new ModelRef("Error"))
                                        .build(),
                                new ResponseMessageBuilder()
                                        .code(HttpStatus.BAD_REQUEST.value())
                                        .message(HttpStatus.BAD_REQUEST.name())
                                        .build()));
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "SGT API",
                "This is the SGT API gateway.",
                "1.0.0",
                "http://www.sgt.com/terms/",
                new Contact("Shawn Taylor", "http://www.sgt.com", "shawn.tayl@sgt.com"),
                "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0.html", Collections.emptyList());
    }


}
