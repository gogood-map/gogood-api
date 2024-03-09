package gogood.gogoodapi.DTOS;

import gogood.gogoodapi.enums.GeneroEnum;
import org.aspectj.lang.annotation.Before;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public record CriarUsuario(
        String nome,
        String email,
        String senha,
        GeneroEnum genero,
        Date dt_Nascimento){}
