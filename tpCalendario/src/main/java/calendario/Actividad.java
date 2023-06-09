package calendario;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.TreeSet;

public abstract class Actividad implements Serializable {

    protected String titulo; // Titulo de la actividad. En caso de que este atributo no se indique, se le asignara "Sin titulo".
    protected String descripcion; // Descripcion de la actividad. En caso de que este atributo no se indique, se le asignara "Sin descripcion".
    protected boolean diaCompleto;
    protected LocalDateTime inicio;
    protected final TreeSet<Alarma> listaAlarmas;

    Actividad(String titulo, String descripcion, boolean diaCompleto, LocalDateTime inicio) {

        this.titulo = titulo == null? "Sin titulo" : titulo;
        this.descripcion = descripcion == null? "Sin descripcion" : descripcion;

        this.diaCompleto = diaCompleto;

        this.inicio = inicio;
        this.listaAlarmas = new TreeSet<>(new Alarma.ComparadorAlarma());
    }

    // Getters
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public boolean isDiaCompleto() { return diaCompleto; }
    public LocalDateTime getInicio() { return inicio; }
    public TreeSet<Alarma> getListaAlarmas() { return listaAlarmas; }

    // Setters
    void setTitulo(String titulo) { this.titulo = titulo; }
    void setDescripcion(String descripcion) { this.descripcion = descripcion; }


    // Metodos relativos a alarmas

    // Recibe los datos necesarios para crear una instancia de Alarma
    // y la agrega a su lista.
    void agregarAlarma(LocalDateTime inicio, Efecto efecto) {
        var nuevaAlarma = new Alarma(this, inicio, efecto);
        listaAlarmas.add(nuevaAlarma);
    }

    // Recibe los datos necesarios para crear una instancia de Alarma
    // y la agrega a su lista.
    void agregarAlarma(Duration inicio, Efecto efecto) {
        var nuevaAlarma = new Alarma(this, inicio, efecto);
        listaAlarmas.add(nuevaAlarma);
    }

    // Recibe una instancia de Alarma y la quita de su lista.
    void eliminarAlarma(Alarma alarma) { listaAlarmas.remove(alarma); }

    // Busca y devuelve la alarma que tiene las caracteristicas indicadas.
    // Devuelve null si esta no se encuentra en la lista.
    public Alarma buscarAlarma(LocalDateTime inicio, Efecto efecto) {
        for (Alarma alarma : listaAlarmas) {
            if (alarma.getInicio().equals(inicio) && alarma.getEfecto().equals((efecto))) { return alarma; }
        }
        return null;
    }

    // Devuelve la siguiente alarma que deberia sonar. Devuelve null si no hay ninguna alarma.
    Alarma obtenerProximaAlarma() {
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

    public abstract void aceptar(ActividadVisitante visitante);

}
