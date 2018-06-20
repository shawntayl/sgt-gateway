package com.sgt.student.service.gateway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sgt.student.service.client.StudentWebserviceClient;
import com.sgt.student.service.gateway.response.error.GatewayApiError;
import com.sgt.students.*;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigInteger;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@RunWith(SpringRunner.class)
@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    public static final int YEAR_2018 = 2018;
    @Autowired
    private MockMvc mvc;

    @MockBean(name = "studentWebserviceClient")
    private StudentWebserviceClient studentWebserviceClient;

    @Autowired
    private ObjectMapper objectMapper;


    private JacksonTester<GetStudentResponse> studentResponseJacksonTester;
    private JacksonTester<GetAllStudentsRequest> allStudentsRequestJacksonTester;
    private JacksonTester<GetAllStudentsResponse> allStudentsResponseJacksonTester;
    private JacksonTester<CreateStudentRequest> createStudentRequestJacksonTester;
    private JacksonTester<CreateStudentResponse> createStudentResponseJacksonTester;
    private JacksonTester<UpdateStudentRequest> updateStudentRequestJacksonTester;
    private JacksonTester<UpdateStudentResponse> updateStudentResponseJacksonTester;
    private JacksonTester<DeleteStudentRequest> deleteStudentRequestJacksonTester;
    private JacksonTester<DeleteStudentResponse> deleteStudentResponseJacksonTester;
    private JacksonTester<GatewayApiError> gatewayApiErrorJacksonTester;

    @Before
    public void setUp() throws Exception {
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    public void givenAGetStudentsRequest_whenGetStudent_thenReturnJson() throws Exception {
        final GetStudentResponse studentResponse = new GetStudentResponse();
        studentResponse.setStudentDetails(createStudentDetailsType("john"));
        final String outputJson = getJson(studentResponse);

        given(studentWebserviceClient.callWebService(Mockito.any(GetStudentRequest.class))).willReturn(studentResponse);

        mvc.perform(get("/students/id/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(outputJson));
    }

    @Test
    public void givenAGetStudentsRequest_whenGetStudent_thenReturn404NotFoundJson() throws Exception {
        final GatewayApiError gatewayApiError = new GatewayApiError(HttpStatus.NOT_FOUND,"Not Found","Not Found");
        final String outputJson = getJson(gatewayApiError);

        given(studentWebserviceClient.callWebService(Mockito.any(GetStudentRequest.class))).willReturn(null);

        mvc.perform(get("/students/id/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(outputJson));
    }


    @Test
    public void givenAGetAllStudentsRequest_whenGetStudent_thenReturnJsonArray() throws Exception {
        GetAllStudentsResponse studentResponse = new GetAllStudentsResponse();
        final List<StudentDetailsType> studentDetails = studentResponse.getStudentDetails();
        studentDetails.add(createStudentDetailsType("Peter"));
        studentDetails.add(createStudentDetailsType("Sam"));

        final String jsonResponse = getJson(studentResponse);
        given(studentWebserviceClient.callWebService(Mockito.any())).willReturn(studentResponse);

        mvc.perform(get("/students?year={year}", 2018)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is("Peter")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", Matchers.is("Sam")));

    }

    @Test
    public void givenACreateStudentRequest_whenCreateStudent_thenReturnJson() throws Exception {
        final CreateStudentResponse createStudentResponse = new CreateStudentResponse();
        createStudentResponse.setYear(BigInteger.valueOf(YEAR_2018));
        createStudentResponse.setStatus(true);

        final StudentDetailsType studentDetailsType = createStudentDetailsType("Peter", 12345678, "A123667788");
        createStudentResponse.setStudentDetails(studentDetailsType);

        final CreateStudentRequest createStudentRequest = new CreateStudentRequest();
        createStudentRequest.setYear(BigInteger.valueOf(YEAR_2018));
        createStudentRequest.setStudentDetails(studentDetailsType);
        final String json = getJson(createStudentRequest);
        given(studentWebserviceClient.callWebService(Mockito.any())).willReturn(createStudentResponse);

        mvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType
                        (MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.content().json(getJson(createStudentResponse)));
        verify(studentWebserviceClient, times(1)).callWebService(Mockito.any());

    }

    @Test
    public void givenACreateStudentRequest_whenCreateStudent_thenReturnInternalServerErrorJson() throws Exception {
        final String message = "CreateStudentResponse came back null";
        final GatewayApiError gatewayApiError = new GatewayApiError(HttpStatus.INTERNAL_SERVER_ERROR, message, message);
        final String outputJson = getJson(gatewayApiError);

        final StudentDetailsType studentDetailsType = createStudentDetailsType("Peter", 12345678, "A123667788");

        final CreateStudentRequest createStudentRequest = new CreateStudentRequest();
        createStudentRequest.setYear(BigInteger.valueOf(YEAR_2018));
        createStudentRequest.setStudentDetails(studentDetailsType);
        final String json = getJson(createStudentRequest);
        given(studentWebserviceClient.callWebService(Mockito.any())).willReturn(null);

        mvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.content().contentType
                        (MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.content().json(outputJson));
        verify(studentWebserviceClient, times(1)).callWebService(Mockito.any());
    }


    @Test
    public void givenAUpdateStudentRequest_whenUpdateStudent_thenReturnJson() throws Exception {
        final UpdateStudentResponse updateStudentResponse = new UpdateStudentResponse();
        updateStudentResponse.setYear(BigInteger.valueOf(YEAR_2018));
        updateStudentResponse.setStatus(true);
        final StudentDetailsType studentDetailsType = createStudentDetailsType("Peter", 12345678, "A123667788");
        updateStudentResponse.setStudentDetails(studentDetailsType);

        final UpdateStudentRequest updateStudentRequest = new UpdateStudentRequest();
        updateStudentRequest.setYear(BigInteger.valueOf(YEAR_2018));
        updateStudentRequest.setStudentDetails(studentDetailsType);
        final String json = getJson(updateStudentRequest);
        given(studentWebserviceClient.callWebService(Mockito.any())).willReturn(updateStudentResponse);

        mvc.perform(put("/students")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType
                        (MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.content().json(getJson(updateStudentResponse)));
        verify(studentWebserviceClient, times(1)).callWebService(Mockito.any());
    }

    @Test
    public void givenAUpdateStudentRequest_whenUpdateStudent_thenReturnInternalServerErrorJson() throws Exception {
        final String message = "UpdateStudentResponse came back null";
        final GatewayApiError gatewayApiError = new GatewayApiError(HttpStatus.INTERNAL_SERVER_ERROR, message, message);
        final String outputJson = getJson(gatewayApiError);

        final StudentDetailsType studentDetailsType = createStudentDetailsType("Peter", 12345678, "A123667788");

        final UpdateStudentRequest updateStudentRequest = new UpdateStudentRequest();
        updateStudentRequest.setYear(BigInteger.valueOf(YEAR_2018));
        updateStudentRequest.setStudentDetails(studentDetailsType);
        final String json = getJson(updateStudentRequest);
        given(studentWebserviceClient.callWebService(Mockito.any())).willReturn(null);

        mvc.perform(put("/students")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.content().contentType
                        (MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.content().json(outputJson));
        verify(studentWebserviceClient, times(1)).callWebService(Mockito.any());
    }


    @Test
    public void givenADeleteStudentRequest_whenDeleteStudent_thenReturnJson() throws Exception {
        final DeleteStudentResponse deleteStudentResponse = new DeleteStudentResponse();
        deleteStudentResponse.setYear(BigInteger.valueOf(YEAR_2018));
        deleteStudentResponse.setStatus("Deleted Successfully");
        final StudentDetailsType studentDetailsType = createStudentDetailsType("Peter", 12345678, "A123667788");
        deleteStudentResponse.setStudentDetails(studentDetailsType);

        final DeleteStudentRequest deleteStudentRequest = new DeleteStudentRequest();
        deleteStudentRequest.setYear(BigInteger.valueOf(YEAR_2018));
        deleteStudentRequest.setStudentDetails(studentDetailsType);
        final String json = getJson(deleteStudentRequest);
        given(studentWebserviceClient.callWebService(Mockito.any())).willReturn(deleteStudentResponse);

        mvc.perform(delete("/students")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType
                        (MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.content().json(getJson(deleteStudentResponse)));
        verify(studentWebserviceClient, times(1)).callWebService(Mockito.any());
    }

    @Test
    public void givenADeleteStudentRequest_whenDeleteStudent_thenReturnInternalServerErrorJson() throws Exception {
        final DeleteStudentResponse deleteStudentResponse = new DeleteStudentResponse();
        deleteStudentResponse.setYear(BigInteger.valueOf(YEAR_2018));
        deleteStudentResponse.setStatus("Failed");
        final StudentDetailsType studentDetailsType = createStudentDetailsType("Peter", 12345678, "A123667788");
        deleteStudentResponse.setStudentDetails(studentDetailsType);

        final DeleteStudentRequest deleteStudentRequest = new DeleteStudentRequest();
        deleteStudentRequest.setYear(BigInteger.valueOf(YEAR_2018));
        deleteStudentRequest.setStudentDetails(studentDetailsType);
        final String json = getJson(deleteStudentRequest);
        given(studentWebserviceClient.callWebService(Mockito.any())).willReturn(deleteStudentResponse);

        mvc.perform(delete("/students")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.content().contentType
                        (MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.content().json(getJson(deleteStudentResponse)));
        verify(studentWebserviceClient, times(1)).callWebService(Mockito.any());
    }

    private StudentDetailsType createStudentDetailsType(String peter) {
        final StudentDetailsType studentDetailsType = new StudentDetailsType();
        studentDetailsType.setName(peter);
        return studentDetailsType;
    }

    private StudentDetailsType createStudentDetailsType(String peter, int id, String passportNumber) {
        final StudentDetailsType studentDetailsType = new StudentDetailsType();

        studentDetailsType.setName(peter);
        studentDetailsType.setId(id);
        studentDetailsType.setPassportNumber(passportNumber);
        return studentDetailsType;
    }

    private String getJson(GetStudentResponse studentResponse) throws java.io.IOException {
        return studentResponseJacksonTester.write(studentResponse).getJson();
    }

    private String getJson(GetAllStudentsResponse allStudentsResponse) throws java.io.IOException {
        return allStudentsResponseJacksonTester.write(allStudentsResponse).getJson();
    }

    private String getJson(CreateStudentRequest createStudentRequest) throws java.io.IOException {
        return createStudentRequestJacksonTester.write(createStudentRequest).getJson();
    }

    private String getJson(CreateStudentResponse createStudentResponse) throws java.io.IOException {
        return createStudentResponseJacksonTester.write(createStudentResponse).getJson();
    }

    private String getJson(UpdateStudentRequest updateStudentRequest) throws java.io.IOException {
        return updateStudentRequestJacksonTester.write(updateStudentRequest).getJson();
    }

    private String getJson(UpdateStudentResponse updateStudentResponse) throws java.io.IOException {
        return updateStudentResponseJacksonTester.write(updateStudentResponse).getJson();
    }

    private String getJson(DeleteStudentRequest deleteStudentRequest) throws java.io.IOException {
        return deleteStudentRequestJacksonTester.write(deleteStudentRequest).getJson();
    }

    private String getJson(DeleteStudentResponse deleteStudentResponse) throws java.io.IOException {
        return deleteStudentResponseJacksonTester.write(deleteStudentResponse).getJson();
    }

    private String getJson(GatewayApiError gatewayApiError) throws java.io.IOException {
        return gatewayApiErrorJacksonTester.write(gatewayApiError).getJson();
    }


}