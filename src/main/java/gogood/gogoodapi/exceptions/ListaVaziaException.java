package gogood.gogoodapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class ListaVaziaException extends ResponseStatusException {
    public ListaVaziaException() {
        super(HttpStatusCode.valueOf(204));
    }
}
