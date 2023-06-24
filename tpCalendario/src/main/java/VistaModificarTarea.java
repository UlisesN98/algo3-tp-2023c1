import calendario.Tarea;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class VistaModificarTarea {

    @FXML
    private Button modificar;
    @FXML
    private Button cancelar;
    @FXML
    private TextField newTitleModifyTask;
    @FXML
    private TextArea newDescriptionModifyTask;
    @FXML
    private CheckBox allDayCheckboxModifyTask;
    @FXML
    private TextField newDateModifyTask;
    @FXML
    private TextField newTimeLimitModifyTask;

    public void mostrar(Tarea tarea, VistaActividades vistaActividades, VistaTarea vistaTarea, Actualizador actualizador) {
        AnchorPane vista = vistaActividades.cargarVista("vistaModificarTarea.fxml", this);

        cancelar.setOnAction(event -> vistaTarea.mostrar(tarea, vistaActividades, actualizador));

        modificar.setOnAction(event -> {
            actualizador.modificarTarea(tarea, newTitleModifyTask, newDescriptionModifyTask, allDayCheckboxModifyTask, newTimeLimitModifyTask, newDateModifyTask);
            vistaActividades.mostrar();
        });

        vistaActividades.mostrarEscenario(vista);
    }
}
