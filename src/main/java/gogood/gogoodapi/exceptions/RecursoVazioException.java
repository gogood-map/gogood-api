package gogood.gogoodapi.exceptions;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class RecursoVazioException extends ResponseStatusException {
    public RecursoVazioException(String reason) {
        super(HttpStatusCode.valueOf(204), reason);
    }
}
