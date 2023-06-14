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
}
