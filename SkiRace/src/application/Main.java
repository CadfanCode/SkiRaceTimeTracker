package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.Random;

public class Main extends Application {
// THIS IS A COMMENT WRITTEN IN INTELIJ
	// Fields
	TextField nameField;
	VBox raceDistanceVBox;
	TilePane startTypeTilePane;
	CheckBox startIntervalCheckbox;
	ObservableList<Athlete> athleteList;
	int dist;
	Button startBtn;
	Button stopBtn;
	boolean raceInProgress;
	TableView<Athlete> resultsTable = new TableView<>();

	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setTitle("SkiRaceTracker");

			// Initialize the list of athletes
			athleteList = FXCollections.observableArrayList();

			// --- Top Region ---

			// Name field
			VBox nameFieldVBox = new VBox();
			Label nameFieldLabel = new Label("Enter Athletes Name");
			nameFieldLabel.setAlignment(Pos.CENTER_LEFT);
			nameField = new TextField();
			nameField.setMinWidth(200);
			nameFieldVBox.getChildren().addAll(nameFieldLabel, nameField);
			nameFieldVBox.setAlignment(Pos.CENTER);
			nameFieldVBox.setPadding(new Insets(20));
			nameFieldVBox.setMinSize(200, 75);
			nameFieldVBox.setSpacing(20);

			// Race distance
			raceDistanceVBox = new VBox();
			Label raceDistaneLabel = new Label("Choose distance:");
			CheckBox k5 = new CheckBox("5 km");
			CheckBox k10 = new CheckBox("10 km");
			CheckBox k20 = new CheckBox("20 km");
			CheckBox k40 = new CheckBox("40 km");
			raceDistanceVBox.getChildren().addAll(raceDistaneLabel, k5, k10, k20, k40);
			raceDistanceVBox.setPadding(new Insets(20));
			raceDistanceVBox.setMinSize(200, 100);
			raceDistanceVBox.setAlignment(Pos.CENTER);
			raceDistanceVBox.setSpacing(1);

			// Start type
			startTypeTilePane = new TilePane();
			CheckBox massStart = new CheckBox("Mass Start");
			CheckBox staggeredStart = new CheckBox("Staggered Start");
			CheckBox jaktStart = new CheckBox("Jaktstart");
			startTypeTilePane.getChildren().addAll(massStart, staggeredStart, jaktStart);
			startTypeTilePane.setAlignment(Pos.CENTER);
			startTypeTilePane.setMinSize(300, 75);

			// Add button
			Button addButton = new Button("Add");
			addButton.setOnMouseClicked(event -> {
				if (!nameField.getText().isEmpty() && k5.isSelected() && massStart.isSelected()) {
					athleteList.add(new Athlete(nameField.getText(), "massStart", 5));
				} else if (!nameField.getText().isEmpty() && k10.isSelected() && massStart.isSelected()) {
					athleteList.add(new Athlete(nameField.getText(), "massStart", 10));
				} else if (!nameField.getText().isEmpty() && k20.isSelected() && massStart.isSelected()) {
					athleteList.add(new Athlete(nameField.getText(), "massStart", 20));
				} else if (!nameField.getText().isEmpty() && k40.isSelected() && massStart.isSelected()) {
					athleteList.add(new Athlete(nameField.getText(), "massStart", 40));
				} else if (!nameField.getText().isEmpty() && k5.isSelected() && staggeredStart.isSelected()) {
					athleteList.add(new Athlete(nameField.getText(), "staggeredStart", 5));
				} else if (!nameField.getText().isEmpty() && k10.isSelected() && staggeredStart.isSelected()) {
					athleteList.add(new Athlete(nameField.getText(), "staggeredStart", 10));
				} else if (!nameField.getText().isEmpty() && k20.isSelected() && staggeredStart.isSelected()) {
					athleteList.add(new Athlete(nameField.getText(), "staggeredStart", 20));
				} else if (!nameField.getText().isEmpty() && k40.isSelected() && staggeredStart.isSelected()) {
					athleteList.add(new Athlete(nameField.getText(), "staggeredStart", 40));
				} else if (!nameField.getText().isEmpty() && k5.isSelected() && jaktStart.isSelected()) {
					athleteList.add(new Athlete(nameField.getText(), "jaktStart", 5));
				} else if (!nameField.getText().isEmpty() && k10.isSelected() && jaktStart.isSelected()) {
					athleteList.add(new Athlete(nameField.getText(), "jaktStart", 10));
				} else if (!nameField.getText().isEmpty() && k20.isSelected() && jaktStart.isSelected()) {
					athleteList.add(new Athlete(nameField.getText(), "jaktStart", 20));
				} else if (!nameField.getText().isEmpty() && k40.isSelected() && jaktStart.isSelected()) {
					athleteList.add(new Athlete(nameField.getText(), "jaktStart", 40));
				}
			});

