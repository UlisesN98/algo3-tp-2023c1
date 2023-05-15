import java.time.LocalDateTime;

public class Tarea extends Actividad {

    private boolean completada;

    public Tarea(String titulo, String descripcion, boolean diaCompleto, LocalDateTime inicio) {
        super(titulo, descripcion, diaCompleto, inicio);

        if (diaCompleto) {
            this.inicio = LocalDateTime.of(inicio.getYear(), inicio.getMonth(), inicio.getDayOfMonth(), 0, 0).plusDays(1);
        }

        this.completada = false;
    }

    // Recibe una nueva fecha de fin, que en caso de ser la Tarea de dia completo se adaptara al formato antes de asignarse.
    public void setInicio(LocalDateTime inicio) {
        if (diaCompleto) {
            this.inicio = LocalDateTime.of(inicio.getYear(), inicio.getMonth(), inicio.getDayOfMonth(), 0, 0).plusDays(1);
        } else {
            this.inicio = inicio;
        }
    }

    // Modifica el estado de dia completo por el que indica el parametro y adapta de ser necesario las fechas de inicio y fin.
    public void setDiaCompleto(boolean esDiaCompleto) {
        if (!diaCompleto && esDiaCompleto) {
            inicio = LocalDateTime.of(inicio.getYear(), inicio.getMonth(), inicio.getDayOfMonth(), 0, 0).plusDays(1);
        }
        this.diaCompleto = esDiaCompleto;
    }

    // Indica si la Tarea esta completada.
    public boolean isCompletada() { return completada; }

    // Cambia el estado de incompleta a completada y viceversa.
    public void cambiarEstadoTarea() { this.completada = !completada; }

    /*
    public static Tarea deserializar(InputStream is) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInStream = new ObjectInputStream(is);
        return (Tarea) objectInStream.readObject();
    }*/

}
