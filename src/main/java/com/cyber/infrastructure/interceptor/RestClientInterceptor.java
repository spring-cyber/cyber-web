package com.cyber.infrastructure.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.cyber.domain.constant.AuthingTokenKey;
import com.cyber.domain.entity.AuthingToken;
import com.cyber.infrastructure.toolkit.ThreadLocals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RestClientInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestClientInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        try {
            AuthingToken<JSONObject> token = null;
            if(ThreadLocals.get(AuthingTokenKey.X_CLIENT_TOKEN_USER) != null) {
                HttpRequestWrapper requestWrapper = new HttpRequestWrapper(request);

                token =  (AuthingToken<JSONObject>) ThreadLocals.get(AuthingTokenKey.X_CLIENT_TOKEN_USER);
                requestWrapper.getHeaders().set(AuthingTokenKey.X_CLIENT_JWT_TOKEN,token.getJwtToken());

                return execution.execute(requestWrapper, body);
            }
            return execution.execute(request,body);
        } catch (Throwable throwable) {
            throw throwable;
        }
    }
}
