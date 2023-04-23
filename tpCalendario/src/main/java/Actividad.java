import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;

public class Actividad {

    protected String titulo;
    protected String descripcion;
    protected boolean diaCompleto;
    protected LocalDateTime inicio;
    protected LocalDateTime fin;
    protected final ArrayList<Alarma> listaAlarmas;

    public Actividad(String titulo, String descripcion, boolean diaCompleto, LocalDateTime inicio, LocalDateTime fin) {
        this.titulo = titulo == null? "Sin titulo" : titulo;
        this.descripcion = descripcion == null? "Sin descripcion" : descripcion;

        this.diaCompleto = diaCompleto;
        if (diaCompleto) {
            inicio = LocalDateTime.of(inicio.getYear(), inicio.getMonth(), inicio.getDayOfMonth(), 0, 0);
            fin = LocalDateTime.of(fin.getYear(), fin.getMonth(), fin.getDayOfMonth(), 0, 0).plusDays(1);
        }

        this.inicio = inicio;
        this.fin = fin;
        this.listaAlarmas = new ArrayList<>();
    }

    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public boolean isDiaCompleto() { return diaCompleto; }
    public LocalDateTime getInicio() { return inicio; }
    public LocalDateTime getFin() { return fin; }
    public ArrayList<Alarma> getListaAlarmas() { return listaAlarmas; }

    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public void setFin(LocalDateTime fin) {
        if (diaCompleto) {
            fin = fin.getHour() == 0 && fin.getMinute() == 0? fin : LocalDateTime.of(fin.getYear(), fin.getMonth(), fin.getDayOfMonth(), 0, 0).plusDays(1);
        }
        this.fin = fin;
    }

    public void setDiaCompleto(boolean esDiaCompleto) {
        if (!diaCompleto && esDiaCompleto) {
            inicio = LocalDateTime.of(inicio.getYear(), inicio.getMonth(), inicio.getDayOfMonth(), 0, 0);
            fin = fin.getHour() == 0 && fin.getMinute() == 0? fin : LocalDateTime.of(fin.getYear(), fin.getMonth(), fin.getDayOfMonth(), 0, 0).plusDays(1);
        }
        this.diaCompleto = esDiaCompleto;
    }

    public void agregarAlarma(Alarma nuevaAlarma) { listaAlarmas.add(nuevaAlarma); }
    public void modificarAlarma(Alarma alarma, LocalDateTime nuevoInicio) { alarma.setInicio(nuevoInicio); }
    public void modificarAlarma(Alarma alarma, Efecto nuevoEfecto) { alarma.setEfecto(nuevoEfecto); }
    public void eliminarAlarma(Alarma alarma) {
        listaAlarmas.remove(alarma);
    }

    public Alarma buscarAlarma(LocalDateTime inicio, Efecto efecto) {
        for (Alarma alarma : listaAlarmas) {
            if (alarma.getInicio().equals(inicio) && alarma.getEfecto().equals((efecto))) { return alarma; }
        }
        return null;
    }
}
