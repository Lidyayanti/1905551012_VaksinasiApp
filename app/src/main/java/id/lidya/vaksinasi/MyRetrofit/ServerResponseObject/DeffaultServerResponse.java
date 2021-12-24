package id.winata.vaksinasi.MyRetrofit.ServerResponseObject;

public class DeffaultServerResponse {
    public String status;
    public String code;
    public String message;

    public static final String SUCCESS = "success";
    public static final String FAIL = "fail";

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
