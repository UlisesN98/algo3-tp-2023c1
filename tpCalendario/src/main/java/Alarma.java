import java.time.LocalDateTime;

public class Alarma {

    private LocalDateTime inicio;
    private Integer efecto;

    public Alarma(LocalDateTime inicio, Integer efecto) {
        this.inicio = inicio;
        this.efecto = efecto;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }
    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public Integer getEfecto() {
        return efecto;
    }

    public void setEfecto(Integer efecto) {
        this.efecto = efecto;
    }
}
