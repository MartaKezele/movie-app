/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

/**
 *
 * @author kezel
 */
public class User {

    private int id;
    private final String username;
    private final String password;
    private UserRole userRole;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, UserRole userRole) {
        this(username, password);
        this.userRole = userRole;
    }

    public User(int id, String username, String password, UserRole userRole) {
        this(username, password, userRole);
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getUserRole() {
        return userRole;
    }
}
