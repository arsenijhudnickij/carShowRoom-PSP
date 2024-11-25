package main.Models.TCP;

import main.Enums.ResponseStatus;
import com.google.gson.Gson;

public class Response {
    private ResponseStatus responseStatus; // Статус ответа
    private String message; // Сообщение, которое отправляется клиенту
    private Object data; // Дополнительные данные (если есть)

    public Response() {
    }

    public Response(ResponseStatus responseStatus, String message, Gson gson) {
        this.responseStatus = responseStatus;
        this.message = message;
        this.data = null; // Изначально data равно null
    }

    public Response(ResponseStatus responseStatus, String message, Object data) {
        this.responseStatus = responseStatus;
        this.message = message;
        this.data = data;
    }

    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
