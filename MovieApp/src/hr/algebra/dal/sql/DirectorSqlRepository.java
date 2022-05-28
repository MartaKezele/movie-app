/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dal.sql;

import hr.algebra.dal.EntityRepository;
import hr.algebra.model.Director;
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
public class DirectorSqlRepository implements EntityRepository<Person> {

    private static final String ID_DIRECTOR = "IDDirector";
    private static final String FIRST_NAME = "FirstName";
    private static final String LAST_NAME = "LastName";

    private static final String CREATE_DIRECTOR = "{ CALL createDirector (?, ?, ?) }";
    private static final String UPDATE_DIRECTOR = "{ CALL updateDirector (?, ?, ?) }";
    private static final String DELETE_DIRECTOR = "{ CALL deleteDirector (?) }";
    private static final String SELECT_DIRECTOR = "{ CALL selectDirector (?) }";
    private static final String SELECT_DIRECTORS = "{ CALL selectDirectors }";

    @Override
    public int create(Person entity) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();

        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_DIRECTOR)) {

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
                CallableStatement stmt = con.prepareCall(CREATE_DIRECTOR)) {

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
                CallableStatement stmt = con.prepareCall(UPDATE_DIRECTOR)) {

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
                CallableStatement stmt = con.prepareCall(DELETE_DIRECTOR)) {

            stmt.setInt(1, id);

            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<Person> select(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_DIRECTOR)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(new Director(
                            rs.getInt(ID_DIRECTOR),
                            rs.getString(FIRST_NAME),
                            rs.getString(LAST_NAME)));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Person> selectAll() throws Exception {
        List<Person> directors = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_DIRECTORS);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                directors.add(new Director(
                        rs.getInt(ID_DIRECTOR),
                        rs.getString(FIRST_NAME),
                        rs.getString(LAST_NAME)));
            }
        }
        return directors;
    }
}
