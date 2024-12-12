package application;

import java.util.ArrayList;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;


public class SkiRaceMenu {
	private MenuBar menuBar = new MenuBar();
	private Menu menu = new Menu("Race type");
	private MenuItem m1 = new MenuItem("Mass Start");
	private MenuItem m2 = new MenuItem("Staggered Start");
	private MenuItem m3 = new MenuItem("Jaktstart");

	public SkiRaceMenu() {
		setEventOnMenuItems();
		menu.getItems().addAll(m1,m2,m3);
		menuBar.getMenus().add(menu);
	}
	
	public MenuBar getMenuBar() {
		return menuBar;
	}

	public void setMenuBar(MenuBar menuBar) {
		this.menuBar = menuBar;
	}

	public MenuItem getM1() {
		return m1;
	}

	public void setM1(MenuItem m1) {
		this.m1 = m1;
	}

	public MenuItem getM2() {
		return m2;
	}

	public void setM2(MenuItem m2) {
		this.m2 = m2;
	}

	public MenuItem getM3() {
		return m3;
	}

	public void setM3(MenuItem m3) {
		this.m3 = m3;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public MenuBar getMenu() {
		return menuBar;
	}
	
	public void setEventOnMenuItems() {
		ArrayList<MenuItem> menuItems = new ArrayList<>();
		menuItems.add(m1);
		menuItems.add(m2);
		menuItems.add(m3);
		
		for (MenuItem menuItem : menuItems) {
			menuItem.setOnAction(e -> {
				menu.setText(menuItem.getText());
			});			
		}
	}
}
