package calendario;

import java.time.LocalDateTime;

public class Tarea extends Actividad {

    private boolean completada;

    Tarea(String titulo, String descripcion, boolean diaCompleto, LocalDateTime inicio) {
        super(titulo, descripcion, diaCompleto, inicio);

        if (diaCompleto) {
            this.inicio = inicio.getHour() == 0 && inicio.getMinute() == 0? inicio : LocalDateTime.of(inicio.getYear(), inicio.getMonth(), inicio.getDayOfMonth(), 0, 0).plusDays(1);
        }

        this.completada = false;
    }

    // Recibe una nueva fecha de inicio, que en caso de ser la Tarea de dia completo se adaptara al formato antes de asignarse.
    void setInicio(LocalDateTime inicio) {
        if (diaCompleto) {
            this.inicio = inicio.getHour() == 0 && inicio.getMinute() == 0? inicio : LocalDateTime.of(inicio.getYear(), inicio.getMonth(), inicio.getDayOfMonth(), 0, 0).plusDays(1);
        } else {
            this.inicio = inicio;
        }
    }

    // Modifica el estado de dia completo por el que indica el parametro y adapta de ser necesario la fecha de inicio.
    void setDiaCompleto(boolean esDiaCompleto) {
        if (!diaCompleto && esDiaCompleto) {
            inicio = inicio.getHour() == 0 && inicio.getMinute() == 0? inicio : LocalDateTime.of(inicio.getYear(), inicio.getMonth(), inicio.getDayOfMonth(), 0, 0).plusDays(1);
        }
        this.diaCompleto = esDiaCompleto;
    }

    // Indica si la Tarea esta completada.
    public boolean isCompletada() { return completada; }

    // Cambia el estado de incompleta a completada y viceversa.
    void cambiarEstadoTarea() { this.completada = !completada; }

}
