package model;

/**
 * Outsourced class describe the Outsourced objects that extends part objects
 * with a constructor, companyName getter and setter
 * @author CHUN KAI LI
 */
public class Outsourced extends Part {
    private String companyName;

    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * Company Name getter
     * @return Company Name
     */
    public String getCompanyName() {

        return companyName;
    }

    /**
     * Company Name setter
     * @param companyName company name to be set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

}
