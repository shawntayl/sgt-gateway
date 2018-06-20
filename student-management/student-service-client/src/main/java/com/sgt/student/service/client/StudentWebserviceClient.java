package com.sgt.student.service.client;

import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

@Component
public class StudentWebserviceClient extends WebServiceGatewaySupport {

    public Object callWebService(String url, Object request){
        return getWebServiceTemplate().marshalSendAndReceive(url, request);
    }

    public Object callWebService(Object request){
        return getWebServiceTemplate().marshalSendAndReceive(request);
    }
}
