package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
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
import java.time.Duration;
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
	static TableView<Skier> resultsTable;
	TableView<Skier> seededResultsTable;

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("SkiRaceTracker");

		// Initialize the list of athletes
		skierList = FXCollections.observableArrayList();

		// --- Top Region ---
		VBox nameFieldVBox = new VBox();
		Label nameFieldLabel = new Label("Ange deltagarens namn:");
		nameField = new TextField();
		nameField.setMinWidth(200);
		nameFieldVBox.getChildren().addAll(nameFieldLabel, nameField);
		nameFieldVBox.setAlignment(Pos.CENTER);
		nameFieldVBox.setPadding(new Insets(20));
		nameFieldVBox.setMinSize(200, 75);
		nameFieldVBox.setSpacing(20);

		VBox raceDistanceVBox = new VBox();
		Label raceDistanceLabel = new Label("Välj distans:");
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

		// -- Search Field --
		HBox searchBox = new HBox();
		Label searchBoxLabel = new Label("Sök enligt StartID:");
		Label searchResultLabel = new Label();
		TextField searchField = new TextField();
		searchField.setMinWidth(100);
		Button searchButton = new Button("Sök");
		searchButton.setOnMouseClicked(event -> {
			String searchResult = search(searchField.getText()); // Perform the search
			if (!searchResult.isEmpty()) {
				searchResultLabel.setText(searchResult);
			}
			else {
				// Optionally, show an alert if no booking is found
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("No Results");
				alert.setHeaderText(null);
				alert.setContentText("Ingen bokning hittades för det angivna bokningsnumret.");
				alert.showAndWait();
			}
		});
		searchBox.getChildren().addAll(searchBoxLabel, searchField, searchButton, searchResultLabel);



		// -- Track set-up --
		ArrayList<Double> photoCells = new ArrayList<>();
		double trackDistance = 10000;
		Track track = new Track(trackDistance, photoCells);
		Race race = new Race(track, skierList); 
		Pane pane = new Pane();

		// -- Add button function -- 
		Button addSkier = new Button("Add skier");
		addSkier.setOnMouseClicked(event -> {
			if (!nameField.getText().isEmpty()) { // Check if the nameField is not empty.
				Skier skier = new Skier(nameField.getText(), 10000, skierIDGenerator());
				skierList.add(skier);
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
		innerTopBox.getChildren().addAll(nameFieldVBox, raceDistanceVBox);

		HBox outerTopBox = new HBox(addSkier);
		outerTopBox.setAlignment(Pos.BOTTOM_RIGHT);
		outerTopBox.setPadding(new Insets(10));

		VBox topRegion = new VBox(innerTopBox, outerTopBox, searchBox);
		topRegion.setStyle("-fx-background-color: #fafaa7");

		// Table Columns for Skier
		Table table = new Table();        
		resultsTable = table.getTableView();
		resultsTable.setItems(skierList);

		// -- New code 13/12/2024 -- 
		// Table to show results from the seeding Race.
		Table seededTable = new Table();
		seededResultsTable = seededTable.getTableView();
		seededResultsTable.setItems(Race.raceSeedingList);
		Pane pane2 = new Pane();
		VBox leftRegion = new VBox();
		VBox rightRegion = new VBox();
		rightRegion.getChildren().addAll(pane2, seededTable.getTableView());
		leftRegion.getChildren().addAll(pane, resultsTable);

		// Combine leftRegion and rightRegion into centerRegion
		HBox centerRegion = new HBox();
		centerRegion.getChildren().addAll(leftRegion, rightRegion);
		centerRegion.setSpacing(20); // Add spacing between left and right regions
		centerRegion.setPadding(new Insets(10));
		centerRegion.setAlignment(Pos.CENTER);
		// -- 

		// --- Bottom Region ---
		VBox bottomRegion = new VBox();
		VBox startTypeVBox = new VBox();
		HBox startTypeHBox = new HBox();
		CheckBox massStart = new CheckBox("Masstart");
		CheckBox individuellStart = new CheckBox("Individuell start");
		CheckBox jaktStart = new CheckBox("Jaktstart");
		Button choiceButton = new Button("Välj StartTyp");
		choiceButton.setOnMouseClicked(event -> {
			for (Skier skier: skierList) {
				if (massStart.isSelected()) {
					skier.setStartType("massStart");
					setStartTime();
				}
				else if (jaktStart.isSelected()) {
					skier.setStartType("jaktStart");
				}
				else if (individuellStart.isSelected()) {
					skier.setStartType("individuellStart");
					setStartTime(15); 
				}
				else {
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("Ingen starttyp har valts!");
					alert.setHeaderText(null);
					alert.setContentText("Välj starttyp!");
					alert.showAndWait();
				}
			}
		});

		startTypeHBox.getChildren().addAll(massStart, individuellStart, jaktStart);
		startTypeHBox.setAlignment(Pos.CENTER);
		startTypeHBox.setMinSize(300, 75);
		startTypeVBox.getChildren().addAll(startTypeHBox, choiceButton);
		startTypeVBox.setAlignment(Pos.CENTER);

		HBox bottomLower = new HBox();
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

		bottomLower.getChildren().addAll(startBtn, stopBtn);
		bottomLower.setAlignment(Pos.CENTER);
		bottomLower.setSpacing(50);
		bottomLower.setPadding(new Insets(20));
		bottomLower.setStyle("-fx-background-color: #fafaa7");
		bottomRegion.getChildren().addAll(startTypeVBox, bottomLower);

		// --- Main Layout ---
		BorderPane borderPane = new BorderPane();
		borderPane.setTop(topRegion);
		borderPane.setCenter(centerRegion); 
		borderPane.setBottom(bottomRegion);

		Scene scene = new Scene(borderPane, 800, 600);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void setStartTime(int interval) {
		for (int i = 0; i < skierList.size(); i++) {
			if (i == 0) {
				skierList.get(i).getTimer().setStartTime(LocalTime.of(00, 00, 00));
			}
			else if (i >= 1) {
				LocalTime previousStartTime = skierList.get(i - 1).getTimer().getStartTime();
				LocalTime newStartTime = previousStartTime.plusSeconds(interval);
				skierList.get(i).getTimer().setStartTime(newStartTime);
				resultsTable.refresh();
			}
		}
	}

	public void setStartTime() {
		for (Skier skier: skierList) {
			skier.getTimer().setStartTime(LocalTime.of(00, 00, 00, 00));
		}
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

	// -- New code 15/12/2024 --
	// Search function to be used for search button in GUI.
	private String search(String input) {
		int searchNum;
		try { searchNum = Integer.parseInt(input);
		for (Skier skier : skierList) {
			ObservableList<Skier> tempList = skierList;
			if (skier.getStartNumber()==searchNum) {
				bubbleSort(tempList);
				for (int i = 0; i < tempList.size(); i++) {
					if (tempList.get(i).getName().equals(skier.getName())) {
						if (i == 0 || i == 1)
							return ("Deltagare med startnummer \"" + searchNum + "\" placerade i " + (i+1) + ":a plats."  );
						else {
							return ("Deltagare med startnummer \"" + searchNum + "\" placerade i " + (i+1) + ":e plats."  );
						}
					}
				}
				return "Ingen deltagare hittades.";
			}
		}
		}  catch (NumberFormatException e) { // Catch the error if the users input cannot be parsed into an integer.
			e.printStackTrace();
		}
		return null;
	}
	// --
	// -- New code 15/12/2024 --
	// This is just recycled code from the race class. The only difference in this method is that it getsFinishTime() instead of getsDeserializedFinishTime().
	// This is due to how deserializedFinishTime is stored as an ObservableSimpleProperty.
	static void bubbleSort(ObservableList<Skier> resultsList) {
		for (int i = 0; i < resultsList.size() - 1; i++) { 
			for (int j = 0; j < resultsList.size() - i - 1; j++) { 
				if (resultsList.get(j).getFinishTime().isAfter(resultsList.get(j + 1).getFinishTime())) {
					Skier temp = resultsList.get(j);
					resultsList.set(j, resultsList.get(j + 1));
					resultsList.set(j + 1, temp);
				}
			}
		}
		for (int i = 0; i < resultsList.size(); i++) { // This will run after the list has been sorted and will assign the skiers a new sorting number.
			resultsList.get(i).setStartNumber(i+1);
		}
	}

	// --
	public static void main(String[] args) {
		launch(args);
	}
}
