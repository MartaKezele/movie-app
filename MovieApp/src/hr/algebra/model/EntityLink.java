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
public abstract class EntityLink {

    private int id;
    private int entity1ID;
    private int entity2ID;    
    
    public int getId() {
        return id;
    }

    public int getEntity1ID() {
        return entity1ID;
    }

    public int getEntity2ID() {
        return entity2ID;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEntity1ID(int entity1ID) {
        this.entity1ID = entity1ID;
    }

    public void setEntity2ID(int entity2ID) {
        this.entity2ID = entity2ID;
    }
}
