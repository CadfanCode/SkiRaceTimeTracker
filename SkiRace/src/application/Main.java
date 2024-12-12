package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;

public class Main extends Application {

	// Fields
	TextField nameField;
	VBox raceDistanceVBox;
	TilePane startTypeTilePane;
	CheckBox startIntervalCheckbox;
	static ObservableList<Skier> skierList;
	int dist;
	Button startBtn;
	Button stopBtn;
	boolean raceInProgress;
	TableView<Skier> resultsTable;

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("SkiRaceTracker");

		// Initialize the list of athletes
		skierList = FXCollections.observableArrayList();
		
		// --- Top Region ---
		VBox nameFieldVBox = new VBox();
		Label nameFieldLabel = new Label("Enter Athlete's Name");
		nameField = new TextField();
		nameField.setMinWidth(200);
		nameFieldVBox.getChildren().addAll(nameFieldLabel, nameField);
		nameFieldVBox.setAlignment(Pos.CENTER);
		nameFieldVBox.setPadding(new Insets(20));
		nameFieldVBox.setMinSize(200, 75);
		nameFieldVBox.setSpacing(20);

		VBox raceDistanceVBox = new VBox();
		Label raceDistanceLabel = new Label("Choose distance:");
		ArrayList<CheckBox> checkBoxesDistance = new ArrayList<>();
		CheckBox k10 = new CheckBox("10 km");
		k10.setId("10000");
		k10.setSelected(true);
		checkBoxesDistance.add(k10);

		raceDistanceVBox.getChildren().addAll(raceDistanceLabel, k10); // k5, k20, k40 Removed.
		raceDistanceVBox.setPadding(new Insets(20));
		raceDistanceVBox.setMinSize(200, 100);
		raceDistanceVBox.setAlignment(Pos.CENTER);
		raceDistanceVBox.setSpacing(1);

		TilePane startTypeTilePane = new TilePane();
		CheckBox massStart = new CheckBox("Mass Start");
		CheckBox staggeredStart = new CheckBox("Staggered Start");
		CheckBox jaktStart = new CheckBox("Jaktstart");
		startTypeTilePane.setAlignment(Pos.CENTER);
		startTypeTilePane.setMinSize(300, 75);
		
		
		// -- Track set-up --
		ArrayList<Double> photoCells = new ArrayList<>();
		double trackDistance = 10000;
		Track track = new Track(trackDistance, photoCells);
		Race race = new Race(track, skierList); 
		Pane pane = new Pane();

		// -- Add button function -- 
		Button addSkier = new Button("Add skier");		
		SkiRaceMenu skiRaceMenu = new SkiRaceMenu();
		MenuBar menuBar = skiRaceMenu.getMenu();
		startTypeTilePane.getChildren().addAll(menuBar);

		addSkier.setOnMouseClicked(event -> {
			if (!menuBar.getMenus().get(0).getText().equals("Race type") ) {
				if (!nameField.getText().isEmpty()) { // Check if the nameField is not empty.
					skierList.add(new Skier(nameField.getText(),menuBar.getMenus().get(0).getText(), 10000, skierIDGenerator()));				
				}				
			}
		});

		// Check if all skiers in the list have the same raceDistance.
		if (raceDistanceCheck()) {
		    photoCells.clear();
		    for (double i = 1000; i <= race.getTrack().getDistance(); i += 1000) {
		        photoCells.add(i);
		    }
		}	

		// -- Region settings --
		HBox innerTopBox = new HBox();
		innerTopBox.getChildren().addAll(nameFieldVBox, raceDistanceVBox, startTypeTilePane);

		HBox outerTopBox = new HBox(addSkier);
		outerTopBox.setAlignment(Pos.CENTER_LEFT);
		outerTopBox.setPadding(new Insets(10));

		VBox topRegion = new VBox(innerTopBox, outerTopBox);
		topRegion.setStyle("-fx-background-color: #fafaa7");

		// Table Columns for Skier
		Table table = new Table();        
		resultsTable = table.getTableView();
		resultsTable.setItems(skierList);

		VBox centerRegion = new VBox();
		centerRegion.getChildren().addAll(pane, resultsTable);

		// --- Bottom Region ---
		HBox bottomRegion = new HBox();
		startBtn = new Button("Start");
		stopBtn = new Button("Stop");

		startBtn.setMinSize(50, 50);
		stopBtn.setMinSize(50, 50);
		startBtn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		stopBtn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		startBtn.setStyle("-fx-background-color: #d9ceb0");
		stopBtn.setStyle("-fx-background-color: #d9ceb0");

		startBtn.setOnMouseClicked(event -> {
			if (!raceInProgress) {
				for (CheckBox checkbox : checkBoxesDistance) {
					if(checkbox.isSelected()) {
						race.getTrack().setDistance(Double.parseDouble(checkbox.getId()));
					}
				}
				race.startRace();
			}
		});

		stopBtn.setOnMouseClicked(event -> {
			raceInProgress = false; 
		});

		bottomRegion.getChildren().addAll(startBtn, stopBtn);
		bottomRegion.setAlignment(Pos.CENTER);
		bottomRegion.setSpacing(50);
		bottomRegion.setPadding(new Insets(20));
		bottomRegion.setStyle("-fx-background-color: #fafaa7");

		// --- Main Layout ---
		BorderPane borderPane = new BorderPane();
		borderPane.setTop(topRegion);
		borderPane.setCenter(centerRegion); 
		borderPane.setBottom(bottomRegion);

		Scene scene = new Scene(borderPane, 800, 600);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	// Helper method create skier id number.
	public int skierIDGenerator() {
		if (skierList.isEmpty()) {
			return 1;
		} else {
			return skierList.size() + 1;
		}
	}
	
	// Helper method to check if all Skiers in the skierList have the same raceDistance.
	private boolean raceDistanceCheck() {
		for (int i = 0; i < Main.skierList.size(); i++) {
			if (skierList.get(i).getRaceDistance() != 10000) {
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
