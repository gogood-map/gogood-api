package gogood.gogoodapi.domain.models.iago;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IagoPersist {
    @NotBlank
    private String prompt;
}
