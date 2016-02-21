/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.khmt.restful;

/**
 *
 * @author Phuo
 */
public class User {
    public String name;
    public int status;
    public String username;
    public String email;
    public int userid;
    public User() {} // JAXB needs this
 
    public User(String u, String n, String e, int s, int i) {
        this.name = n;
        this.status = s;
        this.username = u;
        this.email = e;
        this.userid = i;
    }
}
