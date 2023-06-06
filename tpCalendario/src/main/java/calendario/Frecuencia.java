package calendario;

public enum Frecuencia {

    DIARIA("Diario"),
    SEMANAL("Semanal"),
    MENSUAL("Mensual"),
    ANUAL("Anual");

    private final String descripcion;

    Frecuencia(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }

}
