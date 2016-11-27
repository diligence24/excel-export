package yzf.java.excel.exception;

/**
 * Created by yangzefeng on 2016/11/21
 */
public class ExportException extends RuntimeException {

    private int status = 500;

    private String message = "unknown exception";

    public ExportException() {
    }

    public ExportException(String message) {
        this.message = message;
    }

    public ExportException(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public ExportException(int status, String message, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.status = status;
    }

    public ExportException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public ExportException(int status, Throwable cause) {
        super(cause);
        this.message = cause.getMessage();
        this.status = status;
    }

    public ExportException(Throwable cause) {
        super(cause);
        this.message = cause.getMessage();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
