module CareerCraft {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.fxml;
	requires org.mongodb.driver.core;
	requires org.mongodb.bson;
	requires org.mongodb.driver.sync.client;
	requires de.jensd.fx.glyphs.fontawesome;
	requires java.desktop;
	requires javafx.base;
	
	opens application to javafx.graphics, javafx.fxml;
	opens Controller to javafx.fxml;
}
