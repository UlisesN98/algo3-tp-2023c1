import java.time.Duration;
import java.time.LocalDateTime;

public class Alarma {

    private final Actividad actividad; // Actividad que cuenta con esta Alarma
    private LocalDateTime inicio; // Fecha y hora de inicio de la ALarma
    private Efecto efecto; // Tipo de efecto que producira la Alarma

    // Constructor para el caso donde el tiempo de inicio de la Alarma se indica con una hora absoluta.
    public Alarma(Actividad actividad, LocalDateTime inicio, Efecto efecto) {
        this.actividad = actividad;
        this.inicio = inicio;
        this.efecto = efecto;
    }

    // Constructor para el caso donde el tiempo de inicio de la Alarma se indica con un intervalo previo al inicio de su actividad.
    public Alarma(Actividad actividad, Duration intervaloPrevio, Efecto efecto) {
        this.actividad = actividad;
        this.inicio = actividad.getInicio().minus(intervaloPrevio.getSeconds(), intervaloPrevio.getUnits().get(0));
        this.efecto = efecto;
    }

    // Getters

    public Actividad getActividad() {
        return actividad;
    }
    public LocalDateTime getInicio() {
        return inicio;
    }
    public Efecto getEfecto() { return efecto; }

    // Setters

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }
    public void setEfecto(Efecto efecto) { this.efecto = efecto; }
}
