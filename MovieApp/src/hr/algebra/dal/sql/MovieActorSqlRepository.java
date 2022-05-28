/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dal.sql;

import hr.algebra.dal.EntityLinkRepository;
import hr.algebra.model.EntityLink;
import hr.algebra.model.MovieActor;
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
public class MovieActorSqlRepository implements EntityLinkRepository<EntityLink> {

    private static final String ID_MOVIE_ACTOR = "IDMovieActor";
    private static final String MOVIE_ID = "MovieID";
    private static final String ACTOR_ID = "ActorID";

    private static final String CREATE_MOVIE_ACTOR = "{ CALL createMovieActor (?, ?, ?) }";
    private static final String UPDATE_MOVIE_ACTOR = "{ CALL updateMovieActor (?, ?, ?) }";
    private static final String DELETE_MOVIE_ACTOR_BY_MOVIE_ID = "{ CALL deleteMovieActorsByMovieID (?) }";
    private static final String SELECT_MOVIE_ACTOR = "{ CALL selectMovieActor (?) }";
    private static final String SELECT_ACTORS_FOR_MOVIE = "{ CALL selectActorsForMovie (?) }";
    private static final String SELECT_MOVIES_FOR_ACTOR = "{ CALL selectMoviesForActor (?) }";
    private static final String SELECT_MOVIE_ACTORS = "{ CALL selectMovieActors }";

    @Override
    public int create(EntityLink entityLink) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();

        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_MOVIE_ACTOR)) {

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
                CallableStatement stmt = con.prepareCall(CREATE_MOVIE_ACTOR)) {

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
                CallableStatement stmt = con.prepareCall(UPDATE_MOVIE_ACTOR)) {

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
                CallableStatement stmt = con.prepareCall(DELETE_MOVIE_ACTOR_BY_MOVIE_ID)) {

            stmt.setInt(1, entity1ID);

            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<EntityLink> select(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_MOVIE_ACTOR)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(new MovieActor(
                            rs.getInt(ID_MOVIE_ACTOR),
                            rs.getInt(MOVIE_ID),
                            rs.getInt(ACTOR_ID)));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Integer> selectByEntity1(int entity1ID) throws Exception {
        List<Integer> actors = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_ACTORS_FOR_MOVIE)) {

            stmt.setInt(1, entity1ID);
            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    actors.add(rs.getInt(ACTOR_ID));
                }
            }
        }
        return actors;    }

    @Override
    public List<Integer> selectByEntity2(int entity2ID) throws Exception {
        List<Integer> movies = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_MOVIES_FOR_ACTOR)) {

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
        List<EntityLink> movieActors = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_MOVIE_ACTORS);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                movieActors.add(new MovieActor(
                        rs.getInt(ID_MOVIE_ACTOR),
                        rs.getInt(MOVIE_ID),
                        rs.getInt(ACTOR_ID)));
            }
        }
        return movieActors;
    }
}
