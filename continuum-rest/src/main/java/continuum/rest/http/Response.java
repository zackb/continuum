package continuum.rest.http;

import java.io.InputStream;
import java.util.Map;

/**
 * HTTP Response
 * Created by zack on 3/14/16.
 */
public class Response {

    public String contentType;
    public int status;
    public Object data;
    public InputStream inputStream;
    public Map<String, String> headers;

    public Response contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public Response status(int status) {
        this.status = status;
        return this;
    }

    public Response data(Object data) {
        this.data = data;
        return this;
    }
}
