package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.*;

/**
 * Provides add product functions and methods for this inventory application
 * @author CHUN KAI LI
 */
public class addProductController implements Initializable {

    public TextField addProductId;
    public TextField addProductName;
    public TextField addProductInventory;
    public TextField addProductPrice;
    public TextField addProductMax;
    public TextField addProductMin;
    public TextField addProductSearch;
    private ObservableList<Part> associatedPartsList = FXCollections.observableArrayList();

    public TableView addProductPartsTable;
    public TableColumn addProductPartsId;
    public TableColumn addProductPartsName;
    public TableColumn addProductPartsInventory;
    public TableColumn addProductPartsPrice;
    public TableView associatedPartsTable;
    public TableColumn associatedPartsId;
    public TableColumn associatedPartsName;
    public TableColumn associatedPartsInventory;
    public TableColumn associatedPartsPrice;

    /**
     * Search for parts and shows the parts found in product table
     * @param k Key Event
     */
    @FXML
    void onKeyTypePartSearch(KeyEvent k) {
        String searchText = addProductSearch.getText().toLowerCase();
        ObservableList<Part> results = Inventory.lookupPart(searchText);
        try {
            while (results.size() == 0 ) {
                int partID = Integer.parseInt(searchText);
                results.add(Inventory.lookupPart(partID));
            }
            addProductPartsTable.setItems(results);
        } catch (NumberFormatException e) {
            Alert noPartsFound = new Alert(Alert.AlertType.ERROR);
            noPartsFound.setTitle("Error Message");
            noPartsFound.setContentText("Part not found");
            noPartsFound.showAndWait();
        }
    }

    /**
     * Saves product into product list in inventory object
     * LOGICAL ERROR: All product shares the same associatedPartsLists object
     * Correction: The reason all product shares the same associatedPartsList object
     * was because I made the associatedParts in product class a static ObservableList
     * I made it non-static and private field, the logical error was fixed
     * @param event action event
     * @throws IOException IOException
     */
    @FXML
    void onActionSaveBTNPushed(ActionEvent event) throws IOException {
        try {

            int id = Integer.parseInt(addProductId.getText());
            String name = addProductName.getText();
            int stock = Integer.parseInt(addProductInventory.getText());
            double price = Double.parseDouble(addProductPrice.getText());
            int max = Integer.parseInt(addProductMax.getText());
            int min = Integer.parseInt(addProductMin.getText());

            if (stock < min || stock > max) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Stock must be within the range of min and max.");
                alert.showAndWait();
                return;
            } else if (min >= max) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "The value of Max must be greater than Min.");
                alert.showAndWait();
                return;
            }

            Product product = new Product(id, name, price, stock, min, max);

            for (Part part: associatedPartsList) {
                if (part != associatedPartsList)
                    product.addAssociatedParts(part);
            }

            Inventory.getAllProducts().add(product);
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("../view/main-view.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Incorrect input value");
            alert.showAndWait();
            return;
        }
    }

    /**
     * Takes the user back to main page
     * @param event action event
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
     *Takes selected part then add it into associatedPartsList and show it on associatedPartsTable
     * @param event Action Event
     */
    @FXML
    void onActionAddProduct(ActionEvent event) {
        Part selectedPart = (Part) addProductPartsTable.getSelectionModel().getSelectedItem();

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
     * Remove selected part in associatedPartsTable and associatedPartsList
     * @param event action event
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
        if (associatedPartsList.contains(selectedPart))
        {
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
     * initialize the add product page
     * @param url url
     * @param resourceBundle resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        addProductPartsTable.setItems(Inventory.getAllParts());
        addProductPartsId.setCellValueFactory(new PropertyValueFactory<>("id"));
        addProductPartsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        addProductPartsInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addProductPartsPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        associatedPartsTable.setItems(associatedPartsList);
        associatedPartsId.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartsInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartsPrice.setCellValueFactory(new PropertyValueFactory<>("price"));


        int uniqueId = (int) (Math.random() * 1000);
        addProductId.setText(String.valueOf(uniqueId));

    }
}
