package gogood.gogoodapi.domain.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WeaviatePersist {
    @NotBlank
    private String prompt;
}
