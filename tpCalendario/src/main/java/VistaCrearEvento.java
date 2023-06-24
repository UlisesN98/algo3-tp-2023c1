import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class VistaCrearEvento {

    @FXML
    private Button crear;
    @FXML
    private Button cancelar;
    @FXML
    private TextField eventTitle;
    @FXML
    private TextArea eventDescription;
    @FXML
    private TextField fieldFechaInicio;
    @FXML
    private TextField fieldFechaFin;
    @FXML
    private TextField startTimeEvent;
    @FXML
    private TextField endTimeEvent;
    @FXML
    private CheckBox dailyRepeatCheck;
    @FXML
    private TextField frequencyRepeatDaily;
    @FXML
    private TextField quantityReps;
    @FXML
    private CheckBox completeDayCheckbox;
    @FXML
    private TextArea areaAlarms;

    public void mostrar(VistaActividades vistaActividades, Actualizador actualizador) {
        AnchorPane vista = vistaActividades.cargarVista("vistaCrearEvento.fxml", this);

        crear.setOnAction(event -> {
            actualizador.crearEvento(eventTitle, eventDescription, completeDayCheckbox, fieldFechaInicio, startTimeEvent, fieldFechaFin,  endTimeEvent, areaAlarms, dailyRepeatCheck, frequencyRepeatDaily, quantityReps);
            vistaActividades.mostrar();
        });

        cancelar.setOnAction(event -> vistaActividades.mostrar());

        vistaActividades.mostrarEscenario(vista);
    }
}
