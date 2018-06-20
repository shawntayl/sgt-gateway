package com.sgt.student.service.student;


import com.sgt.schemas.*;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;


@Endpoint
public class StudentDetailsEndpoint {


    @PayloadRoot(namespace = "http://sgt.com/students", localPart = "GetStudentRequest")
    @ResponsePayload
    public GetStudentResponse getStudent(@RequestPayload GetStudentRequest request) {
        final ObjectFactory objectFactory = new ObjectFactory();
        final GetStudentResponse response = objectFactory.createGetStudentResponse();
        final StudentDetailsType studentDetails = createStudentDetails(request.getId(), "John", "F54365");
        response.setStudentDetails(studentDetails);
        return response;
    }

    @PayloadRoot(namespace = "http://sgt.com/students", localPart = "GetAllStudentsRequest")
    @ResponsePayload
    public GetAllStudentsResponse getAllStudents(@RequestPayload GetAllStudentsRequest request) {
        final ObjectFactory objectFactory = new ObjectFactory();
        final GetAllStudentsResponse response = objectFactory.createGetAllStudentsResponse();
        response.setYear(request.getYear());
        final List<StudentDetailsType> studentDetails = response.getStudentDetails();
        studentDetails.add(createStudentDetails(1, "John", "F54365"));
        studentDetails.add(createStudentDetails(2, "Peter", "A2356B"));
        studentDetails.add(createStudentDetails(3, "Sam", "G452249585"));
        return response;
    }

    @PayloadRoot(namespace = "http://sgt.com/students", localPart = "CreateStudentRequest")
    @ResponsePayload
    public CreateStudentResponse createStudent(@RequestPayload CreateStudentRequest createStudentRequest) {
        final ObjectFactory objectFactory = new ObjectFactory();
        final CreateStudentResponse createStudentResponse = objectFactory.createCreateStudentResponse();
        createStudentResponse.setStudentDetails(createStudentRequest.getStudentDetails());
        createStudentResponse.setYear(createStudentRequest.getYear());
        createStudentResponse.setStatus(true);
        return createStudentResponse;
    }

    @PayloadRoot(namespace = "http://sgt.com/students", localPart = "UpdateStudentRequest")
    @ResponsePayload
    public UpdateStudentResponse updateStudent(@RequestPayload UpdateStudentRequest updateStudentRequest) {
        final ObjectFactory objectFactory = new ObjectFactory();
        final UpdateStudentResponse updateStudentResponse = objectFactory.createUpdateStudentResponse();
        updateStudentResponse.setStudentDetails(updateStudentRequest.getStudentDetails());
        updateStudentResponse.setYear(updateStudentRequest.getYear());
        updateStudentResponse.setStatus(true);
        return updateStudentResponse;
    }

    @PayloadRoot(namespace = "http://sgt.com/students", localPart = "DeleteStudentRequest")
    @ResponsePayload
    public DeleteStudentResponse deleteStudent(@RequestPayload DeleteStudentRequest updateStudentRequest) {
        final ObjectFactory objectFactory = new ObjectFactory();
        final DeleteStudentResponse deleteStudentResponse = objectFactory.createDeleteStudentResponse();
        deleteStudentResponse.setStudentDetails(updateStudentRequest.getStudentDetails());
        deleteStudentResponse.setYear(updateStudentRequest.getYear());
        deleteStudentResponse.setStatus("Deleted Successfully");
        return deleteStudentResponse;
    }

    private StudentDetailsType createStudentDetails(int value, String adam, String e1234567) {
        StudentDetailsType studentDetails = new StudentDetailsType();
        studentDetails.setId(value);
        studentDetails.setName(adam);
        studentDetails.setPassportNumber(e1234567);
        return studentDetails;
    }

}
