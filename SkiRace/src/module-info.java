module SkiRace {
    requires javafx.controls;
    requires javafx.fxml;
	requires javafx.graphics;

    // Allow reflective access for JavaFX's TableView and other controls
    opens application to javafx.base;

    // Export the application package so JavaFX can instantiate the Main class
    exports application to javafx.graphics;
}
