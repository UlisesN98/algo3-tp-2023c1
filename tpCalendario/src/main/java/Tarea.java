import java.time.LocalDateTime;

public class Tarea extends Actividad {

    private boolean completada;
    private LocalDateTime limite;

    public Tarea(String titulo, String descripcion, boolean diaCompleto, LocalDateTime limite) {
        super(titulo, descripcion, diaCompleto);
        if (diaCompleto) {
            limite = LocalDateTime.of(limite.getYear(), limite.getMonth(), limite.getDayOfMonth(), 0, 0).plusDays(1);
        }
        this.limite = limite;
        this.completada = false;
    }

    public LocalDateTime getLimite() {
        return limite;
    }
    public void setLimite(LocalDateTime limite) {
        this.limite = limite;
    }

    public boolean getCompletada() {
        return completada;
    }
    public void setCompletada(boolean completada) {
        this.completada = completada;
    }
}
