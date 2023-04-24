import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.TreeSet;

public class Evento extends Actividad {

    private Repeticion repeticion; // Instancia de Repeticion con los datos de que como se repetira el Evento. Si el Evento no se repite lleva null.
    private LocalDateTime siguienteRepeticion; // Fecha de la siguiente repeticion del Evento. Si el Evento no se repite lleva null.

    public Evento(String titulo, String descripcion, boolean diaCompleto, LocalDateTime inicio, LocalDateTime fin, Repeticion repeticion) {
        super(titulo, descripcion, diaCompleto, inicio, fin);
        this.repeticion = repeticion;
        this.siguienteRepeticion = repeticion != null ? repeticion.calcularSiguienteRepeticion(inicio) : null;
    }

    // Recibe una nueva fecha de inicio, que en caso de ser el Evento de dia completo se adaptara al formato antes de asignarse.
    public void setInicio(LocalDateTime inicio) {
        if (diaCompleto) {
            inicio = LocalDateTime.of(inicio.getYear(), inicio.getMonth(), inicio.getDayOfMonth(), 0, 0);
        }
        this.inicio = inicio;
    }

    // Devuelve la instancia de Repeticion del Evento;
    public Repeticion getRepeticion() {
        return repeticion;
    }

    // Cambia el tipo de Repeticion del Evento por el el indicado.
    public void setRepeticion(Repeticion repeticion) {
        this.repeticion = repeticion;
    }

    // Indica si es repetido o no.
    public boolean esRepetido() {return (repeticion != null);}

    // Devuelve la fecha de la siguiente repeticion.
    public LocalDateTime getSiguienteRepeticion() {
        return siguienteRepeticion;
    }

    // Actualiza las fechas del Evento por las de su siguiente repeticion.
    public void actualizarSiguienteRepeticion() {
        if (siguienteRepeticion == null) {
            return;
        }
        long diferencia = ChronoUnit.SECONDS.between(inicio, fin);
        inicio = siguienteRepeticion;
        fin = siguienteRepeticion.plusSeconds(diferencia);
        this.siguienteRepeticion = repeticion.calcularSiguienteRepeticion(siguienteRepeticion);
    }

    // Devuelve un treeset de LocalDateTime con las fechas de la repeticiones que ocurren en el intervalo pasado por parametro.
    public TreeSet<LocalDateTime> repeticionesPorIntervalo(LocalDateTime inicioIntervalo, LocalDateTime finIntervalo) {
        return repeticion.calcularRepeticionesPorIntervalo(inicioIntervalo, finIntervalo);
    }
}
