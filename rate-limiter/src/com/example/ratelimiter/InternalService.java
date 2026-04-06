package com.example.ratelimiter;

public class InternalService {
    private final ExternalServiceGateway gateway;

    public InternalService(ExternalServiceGateway gateway) {
        this.gateway = gateway;
    }

    public  String handleRequest(String clientKey, String request) {
        boolean  needsExternalCall = checkIfExternalCallNeeded(request);

        if  (!needsExternalCall) {
            return "Handled internally, no external call needed for: " + request;
        }

        return gateway.callExternalApi(clientKey, request);
    }

    private boolean checkIfExternalCallNeeded(String request) {
        return request.startsWith("EXTERNAL:");
    }
}
