import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.TreeSet;

public class Actividad {

    private String titulo;
    private String descripcion;
    private boolean diaCompleto;
    private final ArrayList<Alarma> listaAlarmas;

    public Actividad(String titulo, String descripcion, boolean diaCompleto) {
        if (titulo == null){
            titulo = "Sin titulo";
        }
        if (descripcion == null){
            descripcion = "Sin descripcion";
        }
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.diaCompleto = diaCompleto;
        this.listaAlarmas = new ArrayList<>();
        if (titulo == null){
            this.titulo = "Sin titulo";
        }
        if (descripcion == null){
            this.descripcion = "Sin descripcion";
        }
    }

    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public boolean isDiaCompleto() { return diaCompleto; }
    public ArrayList<Alarma> getListaAlarmas() { return listaAlarmas; }

    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setDiaCompleto(boolean diaCompleto) { this.diaCompleto = diaCompleto; }


    public void agregarAlarma(Alarma nuevaAlarma) { listaAlarmas.add(nuevaAlarma); }

    public void modificarAlarma(Alarma alarma, LocalDateTime nuevoInicio) { alarma.setInicio(nuevoInicio); }
    public void modificarAlarma(Alarma alarma, Efecto nuevoEfecto) { alarma.setEfecto(nuevoEfecto); }

    public Alarma buscarAlarma(LocalDateTime inicio, Efecto efecto) {
        for (Alarma alarma : listaAlarmas) {
            if (alarma.getInicio().equals(inicio) && alarma.getEfecto().equals((efecto))) {
                return alarma;
            }
        }
        return null;
    }

    public void eliminarAlarma(Alarma alarma) {
        listaAlarmas.remove(alarma);
    }

}
