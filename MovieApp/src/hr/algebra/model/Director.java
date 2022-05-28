/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author kezel
 */
public class Director extends Person {

    public Director() {
    }
    
    public Director(String firstName, String lastName) {
        super.setFirstName(firstName);
        super.setLastName(lastName);
    }

    public Director(int id, String firstName, String lastName) {
        this(firstName, lastName);
        super.setId(id);
    }

    public static Set<Person> parse(String data) {
        Set<Person> personSet = new TreeSet<>(Person.NAME_COMPARATOR);

        String[] persons = data.split(", ");
        for (String person : persons) {
            String[] names = person.trim().split(" ", 2);
            if (names.length == 2) {
                personSet.add(new Director(names[0].trim(), names[1].trim()));
            }
        }

        return personSet;
    }
}
