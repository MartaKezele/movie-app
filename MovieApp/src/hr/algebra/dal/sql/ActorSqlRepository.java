/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dal.sql;

import hr.algebra.dal.EntityRepository;
import hr.algebra.model.Actor;
import hr.algebra.model.Person;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;

/**
 *
 * @author kezel
 */
public class ActorSqlRepository<T> implements EntityRepository<Person> {

    private static final String ID_ACTOR = "IDActor";
    private static final String FIRST_NAME = "FirstName";
    private static final String LAST_NAME = "LastName";

    private static final String CREATE_ACTOR = "{ CALL createActor (?, ?, ?) }";
    private static final String UPDATE_ACTOR = "{ CALL updateActor (?, ?, ?) }";
    private static final String DELETE_ACTOR = "{ CALL deleteActor (?) }";
    private static final String SELECT_ACTOR = "{ CALL selectActor (?) }";
    private static final String SELECT_ACTORS = "{ CALL selectActors }";

    @Override
    public int create(Person entity) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();

        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_ACTOR)) {

            stmt.setString(1, entity.getFirstName());
            stmt.setString(2, entity.getLastName());
            stmt.registerOutParameter(3, Types.INTEGER);

            stmt.executeUpdate();
            return stmt.getInt(3);
        }
    }

    @Override
    public void create(List<Person> entities) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_ACTOR)) {

            for (Person entity : entities) {
                stmt.setString(1, entity.getFirstName());
                stmt.setString(2, entity.getLastName());
                stmt.registerOutParameter(3, Types.INTEGER);

                stmt.executeUpdate();
            }
        }
    }

    @Override
    public void update(int id, Person entity) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(UPDATE_ACTOR)) {

            stmt.setString(1, entity.getFirstName());
            stmt.setString(2, entity.getLastName());
            stmt.setInt(3, id);

            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(DELETE_ACTOR)) {

            stmt.setInt(1, id);

            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<Person> select(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_ACTOR)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(new Actor(
                            rs.getInt(ID_ACTOR),
                            rs.getString(FIRST_NAME),
                            rs.getString(LAST_NAME)));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Person> selectAll() throws Exception {
        List<Person> actors = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_ACTORS);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                actors.add(new Actor(
                        rs.getInt(ID_ACTOR),
                        rs.getString(FIRST_NAME),
                        rs.getString(LAST_NAME)));
            }
        }
        return actors;
    }
}
