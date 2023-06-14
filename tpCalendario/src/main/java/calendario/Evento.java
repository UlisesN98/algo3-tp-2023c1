package calendario;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class Evento extends Actividad {

    private LocalDateTime fin;
    private Repeticion repeticion; // Instancia de Repeticion con los datos de que como se repetira el Evento. Si el Evento no se repite lleva null.
    private EventoRepetido repActual; // Instancia de EventoRepetido que tiene como objetivo contener la información de la repeticion vigente
    protected boolean original; // Diferencia a un Evento de un EventoRepetido

    Evento(String titulo, String descripcion, boolean diaCompleto, LocalDateTime inicio, LocalDateTime fin, Repeticion repeticion) {
        super(titulo, descripcion, diaCompleto, inicio);

        if (diaCompleto) {
            this.inicio = LocalDateTime.of(inicio.getYear(), inicio.getMonth(), inicio.getDayOfMonth(), 0, 0);
            fin = LocalDateTime.of(fin.getYear(), fin.getMonth(), fin.getDayOfMonth(), 23, 59);
            if (repeticion != null) { repeticion.setInicio(this.inicio); }
        }

        this.fin = fin;
        this.repeticion = repeticion;

        this.original = true;
    }

    public LocalDateTime getFin() { return fin; }

    // Recibe una nueva fecha de inicio, que en caso de ser el Evento de día completo se adaptara al formato antes de asignarse.
    void setInicio(LocalDateTime inicio) {
        if (diaCompleto) {
            inicio = LocalDateTime.of(inicio.getYear(), inicio.getMonth(), inicio.getDayOfMonth(), 0, 0);
        }
        this.inicio = inicio;
        if (repeticion != null) { repeticion.setInicio(this.inicio); }
    }

    // Recibe una nueva fecha de fin, que en caso de ser el Evento de día completo se adaptara al formato antes de asignarse.
    void setFin(LocalDateTime fin) {
        if (diaCompleto) {
            fin = LocalDateTime.of(fin.getYear(), fin.getMonth(), fin.getDayOfMonth(), 23, 59);
        }
        this.fin = fin;
    }

    // Modifica el estado de día completo por el que indica el parametro y adapta de ser necesario las fechas de inicio y fin.
    void setDiaCompleto(boolean esDiaCompleto) {
        if (!diaCompleto && esDiaCompleto) {
            inicio = LocalDateTime.of(inicio.getYear(), inicio.getMonth(), inicio.getDayOfMonth(), 0, 0);
            fin = LocalDateTime.of(fin.getYear(), fin.getMonth(), fin.getDayOfMonth(), 23, 59);
            if (repeticion != null) { repeticion.setInicio(this.inicio); }
        }
        this.diaCompleto = esDiaCompleto;
    }

    void setRepeticion(Repeticion repeticion) {
        this.repeticion = repeticion;
    }

    public boolean esRepetido() { return (repeticion != null); }

    // Devuelve un treeset de LocalDateTime con las fechas de las repeticiones que ocurren en el intervalo pasado por parametro.
    TreeSet<LocalDateTime> repeticionesPorIntervalo(LocalDateTime inicioIntervalo, LocalDateTime finIntervalo) {
        return repeticion.calcularRepeticionesPorIntervalo(inicioIntervalo, finIntervalo);
    }

    public void aceptar(ActividadVisitante visitante) {
        visitante.visitarEvento(this);
    }

    boolean esOriginal() {
        return original;
    }

    EventoRepetido getRepeticionActual() { return repActual; }

    // Metodo para instanciar el atributo de la repeticion actual en
    // caso de ser requerido. Si no se cuenta con una repeticion coloca null.
    void setRepeticionActual() {
        if (repeticion != null) {
            this.repActual = crearRepeticion(inicio);
            return;
        }
        this.repActual = null;
    }

    // Recibe una fecha y, basándose en esta, instancia la repeticion actual correspondiente.
    // Si el evento ya no cuenta con una repeticion pasada esa fecha, coloca null.
    void actualizarRepeticionActual(LocalDateTime fecha) {
        LocalDateTime inicioRepeticion = repeticion.calcularSiguienteRepeticion(fecha);
        if (inicioRepeticion == null) {
            this.repActual = null;
            return;
        }
        this.repActual = crearRepeticion(inicioRepeticion);
    }

    // Recibe una fecha correspondiente al inicio de una repeticion del evento,
    // y devuelve una instancia de EventoRepetido que replica la información del
    // Evento adecuada a esa fecha.
    EventoRepetido crearRepeticion(LocalDateTime inicioRepeticion) {
        var nuevoEvento = new EventoRepetido(titulo, descripcion, diaCompleto, inicioRepeticion, inicioRepeticion.plusSeconds(ChronoUnit.SECONDS.between(inicio, fin)), null, this);

        List<LocalDateTime> inicios = new ArrayList<>();
        List<Efecto> efectos = new ArrayList<>();
        for (Alarma alarma : listaAlarmas) {
            inicios.add(inicioRepeticion.minusSeconds(ChronoUnit.SECONDS.between(alarma.getInicio(), inicio)));
            efectos.add(alarma.getEfecto());
        }
        nuevoEvento.agregarAlarmas(inicios.toArray(new LocalDateTime[0]), efectos.toArray(new Efecto[0]));
        return nuevoEvento;
    }

    boolean tieneSiguienteRepeticion() {
        return repActual != null;
    }

    boolean finRepeticionEsAnterior(LocalDateTime fecha) {
        if (repActual == null) {
            return false;
        }
        return repActual.getFin().isBefore(fecha);
    }
}
