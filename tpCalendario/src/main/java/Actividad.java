import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.TreeSet;

public class Actividad {

    protected String titulo; // Titulo de la actividad. En caso de que este atributo no se indique, se le asignara "Sin titulo".
    protected String descripcion; // Descripcion de la actividad. En caso de que este atributo no se indique, se le asignara "Sin descripcion".
    protected boolean diaCompleto;
    protected LocalDateTime inicio; // Fecha y hora de inicio
    protected LocalDateTime fin; // Fecha y hora de fin
    protected final TreeSet<Alarma> listaAlarmas;

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
        this.listaAlarmas = new TreeSet<>(new Alarma.ComparadorAlarma());
    }

    // Getters

    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public boolean isDiaCompleto() { return diaCompleto; }
    public LocalDateTime getInicio() { return inicio; }
    public LocalDateTime getFin() { return fin; }
    public TreeSet<Alarma> getListaAlarmas() { return listaAlarmas; }

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

    // Recibe los datos necesarios para crear una instancia de Alarma
    // y la agrega a su lista.
    public void agregarAlarma(LocalDateTime inicio, Efecto efecto) {
        var nuevaAlarma = new Alarma(this, inicio, efecto);
        listaAlarmas.add(nuevaAlarma);
    }

    // Recibe los datos necesarios para crear una instancia de Alarma
    // y la agrega a su lista.
    public void agregarAlarma(Duration inicio, Efecto efecto) {
        var nuevaAlarma = new Alarma(this, inicio, efecto);
        listaAlarmas.add(nuevaAlarma);
    }

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

    // Devuelve la siguiente alarma que deberia sonar. Devuelve null si no hay ninguna alarma.
    public Alarma obtenerProximaAlarma() {
        return listaAlarmas.isEmpty()? null : listaAlarmas.first();
    }

    // Metodo requerido para hacer comparaciones que permitan ordenar Eventos y Tareas temporalmente
    public static class ComparadorActividad implements Comparator<Actividad> {
        @Override
        public int compare(Actividad o1, Actividad o2) {
            if (o1.getInicio().isBefore(o2.getInicio())){
                return -1;
            }
            if (o1.getInicio().isAfter(o2.getInicio())){
                return 1;
            }
            return 0;
        }
    }
}
