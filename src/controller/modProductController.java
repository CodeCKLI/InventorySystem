package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Inventory;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Provides functions and methods for modify product page
 * @author CHUN KAI LI
 */
public class modProductController implements Initializable {
    private int index = 0;
    private ObservableList<Part> associatedPartsList = FXCollections.observableArrayList();
    public TextField modProductSearch;
    public TextField modProductId;
    public TextField modProductName;
    public TextField modProductInventory;
    public TextField modProductPrice;
    public TextField modProductMax;
    public TextField modProductMin;
    public TableView modProductPartsTable;
    public TableColumn modProductPartsId;
    public TableColumn modProductPartsName;
    public TableColumn modProductPartsInventory;
    public TableColumn modProductPartsPrice;
    public TableView associatedPartsTable;
    public TableColumn associatedPartsId;
    public TableColumn associatedPartsName;
    public TableColumn associatedPartsInventory;
    public TableColumn associatedPartsPrice;

    /**
     * Saves the modified data to the product object
     * RUNTIME ERROR: java.lang.reflect.InvocationTargetException
     * which was caused by incorrect view page path in the getResource
     * function of the fxml loader.
     * Correction: Change the path to relative path fixed the error
     * @param event Action Event
     * @throws IOException IOException
     */
    @FXML
    void onActionProductSaveBTN(ActionEvent event) throws IOException {
        try {
            int id = Integer.parseInt(modProductId.getText());
            String name = modProductName.getText();
            int stock = Integer.parseInt(modProductInventory.getText());
            double price = Double.parseDouble(modProductPrice.getText());
            int max = Integer.parseInt(modProductMax.getText());
            int min = Integer.parseInt(modProductMin.getText());

            if (stock > max || stock < min) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Stock must be within the range of min and max.");
                alert.showAndWait();
                return;
            } else if (min >= max) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "The value of Max must be greater than Min.");
                alert.showAndWait();
                return;
            }
            Product updatedProduct = new Product(id, name, price, stock, min, max);
            Inventory.updateProduct(index, updatedProduct);

            for (Part part: associatedPartsList) {
                updatedProduct.addAssociatedParts(part);
            }

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("../view/main-view.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Incorrect value");
            alert.showAndWait();
            return;
        }
    }

    /**
     * Search for the parts from user input
     * @param k Key Event
     */
    @FXML
    void onKeyTypePartSearch(KeyEvent k) {
        String searchText = modProductSearch.getText().toLowerCase();
        ObservableList<Part> results = Inventory.lookupPart(searchText);
        try {
            while (results.size() == 0 ) {
                int partID = Integer.parseInt(searchText);
                results.add(Inventory.lookupPart(partID));
            }
            modProductPartsTable.setItems(results);
        } catch (NumberFormatException e) {
            Alert noPartsFound = new Alert(Alert.AlertType.ERROR);
            noPartsFound.setTitle("Error Message");
            noPartsFound.setContentText("Part not found");
            noPartsFound.showAndWait();
        }
    }

    /**
     * Adds the part from parts table to associated part list
     * @param event Action Event
     */
    @FXML
    void onActionAddProduct(ActionEvent event) {
        Part selectedPart = (Part) modProductPartsTable.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please select a part from the list above");
            alert.showAndWait();
            return;
        }
        if (associatedPartsList.contains(selectedPart)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Selected part has already been added");
            alert.showAndWait();
            return;
        }
        if (!associatedPartsList.contains(selectedPart)) {
            associatedPartsList.add(selectedPart);
            associatedPartsTable.setItems(associatedPartsList);
        }

    }

    /**
     * Remove selected part in the associated parts list
     * @param event Action Event
     */
    @FXML
    void onActionRemoveAssociatedPartsBTN(ActionEvent event) {
        Part selectedPart = (Part) associatedPartsTable.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please select part from the list");
            alert.showAndWait();
            return;
        }
        if (associatedPartsList.contains(selectedPart)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Are you sure you want to delete this item?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                associatedPartsList.remove(selectedPart);
                associatedPartsTable.setItems(associatedPartsList);
            }

        }
    }

    /**
     * Takes the user back to main page
     * @param event Action Event
     * @throws IOException IOException
     */
    @FXML
    public void onActionCancelBTNpushed(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../view/main-view.fxml"));
        Scene scene = new Scene(root);
        Stage MainScreenReturn = (Stage)((Node)event.getSource()).getScene().getWindow();
        MainScreenReturn.setScene(scene);
        MainScreenReturn.show();
    }

    /**
     * This method is provided for the main page to
     * send selected product data to the modify product page
     * @param selectedIndex selectedIndex
     * @param product product
     */
    @FXML
    public void sendProduct(int selectedIndex, Product product){

        index = selectedIndex;
        modProductId.setText(String.valueOf(product.getId()));
        modProductName.setText(String.valueOf(product.getName()));
        modProductInventory.setText(String.valueOf(product.getStock()));
        modProductPrice.setText(String.valueOf(product.getPrice()));
        modProductMax.setText(String.valueOf(product.getMax()));
        modProductMin.setText(String.valueOf(product.getMin()));

        for (Part part: product.getAllAssociatedParts()) {
            associatedPartsList.add(part);
        }
    }

    /**
     * Initialize parts table and associated parts table from the
     * product selected from the main page
     * @param url url
     * @param resourceBundle resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        modProductPartsTable.setItems(Inventory.getAllParts());
        modProductPartsId.setCellValueFactory(new PropertyValueFactory<>("id"));
        modProductPartsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        modProductPartsInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modProductPartsPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        associatedPartsTable.setItems(associatedPartsList);
        associatedPartsId.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartsInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartsPrice.setCellValueFactory(new PropertyValueFactory<>("price"));


        int uniqueId = (int) (Math.random() * 1000);
        modProductPartsId.setText(String.valueOf(uniqueId));
    }
}
