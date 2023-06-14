package calendario;

import java.time.LocalDateTime;

public class EventoRepetido extends Evento {

    private final Evento eventoOriginal;

    EventoRepetido(String titulo, String descripcion, boolean diaCompleto, LocalDateTime inicio, LocalDateTime fin, Repeticion repeticion, Evento evento) {
        super(titulo, descripcion, diaCompleto, inicio, fin, repeticion);
        this.eventoOriginal = evento;
        this.original = false;
    }

    Evento getEventoOriginal() {
        return eventoOriginal;
    }

    @Override
    void setTitulo(String titulo) {
        eventoOriginal.setTitulo(titulo);
    }

    @Override
    void setDescripcion(String descripcion) {
        eventoOriginal.setDescripcion(descripcion);
    }

    @Override
    void setInicio(LocalDateTime inicio) {
        eventoOriginal.setInicio(inicio);
    }

    @Override
    void setFin(LocalDateTime fin) {
        eventoOriginal.setFin(fin);
    }

    @Override
    void setDiaCompleto(boolean esDiaCompleto) {
        eventoOriginal.setDiaCompleto(esDiaCompleto);
    }

    @Override
    public boolean esRepetido() {
        return !original;
    }
}
