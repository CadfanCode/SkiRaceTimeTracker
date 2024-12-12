package application;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableCell;
import javafx.util.Callback;

import java.time.Duration;
import java.time.format.DateTimeFormatter;

public class Table {
    TableView<Skier> tableView = new TableView<>();
    TableColumn<Skier, Integer> skierIDCol = new TableColumn<>("ID");
    TableColumn<Skier, String> nameCol = new TableColumn<>("Name");
    TableColumn<Skier, String> finishTimeCol = new TableColumn<>("Finish time");
    TableColumn<Skier, Integer> distanceTravelledCol = new TableColumn<>("Distance travelled");
    TableColumn<Skier, String> checkPointTimeCol = new TableColumn<>("Checkpoint time");
    TableColumn<Skier, Duration> timeFromLeaderCol = new TableColumn<>("Time from leader");

    public Table() {
        skierIDCol.setCellValueFactory(new PropertyValueFactory<>("startNumber"));        
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        finishTimeCol.setCellValueFactory(new PropertyValueFactory<>("finishTime"));
        distanceTravelledCol.setCellValueFactory(new PropertyValueFactory<>("distance"));
        checkPointTimeCol.setCellValueFactory(new PropertyValueFactory<>("lastCheckPointTime"));
        timeFromLeaderCol.setCellValueFactory(cellData -> cellData.getValue().getTimeFromLeader());
        
        // Format the Duration for display
        timeFromLeaderCol.setCellFactory(new Callback<TableColumn<Skier, Duration>, TableCell<Skier, Duration>>() {
            @Override
            public TableCell<Skier, Duration> call(TableColumn<Skier, Duration> param) {
                return new TableCell<Skier, Duration>() {
                    @Override
                    protected void updateItem(Duration item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            // Format the Duration as MM:SS (or another format if needed)
                            long seconds = item.getSeconds();
                            long minutes = seconds / 60;
                            seconds = seconds % 60;
                            setText(String.format("%02d:%02d", minutes, seconds));
                        }
                    }
                };
            }
        });
        
        getTableView().getColumns().addAll(skierIDCol, nameCol, distanceTravelledCol, finishTimeCol, checkPointTimeCol, timeFromLeaderCol);
    }

    public TableView<Skier> getTableView() {
        return tableView;
    }

    public void setTableView(TableView<Skier> tableView) {
        this.tableView = tableView;
    }

    public TableColumn<Skier, Integer> getSkierIDCol() {
        return skierIDCol;
    }

    public void setSkierIDCol(TableColumn<Skier, Integer> skierIDCol) {
        this.skierIDCol = skierIDCol;
    }

    public TableColumn<Skier, String> getNameCol() {
        return nameCol;
    }

    public void setNameCol(TableColumn<Skier, String> nameCol) {
        this.nameCol = nameCol;
    }

    public TableColumn<Skier, String> getFinishTimeCol() {
        return finishTimeCol;
    }

    public void setFinishTimeCol(TableColumn<Skier, String> finishTimeCol) {
        this.finishTimeCol = finishTimeCol;
    }

    public TableColumn<Skier, Integer> getDistanceTravelledCol() {
        return distanceTravelledCol;
    }

    public void setDistanceTravelledCol(TableColumn<Skier, Integer> distanceTravelledCol) {
        this.distanceTravelledCol = distanceTravelledCol;
    }

    public TableColumn<Skier, String> getCheckPointTimeCol() {
        return checkPointTimeCol;
    }

    public void setCheckPointTimeCol(TableColumn<Skier, String> checkPointTimeCol) {
        this.checkPointTimeCol = checkPointTimeCol;
    }

    public TableColumn<Skier, Duration> getTimeFromLeaderCol() {
        return timeFromLeaderCol;
    }

    public void setTimeFromLeaderCol(TableColumn<Skier, Duration> timeFromLeaderCol) {
        this.timeFromLeaderCol = timeFromLeaderCol;
    }
}
