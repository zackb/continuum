package continuum.rest.http.exception;

/**
 * 404 Not Found
 */
public class NotFoundException extends HttpException {
    public NotFoundException(String message) {
        super(message);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }

    @Override
    public String getReasonMessage() {
        return "Not Found";
    }
}
