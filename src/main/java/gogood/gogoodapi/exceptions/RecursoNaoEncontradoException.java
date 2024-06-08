package gogood.gogoodapi.exceptions;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class RecursoNaoEncontradoException extends ResponseStatusException {
    public RecursoNaoEncontradoException(String reason) {
        super(HttpStatusCode.valueOf(404), "Recurso não encontrado.");
    }
}
