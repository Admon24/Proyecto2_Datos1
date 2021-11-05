
package p2;

/**
 * Creates the data for the CSV
 * @author Andrés & Adrián
 */
public class User {
    private String operation;
    private String result;
    private String date;
/**
 * Declares the operation, result and date
 * @param operation
 * @param result
 * @param date 
 */
    public User(String operation, String result, String date) {
        this.operation = operation;
        this.result = result;
        this.date = date;
    }

  /**
   * Gets the operation
   * @return 
   */
    public String getOp() {
        return operation;
    }
/**
 * Sets the operation
 * @param operation 
 */
    public void setOp(String operation) {
        this.operation = operation;
    }
/**
 * Gets the result
 * @return 
 */
    public String getResult() {
        return result;
    }
/**
 * Sets the result
 * @param result 
 */
    public void setResult(String result) {
        this.result = result;
    }
/**
 * Gets the date
 * @return 
 */
    public String getDate() {
        return date;
    }
/**
 * Sets the date
 * @param date 
 */
    public void setDate(String date) {
        this.date = date;
    }
        
    
    
}