/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dal.sql;

import hr.algebra.dal.EntityLinkRepository;
import hr.algebra.model.EntityLink;
import hr.algebra.model.MovieDirector;
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
public class MovieDirectorSqlRepository implements EntityLinkRepository<EntityLink> {

    private static final String ID_MOVIE_DIRECTOR = "IDMovieDirector";
    private static final String MOVIE_ID = "MovieID";
    private static final String DIRECTOR_ID = "DirectorID";

    private static final String CREATE_MOVIE_DIRECTOR = "{ CALL createMovieDirector (?, ?, ?) }";
    private static final String UPDATE_MOVIE_DIRECTOR = "{ CALL updateMovieDirector (?, ?, ?) }";
    private static final String DELETE_MOVIE_DIRECTOR_BY_MOVIE_ID = "{ CALL deleteMovieDirectorsByMovieID (?) }";
    private static final String SELECT_MOVIE_DIRECTOR = "{ CALL selectMovieDirector (?) }";
    private static final String SELECT_DIRECTORS_FOR_MOVIE = "{ CALL selectDirectorsForMovie (?) }";
    private static final String SELECT_MOVIES_FOR_DIRECTOR = "{ CALL selectMoviesForDirector (?) }";
    private static final String SELECT_MOVIE_DIRECTORS = "{ CALL selectMovieDirectors }";

    @Override
    public int create(EntityLink entityLink) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();

        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_MOVIE_DIRECTOR)) {

            stmt.setInt(1, entityLink.getEntity1ID());
            stmt.setInt(2, entityLink.getEntity2ID());
            stmt.registerOutParameter(3, Types.INTEGER);

            stmt.executeUpdate();
            return stmt.getInt(3);
        }
    }

    @Override
    public void create(List<EntityLink> entitieLinks) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_MOVIE_DIRECTOR)) {

            for (EntityLink entityLink : entitieLinks) {
                stmt.setInt(1, entityLink.getEntity1ID());
                stmt.setInt(2, entityLink.getEntity2ID());
                stmt.registerOutParameter(3, Types.INTEGER);

                stmt.executeUpdate();
            }
        }
    }

    @Override
    public void update(int id, EntityLink entityLink) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(UPDATE_MOVIE_DIRECTOR)) {

            stmt.setInt(1, entityLink.getEntity1ID());
            stmt.setInt(2, entityLink.getEntity2ID());
            stmt.setInt(3, id);

            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteByEntity1ID(int entity1ID) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(DELETE_MOVIE_DIRECTOR_BY_MOVIE_ID)) {

            stmt.setInt(1, entity1ID);

            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<EntityLink> select(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_MOVIE_DIRECTOR)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(new MovieDirector(
                            rs.getInt(ID_MOVIE_DIRECTOR),
                            rs.getInt(MOVIE_ID),
                            rs.getInt(DIRECTOR_ID)));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Integer> selectByEntity1(int entity1ID) throws Exception {
        List<Integer> directors = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_DIRECTORS_FOR_MOVIE)) {

            stmt.setInt(1, entity1ID);
            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    directors.add(rs.getInt(DIRECTOR_ID));
                }
            }
        }
        return directors;
    }

    @Override
    public List<Integer> selectByEntity2(int entity2ID) throws Exception {
        List<Integer> movies = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_MOVIES_FOR_DIRECTOR)) {

            stmt.setInt(1, entity2ID);
            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    movies.add(rs.getInt(MOVIE_ID));
                }
            }
        }
        return movies;
    }

    @Override
    public List<EntityLink> selectAll() throws Exception {
        List<EntityLink> movieDirectors = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_MOVIE_DIRECTORS);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                movieDirectors.add(new MovieDirector(
                        rs.getInt(ID_MOVIE_DIRECTOR),
                        rs.getInt(MOVIE_ID),
                        rs.getInt(DIRECTOR_ID)));
            }
        }
        return movieDirectors;
    }
}
