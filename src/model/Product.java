package model;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

/**
 * Product class describes Product object
 * It contains constructor, getters and setters method
 * @author CHUN KAI LI
 */
public class Product {

    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * @param id id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @param name name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param price price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @param stock stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * @param min min to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @param max max to set
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * @return  id to get
     */
    public int getId() {
        return id;
    }

    /**
     * @return name to get
     */
    public String getName() {
        return name;
    }

    /**
     * @return price value to get
     */
    public double getPrice() {
        return price;
    }

    /**
     * @return stock value to get
     */
    public int getStock() {
        return stock;
    }

    /**
     * @return min value to get
     */
    public int getMin() {
        return min;
    }

    /**
     * @return max value to get
     */
    public int getMax() {
        return max;
    }

    /**
     * addAssociatedParts method adds part object to associatedParts list
     * @param part part object to add
     */
    public void addAssociatedParts(Part part) {
        associatedParts.add(part);
    }

    /**
     * deleteAssociatedPart method deletes part object in a associatedParts list
     * @param selectedAssociatedPart  list of associated parts
     * @return return
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        associatedParts.remove(selectedAssociatedPart);
        return true;
    }

    /**
     * getAllAssociatedParts method return all associatedParts list
     * @return return
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }


}
