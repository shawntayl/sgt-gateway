package com.sgt.student.service.gateway.controller;


import com.sgt.student.service.gateway.handler.StudentNotFoundException;
import com.sgt.student.service.gateway.client.StudentWebClient;
import com.sgt.student.service.gateway.response.error.GatewayApiError;
import com.sgt.students.*;
import io.swagger.annotations.*;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * The Student Rest controller class, exposing the relevant student API using Swagger2.
 */

@RestController
@Api(tags = {"Students"}, value = "students", description = "Everything regarding retrieval and management of students")
@RequestMapping("students")
public class StudentController {

    private static final Logger LOG = Logger.getLogger(StudentController.class.getName());
    private static final String DELETED_SUCCESSFULLY = "Deleted Successfully";

    @Autowired
    private StudentWebClient studentWebClient;


    @ApiOperation(value = "Get a student based on given id number", notes = "Returns a single student", response = GetStudentResponse.class, position = 1)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = GetStudentResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = GatewayApiError.class),
            @ApiResponse(code = 404, message = "Not Found", response = GatewayApiError.class),
            @ApiResponse(code = 500, message = "Server Error", response = GatewayApiError.class)})
    @RequestMapping(path = "/id/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<GetStudentResponse> getStudent(@ApiParam(value = "Student id number", required = true, example = "0")
                                                         @PathVariable("id") int id) {
        if (LOG.isInfoEnabled()) {
            LOG.log(Level.INFO, "received incoming getStudent request");
        }
        final ObjectFactory objectFactory = new ObjectFactory();
        final GetStudentRequest studentDetailsRequest = objectFactory.createGetStudentRequest();
        studentDetailsRequest.setId(id);
        final GetStudentResponse studentDetailsResponse = (GetStudentResponse) studentWebClient.callWebService(studentDetailsRequest);

        if (studentDetailsResponse == null) {
            throw new StudentNotFoundException();
        }

        return ResponseEntity.ok(studentDetailsResponse);
    }

    @ApiOperation(value = "Get all students for the given year", notes = "Returns a list of students", response = GetAllStudentsResponse.class, responseContainer = "List", position = 2)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", responseContainer = "List", response = StudentDetailsType.class),
            @ApiResponse(code = 400, message = "Bad Request", response = GatewayApiError.class),
            @ApiResponse(code = 500, message = "Server Error", response = GatewayApiError.class)})
    @RequestMapping(method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<StudentDetailsType>> getAllStudents(@RequestParam(value = "year") int year) {
        if (LOG.isInfoEnabled()) {
            LOG.log(Level.INFO, "received incoming getAllStudents request");
        }
        final List<StudentDetailsType> studentDetailsTypes = new ArrayList<>();
        final ObjectFactory objectFactory = new ObjectFactory();
        final GetAllStudentsRequest getAllStudentsRequest = objectFactory.createGetAllStudentsRequest();
        getAllStudentsRequest.setYear(BigInteger.valueOf(year));
        final GetAllStudentsResponse response = (GetAllStudentsResponse) studentWebClient.callWebService(getAllStudentsRequest);

        if (response != null) {
            studentDetailsTypes.addAll(response.getStudentDetails());
        }

        return ResponseEntity.ok(studentDetailsTypes);
    }

    @ApiOperation(value = "Create a student for the given year", response = CreateStudentResponse.class, position = 3)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = CreateStudentResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = GatewayApiError.class),
            @ApiResponse(code = 500, message = "Server Error", response = GatewayApiError.class)})
    @RequestMapping(method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CreateStudentResponse> createStudent(@Valid @RequestBody CreateStudentRequest createStudentRequest) {
        if (LOG.isInfoEnabled()) {
            LOG.log(Level.INFO, "received incoming createStudent request");
        }
        final CreateStudentResponse response = (CreateStudentResponse) studentWebClient.callWebService(createStudentRequest);
        if (response == null) {
            throw new NullPointerException("CreateStudentResponse came back null");
        }
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "Update a given student", response = UpdateStudentResponse.class, position = 4)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = UpdateStudentResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = GatewayApiError.class),
            @ApiResponse(code = 500, message = "Server Error", response = GatewayApiError.class)})
    @RequestMapping(method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<UpdateStudentResponse> updateStudent(@Valid @RequestBody UpdateStudentRequest updateStudentRequest) {
        if (LOG.isInfoEnabled()) {
            LOG.log(Level.INFO, "received incoming updateStudent request");
        }
        final UpdateStudentResponse response = (UpdateStudentResponse) studentWebClient.callWebService(updateStudentRequest);
        if (response == null) {
            throw new NullPointerException("UpdateStudentResponse came back null");
        }
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "Delete a given student", response = DeleteStudentResponse.class, position = 5)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = DeleteStudentResponse.class),
            @ApiResponse(code = 204, message = "No Content Found", response = DeleteStudentResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = GatewayApiError.class),
            @ApiResponse(code = 500, message = "Server Error", response = GatewayApiError.class)})
    @RequestMapping(method = RequestMethod.DELETE, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<DeleteStudentResponse> deleteStudent(@Valid @RequestBody DeleteStudentRequest deleteStudentRequest) {
        if (LOG.isInfoEnabled()) {
            LOG.log(Level.INFO, "received incoming deleteStudent request");
        }
        final DeleteStudentResponse response = (DeleteStudentResponse) studentWebClient.callWebService(deleteStudentRequest);

        if (response != null
                && !response.getStatus().equalsIgnoreCase(DELETED_SUCCESSFULLY)) {
            if (LOG.isDebugEnabled()) {
                LOG.log(Level.DEBUG, "received incoming deleteStudent request");
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        }

        return ResponseEntity.ok(response);
    }

}
