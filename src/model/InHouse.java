package model;

/**
 * InHouse class describes InHouse object
 * It extends Part class
 * It contains constructor, getters and setters method
 * @author CHUN KAI LI
 */
public class InHouse extends Part {
    private int machineID;

    public InHouse(int id, String name, double price, int stock, int min, int max, int machineID) {
        super(id, name, price, stock, min, max);
        this.machineID = machineID;
    }

    /**
     * Sets MachineID
     * @param machineID the id to se machineID
     */
    public void setMachineID(int machineID) {
        this.machineID = machineID;
    }

    /**
     * Gets MachineID
     * @return machineID
     */
    public int getMachineID() {
        return machineID;
    }

}
