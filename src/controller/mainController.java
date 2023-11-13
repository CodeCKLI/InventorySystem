package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;

import javafx.scene.Parent;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

import model.*;

/**
 * Provides functions and methods for main page
 * @author CHUN KAI LI
 */
public class mainController implements Initializable {

    Stage stage;
    Parent scene;
    public TextField productSearchBar;
    public TextField partSearchBar;
    @FXML
    private TableView<Part> mainAppPartsTable;
    @FXML private TableColumn<Part, Integer> partIDColumn;
    @FXML private TableColumn<Part, String> partNameColumn;
    @FXML private TableColumn<Part, Integer> partInventoryColumn;
    @FXML private TableColumn<Part, Double> partCostColumn;

    @FXML private TableView<Product> mainAppProductsTable;
    @FXML private TableColumn<Product, Integer> productIDColumn;
    @FXML private TableColumn<Product, String> productNameColumn;
    @FXML private TableColumn<Product, Integer> productInventoryColumn;
    @FXML private TableColumn<Product, Double> productCostColumn;


    /**
     * Takes user to add parts page
     * @param event Action event
     * @throws IOException IOException
     */
    @FXML
    void onActionAddParts(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/add-part-view.fxml")));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Takes user to modify parts page
     * also sets the data in the selected part to modify part page
     * @param event Action Event
     * @throws IOException IOException
     */
    @FXML
    void onActionModifyParts(ActionEvent event) throws IOException {
        if (mainAppPartsTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("No part is being selected");
            alert.showAndWait();
            return;
        }
        else {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/mod-part-view.fxml"));
            loader.load();

            modPartController MPController = loader.getController();
            MPController.transferParts(mainAppPartsTable.getSelectionModel().getSelectedIndex(), mainAppPartsTable.getSelectionModel().getSelectedItem());

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * Delete selected part form the part list in Inventory object
     * @param e Action Event
     */
    @FXML
    void onActionDeleteParts(ActionEvent e) {
        if (mainAppPartsTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("No part is being selected");
            alert.showAndWait();
            return;
        }
        Part selectedPart = mainAppPartsTable.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure you want to delete this item?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Inventory.deletePart(selectedPart);
        }
    }

    /**
     * Takes the user to add product page
     * @param event Action Event
     * @throws IOException IOException
     */
    @FXML
    void onActionAddProducts(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/add-prod-view.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Takes the user to modify product page
     * also sets the data in the selected product to modify part page
     * including the associated parts list
     * @param event Action Event
     * @throws IOException IOException
     */
    @FXML
    void onActionModifyProducts(ActionEvent event) throws IOException {
        if (mainAppProductsTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("No product is being selected");
            alert.showAndWait();
            return;
        } else {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/mod-prod-view.fxml"));
            loader.load();

            modProductController MPController = loader.getController();
            MPController.sendProduct(mainAppProductsTable.getSelectionModel().getSelectedIndex(),mainAppProductsTable.getSelectionModel().getSelectedItem());

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * Delete product selected only if there is no associated parts
     * @param e Action Event
     */
    @FXML
    void onActionDeleteProducts(ActionEvent e) {
        if (mainAppProductsTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("No product is being selected");
            alert.showAndWait();
            return;
        }
        Product selectedProduct = mainAppProductsTable.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("Are you sure you want to delete this part?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Product selectedDeleteProduct = mainAppProductsTable.getSelectionModel().getSelectedItem();
            if (selectedDeleteProduct.getAllAssociatedParts().size() > 0) {
                Alert cantDelete = new Alert(Alert.AlertType.ERROR);
                cantDelete.setTitle("Error");
                cantDelete.setContentText("Remove all associated parts before you delete this product.");
                cantDelete.showAndWait();
                return;
            }
            Inventory.deleteProduct(selectedProduct);
        }
    }

    /**
     * Search for parts from user input
     * @param k Key Event
     */
    @FXML
    void onKeyTypePartSearch(KeyEvent k) {
        String searchText = partSearchBar.getText().toLowerCase();
        ObservableList<Part> results = Inventory.lookupPart(searchText);
        try {
            while (results.size() == 0 ) {
                int partID = Integer.parseInt(searchText);
                results.add(Inventory.lookupPart(partID));
            }
            mainAppPartsTable.setItems(results);
        } catch (NumberFormatException e) {
            Alert noPartsFound = new Alert(Alert.AlertType.ERROR);
            noPartsFound.setTitle("Error");
            noPartsFound.setContentText("Part not found");
            noPartsFound.showAndWait();
        }
    }

    /**
     * Search for products from user input on key type
     * FUTURE ENHANCEMENT: When the user typed a name that does
     * not match from any of the parts in the list will
     * pop an error on screen. Future enhancement for this
     * would be a better way to notice the user that
     * no part is found
     * @param k Key Event
     */
    @FXML
    void onKeyTypeProductSearch(KeyEvent k) {
        String searchText = productSearchBar.getText().toLowerCase();
        ObservableList<Product> results = Inventory.lookupProduct(searchText);
        try {
            while (results.size() == 0 ) {
                int productID = Integer.parseInt(searchText);
                results.add(Inventory.lookupProduct(productID));
            }
            mainAppProductsTable.setItems(results);
        } catch (NumberFormatException e) {
            Alert noProductFound = new Alert(Alert.AlertType.ERROR);
            noProductFound.setTitle("Error");
            noProductFound.setContentText("Product not found");
            noProductFound.showAndWait();
        }
    }

    /**
     * Exit from the inventory application
     */
    @FXML
    void mainExitBTN() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to exit for sure?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    /**
     * It initialized the main page, retrieve data from parts list and product list
     * RUNTIME ERROR: can not retrieve property 'id' in propertyvaluefactory
     * Solution: It is caused by new maven JavaFx configurations. This problem is solved by
     * rebuilding and start a brand new the project without using maven configs.
     * @param url url
     * @param resourceBundle resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        mainAppPartsTable.setItems(Inventory.getAllParts());
        partIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partCostColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        mainAppProductsTable.setItems(Inventory.getAllProducts());
        productIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productCostColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

}
