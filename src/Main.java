
/**
 * @author CHUN KAI LI
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;

/**
 * Main class sets up the application
 */
public class Main extends Application {
    /**
     * main method launches the application
     * @param args args
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * start method loads mock-up data and setting the stage for users
     * @param stage taks stage objects
     * @throws Exception Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1100, 550);
        stage.setTitle("C482-InventorySystem-CHUNKAI");
        stage.setScene(scene);
        stage.show();

        Inventory.addPart(new Outsourced(13, "GPU One", 11, 25, 20, 30, "XX"));
        Inventory.addPart(new Outsourced(212, "CPU X Series", 22, 25, 20, 30, "MacroSoft"));
        Inventory.addPart(new InHouse(533, "All New All powerful monitor One", 33, 25, 20, 30, 99));
        Inventory.addPart(new InHouse(94, "The Best RAM of all time X", 44, 25, 20, 30, 98));
        Inventory.addPart(new InHouse(872, "Mother Board Pro Max Fourth Gen", 44, 25, 20, 30, 63));

        Inventory.addProduct(new Product(51,"Gaming Laptop", 999, 100, 20, 200));
        Inventory.addProduct(new Product(512,"Digital Drawing Desktop", 123, 56, 12, 200));
        Inventory.addProduct(new Product(723,"Programming Desktop", 321, 78, 45, 122));
    }
}