package continuum.rest.http.exception;

/**
 * 500 Internal Server error
 */
public class InternalServerErrorException extends HttpException {
    public InternalServerErrorException(String message, Throwable throwable) {
        super(message, throwable);
    }

    @Override
    public int getStatusCode() {
        return 500;
    }

    @Override
    public String getReasonMessage() {
        return "Internal Server Error";
    }
}
