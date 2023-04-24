import java.time.LocalDateTime;
import java.util.ArrayList;

public class Actividad {

    protected String titulo; // Titulo de la actividad. En caso de que este atributo no se indique, se le asignara "Sin titulo".
    protected String descripcion; // Descripcion de la actividad. En caso de que este atributo no se indique, se le asignara "Sin descripcion".
    protected boolean diaCompleto; // Boolean que indica si es de dia completo
    protected LocalDateTime inicio; // Fecha y hora de inicio
    protected LocalDateTime fin; // Fecha y hora de fin
    protected final ArrayList<Alarma> listaAlarmas; // Lista con sus alarmas designadas

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

    // Getters

    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public boolean isDiaCompleto() { return diaCompleto; }
    public LocalDateTime getInicio() { return inicio; }
    public LocalDateTime getFin() { return fin; }
    public ArrayList<Alarma> getListaAlarmas() { return listaAlarmas; }

    // Setters

    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    // Recibe una nueva fecha de fin, que en caso de ser la Actividad de dia completo se adaptara al formato antes de asignarse.
    public void setFin(LocalDateTime fin) {
        if (diaCompleto) {
            fin = fin.getHour() == 0 && fin.getMinute() == 0? fin : LocalDateTime.of(fin.getYear(), fin.getMonth(), fin.getDayOfMonth(), 0, 0).plusDays(1);
        }
        this.fin = fin;
    }

    // Modifica el estado de dia completo por el que indica el parametro y adapta de ser necesario las fechas de inicio y fin.
    public void setDiaCompleto(boolean esDiaCompleto) {
        if (!diaCompleto && esDiaCompleto) {
            inicio = LocalDateTime.of(inicio.getYear(), inicio.getMonth(), inicio.getDayOfMonth(), 0, 0);
            fin = fin.getHour() == 0 && fin.getMinute() == 0? fin : LocalDateTime.of(fin.getYear(), fin.getMonth(), fin.getDayOfMonth(), 0, 0).plusDays(1);
        }
        this.diaCompleto = esDiaCompleto;
    }

    // Metodos relativos a alarmas

    // Recibe una instancia de Alarma y la agrega a su lista.
    public void agregarAlarma(Alarma nuevaAlarma) { listaAlarmas.add(nuevaAlarma); }

    // Recibe una instancia de Alarma y la quita de su lista.
    public void eliminarAlarma(Alarma alarma) { listaAlarmas.remove(alarma); }

    // Busca y devuelve la alarma que tiene las caracteristicas indicadas.
    // Devuelve null si esta no se encuentra en la lista.
    public Alarma buscarAlarma(LocalDateTime inicio, Efecto efecto) {
        for (Alarma alarma : listaAlarmas) {
            if (alarma.getInicio().equals(inicio) && alarma.getEfecto().equals((efecto))) { return alarma; }
        }
        return null;
    }

    /*
    public void actualizarAlarmas() {
        for (Alarma alarma: listaAlarmas) {
            alarma.setInicio(this.inicio.minusSeconds(ChronoUnit.SECONDS.between(alarma.getInicio(), this.inicio)));
        }
    }*/
}
