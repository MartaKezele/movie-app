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
public class MovieGenre extends EntityLink {

    public MovieGenre(int entity1ID, int entity2ID) {
        super.setEntity1ID(entity1ID);
        super.setEntity2ID(entity2ID);
    }

    public MovieGenre(int id, int entity1ID, int entity2ID) {
        this(entity1ID, entity2ID);
        super.setId(id);
    }
}
