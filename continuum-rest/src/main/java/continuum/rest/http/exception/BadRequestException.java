package continuum.rest.http.exception;

/**
 * 400 Bad Request
 */
public class BadRequestException extends HttpException {
    public BadRequestException(String message) {
        super(message);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }

    @Override
    public String getReasonMessage() {
        return "Bad Request";
    }

    @Override
    public Throwable getCause() {
        return null;
    }
}
