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
public enum UserRole {

    ADMINISTRATOR,
    BASIC;

    public static UserRole from(String name) {

        for (UserRole value : values()) {
            if (value.name().equals(name)) {
                return value;
            }
        }

        throw new IllegalArgumentException("No such user role.");
    }

    public static final String ID_USER_ROLE = "IDUserRole";
    public static final String NAME = "Name";

    public static final String CREATE_USER_ROLE = "{ CALL createUserRole (?, ?) }";
    public static final String UPDATE_USER_ROLE = "{ CALL updateUserRole (?, ?) }";
    public static final String DELETE_USER_ROLE = "{ CALL deleteUserRole (?) }";
    public static final String SELECT_USER_ROLE = "{ CALL selectUserRole (?) }";
    public static final String SELECT_USER_ROLES = "{ CALL selectUserRoles }";
}
