import calendario.Evento;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class VistaModificarEvento {

    @FXML
    private Button modificar;
    @FXML
    private Button cancelar;
    @FXML
    private TextField newTitleModifyEvent;
    @FXML
    private TextArea newDescriptionModifyEvent;
    @FXML
    private CheckBox allDayCheckboxModifyEvent;
    @FXML
    private TextField newDateStartModifyEvent;
    @FXML
    private TextField newTimeStartModifyEvent;
    @FXML
    private TextField newDateEndModifyEvent;
    @FXML
    private TextField newTimeEndModifyEvent;

    public void mostrar(Evento evento, VistaActividades vistaActividades, VistaEvento vistaEvento, Actualizador actualizador) {
        AnchorPane vista = vistaActividades.cargarVista("vistaModificarEvento.fxml", this);

        cancelar.setOnAction(event -> vistaEvento.mostrar(evento, vistaActividades, actualizador));

        modificar.setOnAction(event -> {
            actualizador.modificarEvento(evento, newTitleModifyEvent, newDescriptionModifyEvent, allDayCheckboxModifyEvent, newTimeStartModifyEvent, newDateStartModifyEvent, newTimeEndModifyEvent, newDateEndModifyEvent);
            vistaActividades.mostrar();
        });

        vistaActividades.mostrarEscenario(vista);
    }
}
