import java.time.LocalDateTime;

public class Tarea extends Actividad {

    private boolean completada; // Boolean que indica si la Tarea esta completa o no

    public Tarea(String titulo, String descripcion, boolean diaCompleto, LocalDateTime inicio, LocalDateTime fin) {
        super(titulo, descripcion, diaCompleto, inicio, fin);

        this.completada = false;
    }

    // Recibe una nueva fecha de fin, que en caso de ser la Tarea de dia completo se adaptara al formato antes de asignarse.
    @Override
    public void setFin(LocalDateTime fin) {
        if (diaCompleto) {
            inicio = LocalDateTime.of(fin.getYear(), fin.getMonth(), fin.getDayOfMonth(), 0, 0);
            this.fin = inicio.plusDays(1);
        } else {
            this.inicio = fin;
            this.fin = fin;
        }
    }

    // Modifica el estado de dia completo por el que indica el parametro y adapta de ser necesario las fechas de inicio y fin.
    @Override
    public void setDiaCompleto(boolean esDiaCompleto) {
        if (!diaCompleto && esDiaCompleto) {
            inicio = LocalDateTime.of(fin.getYear(), fin.getMonth(), fin.getDayOfMonth(), 0, 0);
            fin = inicio.plusDays(1);
        } else if (diaCompleto && !esDiaCompleto) {
            inicio = fin;
        }
        this.diaCompleto = esDiaCompleto;
    }

    // Indica si la Tarea esta completada.
    public boolean isCompletada() { return completada; }

    // Cambia el estado de incompleta a completada y viceversa.
    public void cambiarEstadoTarea() { this.completada = !completada; }

}
