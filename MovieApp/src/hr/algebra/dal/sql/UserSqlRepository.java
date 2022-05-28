/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dal.sql;

import hr.algebra.dal.EntityRepository;
import hr.algebra.dal.EnumRepository;
import hr.algebra.dal.RepositoryFactory;
import hr.algebra.model.User;
import hr.algebra.model.UserRole;
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
public class UserSqlRepository implements EntityRepository<User> {

    private static final String ID_USER = "IDAppUser";
    private static final String USERNAME = "Username";
    private static final String PASSWORD = "Password";
    private static final String USER_ROLE = "UserRoleID";

    private static final String CREATE_USER = "{ CALL createAppUser (?, ?, ?, ?) }";
    private static final String UPDATE_USER = "{ CALL updateAppUser (?, ?, ?, ?) }";
    private static final String DELETE_USER = "{ CALL selectAppUser (?) }";
    private static final String SELECT_USER = "{ CALL selectAppUser (?) }";
    private static final String SELECT_USERS = "{ CALL selectAppUsers }";

    @Override
    public int create(User entity) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();

        try (Connection connection = dataSource.getConnection();
                CallableStatement stmt = connection.prepareCall(CREATE_USER)) {

            stmt.setString(1, entity.getUsername());
            stmt.setString(2, entity.getPassword());

            EnumRepository userRoleRepository = RepositoryFactory.getUserRoleRepository();
            List<Pair<Integer, String>> userRoles = userRoleRepository.selectAll();

            for (Pair<Integer, String> userRole : userRoles) {
                if (userRole.getValue().equals(entity.getUserRole().name())) {
                    stmt.setInt(3, userRole.getKey());
                }
            }

            stmt.registerOutParameter(4, Types.INTEGER);

            stmt.executeUpdate();
            return stmt.getInt(4);
        }
    }

    @Override
    public void create(List<User> entities) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_USER)) {

            for (User entity : entities) {
                stmt.setString(1, entity.getUsername());
                stmt.setString(2, entity.getPassword());

                EnumRepository userRoleRepository = RepositoryFactory.getUserRoleRepository();
                List<Pair<Integer, String>> userRoles = userRoleRepository.selectAll();

                for (Pair<Integer, String> userRole : userRoles) {
                    if (userRole.getValue().equals(entity.getUserRole().name())) {
                        stmt.setInt(3, userRole.getKey());
                    }
                }
                stmt.registerOutParameter(4, Types.INTEGER);

                stmt.executeUpdate();
            }
        }
    }

    @Override
    public void update(int id, User entity) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(UPDATE_USER)) {

            stmt.setString(1, entity.getUsername());
            stmt.setString(2, entity.getPassword());
            EnumRepository userRoleRepository = RepositoryFactory.getUserRoleRepository();
            List<Pair<Integer, String>> userRoles = userRoleRepository.selectAll();

            for (Pair<Integer, String> userRole : userRoles) {
                if (userRole.getValue().equals(entity.getUserRole().name())) {
                    stmt.setInt(3, userRole.getKey());
                }
            }
            stmt.setInt(4, id);

            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(DELETE_USER)) {

            stmt.setInt(1, id);

            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<User> select(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();

        try (Connection connection = dataSource.getConnection();
                CallableStatement stmt = connection.prepareCall(SELECT_USER);) {

            stmt.setInt(1, id);

            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {

                    EnumRepository userRoleRepository = RepositoryFactory.getUserRoleRepository();

                    return Optional.of(new User(
                            resultSet.getInt(ID_USER),
                            resultSet.getString(USERNAME),
                            resultSet.getString(PASSWORD),
                            UserRole.from(userRoleRepository.select(resultSet.getInt(USER_ROLE)).get().getValue())
                    ));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public List<User> selectAll() throws Exception {

        List<User> users = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();

        try (Connection connection = dataSource.getConnection();
                CallableStatement stmt = connection.prepareCall(SELECT_USERS);
                ResultSet resultSet = stmt.executeQuery()) {

            EnumRepository userRoleRepository = RepositoryFactory.getUserRoleRepository();

            while (resultSet.next()) {

                users.add(new User(
                        resultSet.getInt(ID_USER),
                        resultSet.getString(USERNAME),
                        resultSet.getString(PASSWORD),
                        UserRole.from(userRoleRepository.select(resultSet.getInt(USER_ROLE)).get().getValue())
                ));
            }
        }
        return users;
    }

}
