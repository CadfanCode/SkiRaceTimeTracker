module SkiRace {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.desktop;  // This includes java.beans
    requires java.base; // Required for reflection and serialization

    opens application;  // Open package for JavaFX reflection (if needed)
    exports application; // Export package if needed
}
