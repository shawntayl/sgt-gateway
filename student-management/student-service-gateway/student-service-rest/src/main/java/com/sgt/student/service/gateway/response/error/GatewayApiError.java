package com.sgt.student.service.gateway.response.error;

import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class GatewayApiError {

    private int status;
    private String error;
    private String message;
    private List<String> errors;

    public GatewayApiError(HttpStatus httpStatus, String message, List<String> errors) {
        this.status = httpStatus.value();
        this.error = httpStatus.getReasonPhrase();
        this.message = message;
        this.errors = errors;
    }

    public GatewayApiError(HttpStatus httpStatus, String message, String error) {
        super();
        this.status = httpStatus.value();
        this.error = httpStatus.getReasonPhrase();
        this.message = message;
        errors = Arrays.asList(error);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GatewayApiError)) return false;
        GatewayApiError that = (GatewayApiError) o;
        return getStatus() == that.getStatus() &&
                Objects.equals(getError(), that.getError()) &&
                Objects.equals(getMessage(), that.getMessage()) &&
                Objects.equals(getErrors(), that.getErrors());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getStatus(), getError(), getMessage(), getErrors());
    }

    @Override
    public String toString() {
        return "GatewayApiError{" +
                "status=" + status +
                ", error='" + error + '\'' +
                ", message='" + message + '\'' +
                ", errors=" + errors +
                '}';
    }
}
