module SkiRace {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.desktop;  // For java.beans and other desktop-related classes
    requires java.base; // Required for reflection and serialization
    
    opens application to javafx.fxml;  // Open application package for JavaFX reflection
    exports application; // Export package if needed
}