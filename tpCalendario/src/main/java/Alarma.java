import java.time.LocalDateTime;

public class Alarma {

    private final Actividad actividad;
    private LocalDateTime inicio;
    private Efecto efecto;

    public Alarma(Actividad actividad, LocalDateTime inicio, Efecto efecto) {
        this.actividad = actividad;
        this.inicio = inicio;
        this.efecto = efecto;
    }

    public Actividad getActividad() {
        return actividad;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }
    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public Efecto getEfecto() { return efecto; }
    public void setEfecto(Efecto efecto) { this.efecto = efecto; }
}
