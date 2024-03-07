package gogood.gogoodapi.Enum;

public enum GeneroEnum {
    MASCULINO("M"),
    FEMININO("F"),
    OUTROS("O"),
    PREFIRO_NAO_DIZER("PFN");

    GeneroEnum(String sigla) {
    }

    String getGeneroSigla(String sigla){
        return sigla;
    }
}
