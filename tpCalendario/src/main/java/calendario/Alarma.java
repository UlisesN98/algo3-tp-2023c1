package calendario;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;

public class Alarma implements Serializable {

    private final Actividad actividad; // Actividad que cuenta con esta Alarma
    private final LocalDateTime inicio;
    private final Efecto efecto; // Tipo de efecto que producira la Alarma

    // Constructor para el caso donde el tiempo de inicio de la Alarma se indica con una hora absoluta.
    Alarma(Actividad actividad, LocalDateTime inicio, Efecto efecto) {
        this.actividad = actividad;
        this.inicio = inicio;
        this.efecto = efecto;
    }

    // Constructor para el caso donde el tiempo de inicio de la Alarma se indica con un intervalo previo al inicio de su actividad.
    Alarma(Actividad actividad, Duration intervaloPrevio, Efecto efecto) {
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

    // Devuelve true si la instancia de Alarma se inicia antes que la pasada por parametro
    boolean esAnterior(Alarma alarma) {
        return this.getInicio().isBefore(alarma.getInicio());
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

}
