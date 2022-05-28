/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dal;

import java.util.List;
import java.util.Optional;
import javafx.util.Pair;

/**
 *
 * @author kezel
 */
public interface EnumRepository {

    int create(String entity) throws Exception;

    void create(List<String> entities) throws Exception;

    void update(int id, String entity) throws Exception;

    void delete(int id) throws Exception;

    Optional<Pair<Integer, String>> select(int id) throws Exception;

    List<Pair<Integer, String>> selectAll() throws Exception;
}
