
package p2;

public class User {
    private String operation;
    private String result;
    private String date;

    public User(String operation, String result, String date) {
        this.operation = operation;
        this.result = result;
        this.date = date;
    }

  
    public String getOp() {
        return operation;
    }

    public void setOp(String operation) {
        this.operation = operation;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
        
    
    
}