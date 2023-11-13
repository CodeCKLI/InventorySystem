package model;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

/**
 * Inventory class describes Inventory object
 * It contains constructor, getters and setters method
 * It stores the parts and products in separate observable lists
 * @author CHUN KAI LI
 */
public class Inventory {

    //Create two lists for containing Parts and Products objects
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * addPart method adds Parts into allParts ObservableList
     * @param newPart Part to add
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * addProduct method adds Product into allProducts ObservableList
     * @param newProduct Product to add
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }


    /**
     * lookupPart method is for looking up parts in the Inventory object with id
     * @param partId part id to lookup
     * @return null
     */
    public static Part lookupPart(int partId) {
        for(Part part: Inventory.getAllParts()) {
            if (part.getId() == partId) {
                return part;
            }
        }
        return null;
    }


    /**
     * lookupProduct method is for looking up products in the Inventory object with id
     * @param productId product id to lookup
     * @return null
     */
    public static Product lookupProduct(int productId) {
        for(Product product: Inventory.getAllProducts()){
            if (product.getId() == productId)
                return product;
        }
        return null;
    }

    /**
     * This is the lookupPart method that takes String as an input
     * @param partName part name to lookup
     * @return found parts
     */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> partsFound = FXCollections.observableArrayList();
        for (Part part: allParts) {
            if (part.getName().toLowerCase().contains(partName)) {
                partsFound.add(part);
            }
        }
        return partsFound;
    }

    /**
     * This is the lookupProduct method that takes String as an input
     * @param productName product name to lookup
     * @return found products
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> productsFound = FXCollections.observableArrayList();
        for (Product product: allProducts) {
            if (product.getName().toLowerCase().contains(productName)) {
                productsFound.add(product);
            }
        }
        return productsFound;
    }


    /**
     * updatePart method takes part index and a part object as input and update respective part object
     * @param index index of parts
     * @param selectedPart part object selected
     */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     * updateProduct method takes product index and a product object as input and update respective product object
     * @param index index of products
     * @param newProduct product object selected
     */
    public static void updateProduct(int index, Product newProduct) {
        allProducts.set(index, newProduct);
    }

    /**
     * deletePart method takes a part object and delete it from the list
     * @param selectedPart part selected to be deleted
     * @return return
     */
    public static boolean deletePart (Part selectedPart) {
        if(allParts.contains(selectedPart)) {
            allParts.remove(selectedPart);
            return true;
        } else {
            return false;
        }
    }


    /**
     * deleteProduct method takes a product object and delete it from the list
     * @param selectedProduct products selected to be deleted
     * @return return
     */
    public static boolean deleteProduct (Product selectedProduct) {
        if(allProducts.contains(selectedProduct)) {
            allProducts.remove(selectedProduct);
            return true;
        } else {
            return false;
        }
    }


    /**
     * getAllParts method return all parts in the list
     * @return allParts list
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * getAllProducts method return all products in the list
     * @return allProducts list
     */
    public static ObservableList<Product> getAllProducts(){
        return allProducts;
    }

}
