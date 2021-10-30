/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p2;

/**
 *
 * @author Andrés
 */
public class User {
    private String name;
    private String phone;
    private String email;

    public User(String name, String telefono, String email) {
        this.name = name;
        this.phone = telefono;
        this.email = email;
    }

  
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPHone(String telefono) {
        this.phone = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
        
    
    
}