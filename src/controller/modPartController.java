package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;

import java.io.IOException;

/**
 * Provides methods and functions for modifying parts page
 * @author CHUN KAI LI
 */
public class modPartController{

    public TextField partId;
    public TextField partName;
    public TextField partInventory;
    public TextField partPrice;
    public TextField partMax;
    public TextField partLastField;
    public TextField partMin;
    public Label MachineIDorCompany;
    public RadioButton modpartsInHouseRadio;
    public RadioButton modpartsOutSourcedRadio;

    //Create index field for part index
    private int index = 0;

    /**
     * Takes the changes made by user and save it to the part list
     * @param event Action Event
     * @throws IOException IOException
     */
    @FXML
    void modPartSaveButton(ActionEvent event) throws IOException {
        try {
            int partID = Integer.parseInt(partId.getText());
            String name = partName.getText();
            int inStock = Integer.parseInt(partInventory.getText());
            double price = Double.parseDouble(partPrice.getText());
            int max = Integer.parseInt(partMax.getText());
            int min = Integer.parseInt(partMin.getText());
            int machineID;

            String companyName;

            if (max < min) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "The value of Max must be greater than Min.");
                alert.showAndWait();
                return;
            } else if (inStock < min || max < inStock) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Stock must be within the range of min and max.");
                alert.showAndWait();
                return;
            }

            if (modpartsInHouseRadio.isSelected()) {
                machineID = Integer.parseInt(partLastField.getText());
                InHouse updatedPart = new InHouse(partID, name, price, inStock, min, max, machineID);
                Inventory.updatePart(index, updatedPart);
            }
            if (modpartsOutSourcedRadio.isSelected()) {
                companyName = partLastField.getText();
                Outsourced updatedPart = new Outsourced(partID, name, price, inStock, min, max, companyName);
                Inventory.updatePart(index, updatedPart);
            }
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("../view/main-view.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Incorrect input value");
            alert.showAndWait();
            return;
        }
    }

    /**
     * This is a method for main page to transfer parts data from main page parts table
     * to modify page input fields
     * @param selectedIndex SelectedIndex
     * @param part Part
     */
    public void transferParts(int selectedIndex, Part part){

        index = selectedIndex;

        if (part instanceof InHouse) {
            modpartsInHouseRadio.setSelected(true);
            MachineIDorCompany.setText("MachineID");
            partLastField.setText(String.valueOf(((InHouse) part).getMachineID()));
        }
        else {
            modpartsOutSourcedRadio.setSelected(true);
            MachineIDorCompany.setText("CompanyName");
            partLastField.setText(((Outsourced) part).getCompanyName());
        }

        partId.setText(String.valueOf(part.getId()));
        partName.setText(String.valueOf(part.getName()));
        partInventory.setText(String.valueOf(part.getStock()));
        partPrice.setText(String.valueOf(part.getPrice()));
        partMax.setText(String.valueOf(part.getMax()));
        partMin.setText(String.valueOf(part.getMin()));
    }


    /**
     * sets the label MachineIDorCompany to "Machine ID"
     * @param event Action Event
     */
    @FXML
    public void onActionPartInHouse(ActionEvent event) {

        MachineIDorCompany.setText("Machine ID");
    }

    /**
     * sets the label MachineIDorCompany to "Company Name"
     * @param event Action Event
     */
    @FXML
    public void onActionPartOutsourced(ActionEvent event) {

        MachineIDorCompany.setText("Company Name");
    }

    /**
     * Takes user back to main page
     * @param event Action Event
     * @throws IOException IOException
     */
    public void partCancelButton (ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("../view/main-view.fxml"));
        Scene scene = new Scene(root);
        Stage MainScreenReturn = (Stage)((Node)event.getSource()).getScene().getWindow();
        MainScreenReturn.setScene(scene);
        MainScreenReturn.show();
    }

}
