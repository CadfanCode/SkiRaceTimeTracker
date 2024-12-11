package application;

import java.util.ArrayList;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class Table {
	TableView tableView = new TableView();
	TableColumn skierIDCol = new TableColumn("ID");
	TableColumn nameCol = new TableColumn("Name");
	TableColumn finishTimeCol = new TableColumn("Finish time");
	TableColumn distanceTravelledCol = new TableColumn("Distance travelled");
	TableColumn CheckPointTimeCol = new TableColumn("Checkpoint time");
	
	public Table() {
        skierIDCol.setCellValueFactory(new PropertyValueFactory<>("startNumber"));        
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        finishTimeCol.setCellValueFactory(new PropertyValueFactory<>("finishTime"));
        distanceTravelledCol.setCellValueFactory(new PropertyValueFactory<>("distance"));
        CheckPointTimeCol.setCellValueFactory(new PropertyValueFactory<>("lastCheckPointTime"));
        getTableView().getColumns().addAll(skierIDCol, nameCol, distanceTravelledCol, finishTimeCol, CheckPointTimeCol);
	}
	
	public TableView getTableView() {
		return tableView;
	}

	public void setTableView(TableView tableView) {
		this.tableView = tableView;
	}

	public TableColumn getSkierIDCol() {
		return skierIDCol;
	}

	public void setSkierIDCol(TableColumn skierIDCol) {
		this.skierIDCol = skierIDCol;
	}

	public TableColumn getNameCol() {
		return nameCol;
	}

	public void setNameCol(TableColumn nameCol) {
		this.nameCol = nameCol;
	}

	public TableColumn getFinishTimeCol() {
		return finishTimeCol;
	}

	public void setFinishTimeCol(TableColumn finishTimeCol) {
		this.finishTimeCol = finishTimeCol;
	}

	public TableColumn getDistanceTravelledCol() {
		return distanceTravelledCol;
	}

	public void setDistanceTravelledCol(TableColumn distanceTravelledCol) {
		this.distanceTravelledCol = distanceTravelledCol;
	}

	public TableColumn getCheckPointTimeCol() {
		return CheckPointTimeCol;
	}

	public void setCheckPointTimeCol(TableColumn CheckPointTimeCol) {
		this.CheckPointTimeCol = CheckPointTimeCol;
	}


	
	
}
