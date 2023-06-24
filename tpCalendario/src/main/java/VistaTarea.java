import calendario.Alarma;
import calendario.Tarea;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class VistaTarea {

    @FXML
    private Button cancelar;
    @FXML
    private Button eliminar;
    @FXML
    private VBox detalle;
    @FXML
    private Button vistaModificar;
    @FXML
    private Button marcar;

    private final VistaModificarTarea vistaModificarTarea;

    public VistaTarea() {
        this.vistaModificarTarea = new VistaModificarTarea();
    }

    public void mostrar(Tarea tarea, VistaActividades vistaActividades, Actualizador actualizador) {
        AnchorPane vista = vistaActividades.cargarVista("vistaTarea.fxml", this);

        cancelar.setOnAction(event -> vistaActividades.mostrar());

        eliminar.setOnAction(event -> {
            actualizador.eliminarTarea(tarea);
            vistaActividades.mostrar();
        });

        marcar.setOnAction(event -> {
            actualizador.cambiarTareaCompletada(tarea);
            mostrar(tarea, vistaActividades, actualizador);
        });

        vistaModificar.setOnAction(event -> vistaModificarTarea.mostrar(tarea, vistaActividades, this, actualizador));

        mostrarDetalle(tarea);

        vistaActividades.mostrarEscenario(vista);
    }

    public void mostrarDetalle(Tarea tarea) {
        Label titulo = new Label();
        Font f1 = new Font(18);
        titulo.setText(tarea.getTitulo());
        titulo.setFont(f1);

        Label descripcion = new Label();
        Font f2 = new Font(14);
        descripcion.setText(tarea.getDescripcion());
        descripcion.setFont(f2);

        Label diaCompleto = new Label();
        Label inicio = new Label();

        if (tarea.isDiaCompleto()) {
            diaCompleto.setText("Dia completo ✅");
            inicio.setText("Vence: " + VistaActividades.formatoFecha(tarea.getInicio()));
        } else {
            diaCompleto.setText("Dia completo ✗");
            inicio.setText("Vence: " + VistaActividades.formatoTiempo(tarea.getInicio()));
        }

        Label completada = new Label();
        if (tarea.isCompletada()) {
            completada.setText("Completada: ✅");
        } else {
            completada.setText("Completada: ✗");
        }

        detalle.getChildren().add(titulo);
        detalle.getChildren().add(descripcion);
        detalle.getChildren().add(new Label());
        detalle.getChildren().add(diaCompleto);
        detalle.getChildren().add(inicio);
        detalle.getChildren().add(new Label());
        detalle.getChildren().add(completada);
        detalle.getChildren().add(new Label());

        if (tarea.getListaAlarmas().size() == 0) {
            detalle.getChildren().add(new Label("Sin alarmas"));
        } else {
            for (Alarma a : tarea.getListaAlarmas()) {
                Label alarma = new Label();
                alarma.setText(String.format("Alarma [ Inicio: %s ]", VistaActividades.formatoTiempo(a.getInicio())));
                detalle.getChildren().add(alarma);
            }
        }
    }

}
