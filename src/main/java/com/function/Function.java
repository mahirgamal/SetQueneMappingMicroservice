package com.function;

import com.domain.SetQueueMapping;
import com.domain.Authorisation;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.ExecutionContext;

import java.util.Optional;

public class Function {

    private final SetQueueMapping setQueueMapping;
    private final Authorisation authorisation;

    public Function() {
        this.setQueueMapping = new SetQueueMapping();
        this.authorisation = new Authorisation();
    }

    @FunctionName("setQueueMapping")
    public HttpResponseMessage setQueueMapping(
            @HttpTrigger(name = "req", methods = {HttpMethod.POST}, authLevel = AuthorizationLevel.FUNCTION) HttpRequestMessage<Optional<QueueMappingRequest>> request,
            ExecutionContext context) {
        
        QueueMappingRequest mappingRequest = request.getBody().orElse(null);
        if (mappingRequest == null || !mappingRequest.isValid()) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Invalid request data").build();
        }

        long userId = mappingRequest.getPublisherId();
        if (!authorisation.isUserAuthorised(userId)) {
            return request.createResponseBuilder(HttpStatus.UNAUTHORIZED).body("User not authorised").build();
        }

        boolean success = setQueueMapping.setMapping(userId, mappingRequest.getConsumerQueueName(), mappingRequest.getEventType());
        return success ? request.createResponseBuilder(HttpStatus.OK).body("Mapping set successfully").build() 
                       : request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to set mapping").build();
    }
}
