package controller;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;

import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.stage.Stage;

import model.*;


/**
 * addPartController class provides methods for add part function of the inventory application
 * @author CHUN KAI LI
 */
public class addPartController implements Initializable {

    public Label MachineIDorCompany;
    public TextField partId;
    public TextField partName;
    public TextField partInventory;
    public TextField partPrice;
    public TextField partMax;
    public TextField partLastField;
    public TextField partMin;
    public RadioButton inHouseRadio;
    public RadioButton OutsourcedRadio;

    /**
     * onActionAddPartInHouse method sets label "MachineIDorCompany" to "Machine ID"
     * @param event action event
     */
    @FXML
    void onActionAddPartInHouse(ActionEvent event) {

        MachineIDorCompany.setText("Machine ID");
    }

    /**
     * onActionAddPartOutsourced method sets label "MachineIDorCompany" to "Company Name"
     * @param event action event
     */
    @FXML
    void onActionAddPartOutsourced(ActionEvent event) {

        MachineIDorCompany.setText("Company Name");
    }

    /**
     * partSaveButton class saves the data user put in into the part list in inventory object
     * @param event action event
     * @throws IOException IOException
     */
    @FXML
    void partSaveButton(ActionEvent event) throws IOException {

        try{
            int id = Integer.parseInt(partId.getText());
            String name = partName.getText();
            int stock = Integer.parseInt(partInventory.getText());
            double price = Double.parseDouble(partPrice.getText());
            int max = Integer.parseInt(partMax.getText());
            int min = Integer.parseInt(partMin.getText());

            int machineID;
            String companyName;

            if (max < min) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "The value of Max must be greater than Min.");
                alert.showAndWait();
                return;
            }
            else if (stock < min || max < stock) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Stock must be within the range of min and max.");
                alert.showAndWait();
                return;
            }

            if (OutsourcedRadio.isSelected()) {
                companyName = partLastField.getText();
                Outsourced addPart = new Outsourced(id, name, price, stock, min, max, companyName);
                Inventory.addPart(addPart);
            }
            if (inHouseRadio.isSelected()) {
                machineID = Integer.parseInt(partLastField.getText());
                InHouse addPart = new InHouse(id, name, price, stock, min, max, machineID);
                Inventory.addPart(addPart);
            }
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("../view/main-view.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();

        } catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Incorrect input value");
            alert.showAndWait();
            return;
        }

    }

    /**
     * Takes user back to main page
     * @param event action event
     * @throws IOException IOException
     */
    public void partCancelButton (ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../view/main-view.fxml"));
        Scene scene = new Scene(root);
        Stage MainScreenReturn = (Stage)((Node)event.getSource()).getScene().getWindow();
        MainScreenReturn.setScene(scene);
        MainScreenReturn.show();

    }

    /**
     * initialize the add parts page
     * LOGICAL ERROR: Both radio buttons are not selected when page is initiated
     * Correction: I set up an if then statement to check if any of the radio buttons are selected
     * if none is selected then make inHouseRadio to be selected as default
     * @param url url
     * @param resourceBundle resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int uniqueId = (int) (Math.random() * 1000);
        partId.setText(String.valueOf(uniqueId));
        if (inHouseRadio.isSelected() || OutsourcedRadio.isSelected()) {
            return;
        }else{
            inHouseRadio.setSelected(true);
        }
    }
}
