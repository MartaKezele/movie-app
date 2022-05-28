/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dal.sql;

import hr.algebra.dal.EnumRepository;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.util.Pair;
import javax.sql.DataSource;

/**
 *
 * @author kezel
 */
public class EnumSqlRepository implements EnumRepository {

    private final String ID;
    private final String NAME;

    private final String CREATE;
    private final String UPDATE;
    private final String DELETE;
    private final String SELECT;
    private final String SELECT_ALL;

    public EnumSqlRepository(String id, String name, String create, String update, String delete, String select, String select_all) {
        this.ID = id;
        this.NAME = name;
        this.CREATE = create;
        this.UPDATE = update;
        this.DELETE = delete;
        this.SELECT = select;
        this.SELECT_ALL = select_all;
    }

    @Override
    public int create(String entity) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();

        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE)) {

            stmt.setString(1, entity);
            stmt.registerOutParameter(2, Types.INTEGER);

            stmt.executeUpdate();
            return stmt.getInt(2);
        }
    }

    @Override
    public void create(List<String> entities) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE)) {

            for (String entity : entities) {
                stmt.setString(1, entity);
                stmt.registerOutParameter(2, Types.INTEGER);

                stmt.executeUpdate();
            }
        }
    }

    @Override
    public void update(int id, String entity) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(UPDATE)) {

            stmt.setString(1, entity);
            stmt.setInt(2, id);

            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(DELETE)) {

            stmt.setInt(1, id);

            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<Pair<Integer, String>> select(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(new Pair(rs.getInt(ID), rs.getString(NAME)));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Pair<Integer, String>> selectAll() throws Exception {
        List<Pair<Integer, String>> entities = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_ALL);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                entities.add(new Pair(rs.getInt(ID), rs.getString(NAME)));
            }
        }
        return entities;
    }
}
