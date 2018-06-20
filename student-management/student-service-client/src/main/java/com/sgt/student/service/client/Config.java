package com.sgt.student.service.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.client.support.interceptor.PayloadValidatingInterceptor;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import java.io.IOException;
import java.util.List;

@Configuration
public class Config {

    public static final String COM_SGT_STUDENTS = "com.sgt.students";

    @Value("${client.default-uri}")
    private String defaultUri;

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath(COM_SGT_STUDENTS);
        return marshaller;
    }

    @Bean
    public StudentWebserviceClient studentWebserviceClient(Jaxb2Marshaller marshaller) throws IOException {
        StudentWebserviceClient client = new StudentWebserviceClient();
        client.setDefaultUri(defaultUri);
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        return client;
    }
}
