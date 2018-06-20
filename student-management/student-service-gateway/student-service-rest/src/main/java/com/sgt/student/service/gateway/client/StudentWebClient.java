package com.sgt.student.service.gateway.client;


import com.sgt.student.service.client.StudentWebserviceClient;
import com.sgt.student.service.gateway.logging.LogExecutionTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * This class should be used to call the Student SOAP service.
 * I.e. Wraps the <i>com.sgt.student.service.client.StudentWebserviceClient</i> class, allowing for performance logging
 */

@Component
public class StudentWebClient {

    @Autowired
    private StudentWebserviceClient studentWebserviceClient;


    /**
     * Method used to call the student web service.
     * Note that this method is annotated with <i>@LogExecutionTime</i>, to enable performance logging.
     * @param request the request object
     * @return the response object
     */
    @LogExecutionTime
    public Object callWebService(final Object request){
        return this.studentWebserviceClient.callWebService(request);
    }


}
