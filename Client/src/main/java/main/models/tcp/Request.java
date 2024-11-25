package main.models.tcp;

import main.enums.RequestType;

public class Request {
    private String requestMessage;
    private RequestType requestType;

    public String getRequestMessage() {
        return requestMessage;
    }
    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public Request() {
    }

    public Request(String requestMessage, RequestType requestType) {
        this.requestMessage = requestMessage;
        this.requestType = requestType;
    }
}
