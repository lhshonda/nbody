module com.lhshonda.nbody {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.lhshonda.nbody to javafx.fxml;
    exports com.lhshonda.nbody;
}