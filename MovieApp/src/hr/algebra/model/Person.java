/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import java.util.Comparator;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author kezel
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"firstName", "lastName"})
@XmlTransient
@XmlSeeAlso({ Director.class, Actor.class })
public abstract class Person implements Comparable<Person> {

    public static final Comparator<Person> NAME_COMPARATOR = (Person o1, Person o2) -> o1.toString().compareTo(o2.toString());

    @XmlAttribute
    private int id;
    private String firstName;
    private String lastName;
    
    @Override
    public int compareTo(Person o) {
        return Integer.valueOf(id).compareTo(o.id);
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
