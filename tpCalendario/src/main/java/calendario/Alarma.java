package calendario;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;

public class Alarma implements Serializable {

    private final Actividad actividad; // Actividad que cuenta con esta Alarma
    private LocalDateTime inicio;
    private final Efecto efecto; // Tipo de efecto que producira la Alarma
    private boolean disparada;

    // Constructor para el caso donde el tiempo de inicio de la Alarma se indica con una hora absoluta.
    Alarma(Actividad actividad, LocalDateTime inicio, Efecto efecto) {
        this.actividad = actividad;
        this.inicio = inicio;
        this.efecto = efecto;
        this.disparada = false;
    }

    // Constructor para el caso donde el tiempo de inicio de la Alarma se indica con un intervalo previo al inicio de su actividad.
    Alarma(Actividad actividad, Duration intervaloPrevio, Efecto efecto) {
        this.actividad = actividad;
        this.inicio = actividad.getInicio().minus(intervaloPrevio.getSeconds(), intervaloPrevio.getUnits().get(0));
        this.efecto = efecto;
        this.disparada = false;
    }

    // Getters & Setters

    public LocalDateTime getInicio() {
        return inicio;
    }
    public Efecto getEfecto() { return efecto; }
    public boolean isDisparada() { return disparada; }

    public void setInicio(LocalDateTime inicio) { this.inicio = inicio; }

    // Devuelve true si la instancia de Alarma se inicia antes que la pasada por parametro
    boolean esAnterior(Alarma alarma) {
        return this.getInicio().isBefore(alarma.getInicio());
    }

    // Devuelve true si la instancia de Alarma se inicia antes que el tiempo pasado por parametro
    boolean esAnterior(LocalDateTime tiempo) {
        return this.getInicio().isBefore(tiempo);
    }

    // Metodo requerido para hacer comparaciones que permitan ordenar Alarmas temporalmente
    public static class ComparadorAlarma implements Comparator<Alarma>, Serializable {
        @Override
        public int compare(Alarma o1, Alarma o2) {
            if (o1.getInicio().isBefore(o2.getInicio())) {
                return -1;
            }
            if (o1.getInicio().isAfter(o2.getInicio())) {
                return 1;
            }
            return 0;
        }
    }

    public Actividad disparar() {
        disparada = true;
        return actividad;
    }

}
