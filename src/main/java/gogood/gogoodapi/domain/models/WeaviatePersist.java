package gogood.gogoodapi.domain.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WeaviatePersist {
    @NotBlank
    private String prompt;
    @NotNull
    private Integer limit;
    @NotNull
    private Integer page;
}
