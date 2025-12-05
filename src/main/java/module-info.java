module com.kiber.ctfprogram {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.prefs;

    opens com.kiber.ctfprogram to javafx.fxml;
    exports com.kiber.ctfprogram;
}