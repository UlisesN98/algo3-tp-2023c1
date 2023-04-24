import java.time.LocalDateTime;
import java.util.TreeSet;

public abstract class Repeticion {

    protected LocalDateTime inicio; // Fecha inicial a partir de la cual una repeticion comienza

    public Repeticion(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    // Recibe una fecha y devuelve, en base al tipo de repeticion, cual es la siguiente donde se repite.
    // Devuelve null en caso de que la siguiente fecha este fuera del limite de la repeticion
    public abstract LocalDateTime calcularSiguienteRepeticion(LocalDateTime fecha);

    // Devuelve un treeset con las fechas de las repeticiones que ocurren dentro del intervalo de
    // tiempo pasado por parametro
    public abstract TreeSet<LocalDateTime> calcularRepeticionesPorIntervalo(LocalDateTime inicioIntervalo, LocalDateTime finIntervalo);

}

