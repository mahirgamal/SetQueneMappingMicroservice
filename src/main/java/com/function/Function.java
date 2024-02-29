package com.function;

import com.domain.Authorisation;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import java.util.List;
import java.util.logging.Logger;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {
    private static final Logger logger = Logger.getLogger(Function.class.getName());

    /**
     * This function listens at endpoint "/api/HttpExample". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/HttpExample
     * 2. curl "{your host}/api/HttpExample?name=HTTP%20Query"
     */
    @FunctionName("setQueueMapping")
    public HttpResponseMessage run(
            @HttpTrigger(
                name = "req",
                methods = {HttpMethod.POST},
                authLevel = AuthorizationLevel.ANONYMOUS)
                HttpRequestMessage<List<QueueMappingRequest>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        final List<QueueMappingRequest> queueMappingRequests = request.getBody();

        if (queueMappingRequests == null || queueMappingRequests.isEmpty()) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("Please pass JSON array in the request body").build();
        }
        
        Authorisation auth=new Authorisation();
        if (auth.isAuthorised(request)) {
            return request.createResponseBuilder(HttpStatus.UNAUTHORIZED).body("Unauthorized").build();
        } else {
            logger.warning("Authorization failed or user information exists for user");
            return request.createResponseBuilder(HttpStatus.UNAUTHORIZED).body("Unauthorized").build();
        }
    }
}
