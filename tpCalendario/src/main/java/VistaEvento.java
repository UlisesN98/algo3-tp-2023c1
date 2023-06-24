import calendario.Alarma;
import calendario.Evento;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class VistaEvento {
    @FXML
    private Button cancelar;
    @FXML
    private Button eliminar;
    @FXML
    private VBox detalle;
    @FXML
    private Button vistaModificar;

    private final VistaModificarEvento vistaModificarEvento;

    public VistaEvento() {
        this.vistaModificarEvento = new VistaModificarEvento();
    }

    public void mostrar(Evento evento, VistaActividades vistaActividades, Actualizador actualizador) {
        AnchorPane vista = vistaActividades.cargarVista("vistaEvento.fxml", this);

        cancelar.setOnAction(event -> vistaActividades.mostrar());

        eliminar.setOnAction(event -> {
            actualizador.eliminarEvento(evento);
            vistaActividades.mostrar();
        });

        vistaModificar.setOnAction(event -> vistaModificarEvento.mostrar(evento, vistaActividades, this, actualizador));

        mostrarDetalle(evento);

        vistaActividades.mostrarEscenario(vista);
    }

    public void mostrarDetalle(Evento evento) {
        Label titulo = new Label();
        Font f1 = new Font(18);
        titulo.setText(evento.getTitulo());
        titulo.setFont(f1);

        Label descripcion = new Label();
        Font f2 = new Font(14);
        descripcion.setText(evento.getDescripcion());
        descripcion.setFont(f2);

        Label diaCompleto = new Label();
        Label inicio = new Label();
        Label fin = new Label();

        if (evento.isDiaCompleto()) {
            diaCompleto.setText("Dia completo ✅");
            inicio.setText("Inicia: " + VistaActividades.formatoFecha(evento.getInicio()));
            fin.setText("Finaliza: "  + VistaActividades.formatoFecha(evento.getFin()));
        } else {
            diaCompleto.setText("Dia completo ✗");
            inicio.setText("Inicia: " + VistaActividades.formatoTiempo(evento.getInicio()));
            fin.setText("Finaliza: " + VistaActividades.formatoTiempo(evento.getFin()));
        }

        Label repeticion = new Label();
        if (evento.esRepetido()) {
            repeticion.setText("Se repite ✅");
        } else {
            repeticion.setText("Se repite ✗");
        }

        detalle.getChildren().add(titulo);
        detalle.getChildren().add(descripcion);
        detalle.getChildren().add(new Label());
        detalle.getChildren().add(diaCompleto);
        detalle.getChildren().add(inicio);
        detalle.getChildren().add(fin);
        detalle.getChildren().add(new Label());
        detalle.getChildren().add(repeticion);
        detalle.getChildren().add(new Label());

        if (evento.getListaAlarmas().size() == 0) {
            detalle.getChildren().add(new Label("Sin alarmas"));
        } else {
            for (Alarma a : evento.getListaAlarmas()) {
                Label alarma = new Label();
                alarma.setText(String.format("Alarma [ Inicio: %s ]", VistaActividades.formatoTiempo(a.getInicio())));
                detalle.getChildren().add(alarma);
            }
        }
    }

}
