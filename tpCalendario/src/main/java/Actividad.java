import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.TreeSet;

public class Actividad {

    private String titulo;
    private String descripcion;
    private boolean diaCompleto;
    private final ArrayList<Alarma> listaAlarmas;

    public Actividad(String titulo, String descripcion, boolean diaCompleto) {
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

    public String getTitulo() {
        return titulo;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public boolean isDiaCompleto() {
        return diaCompleto;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public void setDiaCompleto(boolean diaCompleto) {
        this.diaCompleto = diaCompleto;
    }


    public void agregarAlarma(LocalDateTime inicio, Integer efecto) {
        var nuevaAlarma = new Alarma(inicio, efecto);
        listaAlarmas.add(nuevaAlarma);
    }

    public void modificarAlarma(Alarma alarma, LocalDateTime nuevoInicio, Integer nuevoEfecto) {
        alarma.setInicio(nuevoInicio);
        alarma.setEfecto(nuevoEfecto);
    }

    public Alarma buscarAlarma(LocalDateTime inicio, Integer efecto) {
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
