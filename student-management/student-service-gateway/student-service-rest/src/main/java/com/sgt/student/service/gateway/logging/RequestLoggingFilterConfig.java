package com.sgt.student.service.gateway.logging;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;


/**
 * To Enabled and configure Springs Commons Request Logging Filter,
 * which enables logging of incoming request messages
 * Please note that the CommonsRequestLoggingFilter only logs in debug mode
 * I.e. set logging.level.org.springframework.web.filter.CommonsRequestLoggingFilter=DEBUG
 */
@Configuration
public class RequestLoggingFilterConfig {

    private static final int MAX_PAYLOAD_LENGTH = 10000;
    private static final String AFTER_MESSAGE_PREFIX = "REQUEST DATA : ";

    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(MAX_PAYLOAD_LENGTH);
        filter.setIncludeHeaders(false);
        filter.setAfterMessagePrefix(AFTER_MESSAGE_PREFIX);
        return filter;
    }
}
