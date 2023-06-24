import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class VistaCrearTarea {

    @FXML
    private Button crear;
    @FXML
    private Button cancelar;
    @FXML
    private TextField taskTitle;
    @FXML
    private TextArea taskDescription;
    @FXML
    private TextField fieldFecha;
    @FXML
    private TextField endTimeTask;
    @FXML
    private CheckBox completeDayCheckbox;
    @FXML
    private TextArea areaAlarms;

    public void mostrar(VistaActividades vistaActividades, Actualizador actualizador) {
        AnchorPane vista = vistaActividades.cargarVista("vistaCrearTarea.fxml", this);

        crear.setOnAction(event -> {
            actualizador.crearTarea(taskTitle, taskDescription, completeDayCheckbox, endTimeTask, fieldFecha, areaAlarms);
            vistaActividades.mostrar();
        });

        cancelar.setOnAction(event -> vistaActividades.mostrar());

        vistaActividades.mostrarEscenario(vista);
    }
}