			HBox innerTopBox = new HBox();
			innerTopBox.getChildren().addAll(nameFieldVBox, raceDistanceVBox, startTypeTilePane);

			HBox outerTopBox = new HBox(addButton);
			outerTopBox.setAlignment(Pos.BOTTOM_RIGHT);
			outerTopBox.setPadding(new Insets(10));

			VBox topRegion = new VBox(innerTopBox, outerTopBox);
			topRegion.setStyle("-fx-background-color: #fafaa7");

			// --- Center Region ---
			TableColumn<Athlete, String> nameCol = new TableColumn<>("Name");
			nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

			TableColumn<Athlete, Double> currentTimeCol = new TableColumn<>("Current time");
			currentTimeCol.setCellValueFactory(new PropertyValueFactory<>("timeElapsed"));

			TableColumn<Athlete, Integer> distanceTravelledCol = new TableColumn<>("Distance travelled");
			distanceTravelledCol.setCellValueFactory(new PropertyValueFactory<>("distanceTravelled"));

			TableColumn<Athlete, Double> timeFromLeaderCol = new TableColumn<>("Time from leader");
			timeFromLeaderCol.setCellValueFactory(new PropertyValueFactory<>("timeFromLeader"));

			resultsTable.getColumns().addAll(nameCol, distanceTravelledCol, currentTimeCol, timeFromLeaderCol);
			resultsTable.setItems(athleteList);

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

			startBtn.setOnMouseClicked(event -> {raceInProgress = true; startRace();});
			stopBtn.setOnMouseClicked(event -> stopRace());

			bottomRegion.getChildren().addAll(startBtn, stopBtn);
			bottomRegion.setAlignment(Pos.CENTER);
			bottomRegion.setSpacing(50);
			bottomRegion.setPadding(new Insets(20));
			bottomRegion.setStyle("-fx-background-color: #fafaa7");

			// --- Main Layout ---
			BorderPane borderPane = new BorderPane();
			borderPane.setTop(topRegion);
			borderPane.setCenter(resultsTable);
			borderPane.setBottom(bottomRegion);

			Scene scene = new Scene(borderPane, 800, 500);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


		public void startRace() {
		    new Thread(() -> {
		        while (raceInProgress) {
		            Platform.runLater(() -> {
		                if (!athleteList.isEmpty() && athleteList.size() > 1) {
		                    Random rand = new Random();
		                    Athlete leader = findLeader(athleteList);
		                    for (Athlete athlete : athleteList) {
		                        int randomDistance = 50 + rand.nextInt(51);
		                        athlete.setDistanceTravelled(athlete.getDistanceTravelled() + randomDistance);

		                        athlete.setTimeElapsed(athlete.getTimeElapsed() + 20);
		                        // Time= Distance/Speed.
		                        double timeDifference = (leader.getDistanceTravelled() - athlete.getDistanceTravelled()) * 1.0 / leader.getRaceDistance() * leader.getTimeElapsed(); 
		                        athlete.setTimeFromLeader(timeDifference);
		                        athlete.setTimeFromLeader(timeDifference);
		                    }
		                }
		                resultsTable.refresh();
		            });

		            try {
		                Thread.sleep(10000); // Wait for 10 seconds
		            } catch (InterruptedException e) {
		                e.printStackTrace();
		            }
		        }
		    }).start();
		}
		
		public void stopRace() {
			raceInProgress = false;
		}

	
	// Helper method to find lead athlete.
	public Athlete findLeader(ObservableList<Athlete> athleteList) {
		if (athleteList.isEmpty()) {
		return null;
		}
		Athlete leader = athleteList.get(0);
		
		for (int i = 0; i < athleteList.size(); i++) {
			Athlete currentAthlete = athleteList.get(i);
			if (currentAthlete.getDistanceTravelled() > leader.getDistanceTravelled()) {
				leader = currentAthlete;
			}
		}
		return leader;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
