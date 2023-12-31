package com.project;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        final int windowWidth = 700;
        final int windowHeight = 600;

        UtilsViews.parentContainer.setStyle("-fx-font: 14 arial;");
        UtilsViews.addView(getClass(), "View0", "/assets/view0.fxml");
        UtilsViews.addView(getClass(), "View1", "/assets/view1.fxml");
        UtilsViews.addView(getClass(), "View2", "/assets/view2.fxml");
        UtilsViews.addView(getClass(), "View3", "/assets/view3.fxml");
        UtilsViews.addView(getClass(), "View4", "/assets/view4.fxml");
        UtilsViews.addView(getClass(), "View5", "/assets/view5.fxml");
        UtilsViews.addView(getClass(), "View6", "/assets/view6.fxml");

        Scene scene = new Scene(UtilsViews.parentContainer);
        
        stage.setScene(scene);
        stage.setTitle("PR1.1 Encriptació en JAVA");
        stage.setMinWidth(windowWidth);
        stage.setMinHeight(windowHeight);
        stage.show();

        // Add icon only if not Mac
        if (!System.getProperty("os.name").contains("Mac")) {
            Image icon = new Image("file:/icons/icon.png");
            stage.getIcons().add(icon);
        }
    }
}