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

    public static Boolean istipoTransporte(String transporte) {
        String tipoTransporte = transporte.toUpperCase();
        return tipoTransporte.equals("TRANSPORTE_PUBLICO") || tipoTransporte.equals("BIKE") || tipoTransporte.equals("VEICULO") || tipoTransporte.equals("A_PE");

    }

    public String getTipoTransporte() {
        return tipoTransporte;
    }

}
