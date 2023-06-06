package calendario;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.TreeSet;

public class Evento extends Actividad {

    private LocalDateTime fin;
    private Repeticion repeticion; // Instancia de Repeticion con los datos de que como se repetira el Evento. Si el Evento no se repite lleva null.
    private LocalDateTime siguienteRepeticion; // Fecha de la siguiente repeticion del Evento. Si el Evento no se repite lleva null.

    Evento(String titulo, String descripcion, boolean diaCompleto, LocalDateTime inicio, LocalDateTime fin, Repeticion repeticion) {
        super(titulo, descripcion, diaCompleto, inicio);

        if (diaCompleto) {
            this.inicio = LocalDateTime.of(inicio.getYear(), inicio.getMonth(), inicio.getDayOfMonth(), 0, 0);
            fin = LocalDateTime.of(fin.getYear(), fin.getMonth(), fin.getDayOfMonth(), 23, 59);
            if (repeticion != null) { repeticion.setInicio(this.inicio); }
        }

        this.fin = fin;
        this.repeticion = repeticion;
        this.siguienteRepeticion = repeticion != null ? repeticion.calcularSiguienteRepeticion(inicio) : null;
    }

    public LocalDateTime getFin() { return fin; }

    // Recibe una nueva fecha de inicio, que en caso de ser el Evento de dia completo se adaptara al formato antes de asignarse.
    void setInicio(LocalDateTime inicio) {
        if (diaCompleto) {
            inicio = LocalDateTime.of(inicio.getYear(), inicio.getMonth(), inicio.getDayOfMonth(), 0, 0);
        }
        this.inicio = inicio;
        if (repeticion != null) { repeticion.setInicio(this.inicio); }
    }

    // Recibe una nueva fecha de fin, que en caso de ser el Evento de dia completo se adaptara al formato antes de asignarse.
    void setFin(LocalDateTime fin) {
        if (diaCompleto) {
            fin = LocalDateTime.of(fin.getYear(), fin.getMonth(), fin.getDayOfMonth(), 23, 59);
        }
        this.fin = fin;
    }

    // Modifica el estado de dia completo por el que indica el parametro y adapta de ser necesario las fechas de inicio y fin.
    void setDiaCompleto(boolean esDiaCompleto) {
        if (!diaCompleto && esDiaCompleto) {
            inicio = LocalDateTime.of(inicio.getYear(), inicio.getMonth(), inicio.getDayOfMonth(), 0, 0);
            fin = LocalDateTime.of(fin.getYear(), fin.getMonth(), fin.getDayOfMonth(), 23, 59);
            if (repeticion != null) { repeticion.setInicio(this.inicio); }
        }
        this.diaCompleto = esDiaCompleto;
    }

    // Devuelve la instancia de Repeticion del Evento;
    Repeticion getRepeticion() {
        return repeticion;
    }

    // Cambia el tipo de Repeticion del Evento por el el indicado.
    // Actualiza la siguiente repeticion.
    void setRepeticion(Repeticion repeticion) {
        if (repeticion == null) {
            siguienteRepeticion = null;
        } else {
            siguienteRepeticion = repeticion.calcularSiguienteRepeticion(inicio);
        }
        this.repeticion = repeticion;
    }

    // Indica si es repetido o no.
    public boolean esRepetido() {return (repeticion != null);}

    // Devuelve la fecha de la siguiente repeticion.
    public LocalDateTime getSiguienteRepeticion() {
        return siguienteRepeticion;
    }

    // Actualiza las fechas del Evento por las de su siguiente repeticion.
    void actualizarSiguienteRepeticion() {
        if (siguienteRepeticion == null) {
            return;
        }
        long diferencia = ChronoUnit.SECONDS.between(inicio, fin);
        inicio = siguienteRepeticion;
        fin = siguienteRepeticion.plusSeconds(diferencia);
        this.siguienteRepeticion = repeticion.calcularSiguienteRepeticion(siguienteRepeticion);
    }

    // Devuelve un treeset de LocalDateTime con las fechas de la repeticiones que ocurren en el intervalo pasado por parametro.
    TreeSet<LocalDateTime> repeticionesPorIntervalo(LocalDateTime inicioIntervalo, LocalDateTime finIntervalo) {
        return repeticion.calcularRepeticionesPorIntervalo(inicioIntervalo, finIntervalo);
    }

    public void aceptar(ActividadVisitante visitante) {
        visitante.visitarEvento(this);
    }

}
