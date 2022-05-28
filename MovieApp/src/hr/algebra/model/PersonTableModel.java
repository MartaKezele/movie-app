/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author kezel
 */
public class PersonTableModel extends AbstractTableModel {
    
    private static final String[] COLUMN_NAMES = {
        "Id",
        "First name",
        "Last name"};

    private List<Person> persons;

    public PersonTableModel(List persons) {
        this.persons = persons;
    }

    public void setPersons(List persons) {
        this.persons = persons;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return persons.size();
    }

    @Override
    public int getColumnCount() {
        return Person.class.getDeclaredFields().length - 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return persons.get(rowIndex).getId();
            case 1:
                return persons.get(rowIndex).getFirstName();
            case 2:
                return persons.get(rowIndex).getLastName();
            default:
                throw new RuntimeException("No such column");
        }
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Integer.class;
        }
        return super.getColumnClass(columnIndex);
    }
}
