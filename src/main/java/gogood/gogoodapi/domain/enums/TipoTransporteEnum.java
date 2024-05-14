package gogood.gogoodapi.domain.enums;

public enum TipoTransporteEnum {
    TRANSPORTE_PUBLICO("TRANSPORTE_PUBLICO"),
    BIKE("BIKE"),
    VEICULO("VEICULO"),
    A_PE("A_PE");

    private final String tipoTransporte;

    TipoTransporteEnum(String tipoTransporte) {
        this.tipoTransporte = tipoTransporte;
    }

    public String getTipoTransporte() {
        return tipoTransporte;
    }
}
