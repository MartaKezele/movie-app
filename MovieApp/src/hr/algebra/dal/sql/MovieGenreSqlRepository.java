/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dal.sql;

import hr.algebra.dal.EntityLinkRepository;
import hr.algebra.model.EntityLink;
import hr.algebra.model.MovieGenre;
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
public class MovieGenreSqlRepository implements EntityLinkRepository<EntityLink> {

    private static final String ID_MOVIE_GENRE = "IDMovieGenre";
    private static final String MOVIE_ID = "MovieID";
    private static final String GENRE_ID = "GenreID";

    private static final String CREATE_MOVIE_GENRE = "{ CALL createMovieGenre (?, ?, ?) }";
    private static final String UPDATE_MOVIE_GENRE = "{ CALL updateMovieGenre (?, ?, ?) }";
    private static final String DELETE_MOVIE_GENRE_BY_MOVIE_ID = "{ CALL deleteMovieGenreByMovieID (?) }";
    private static final String SELECT_MOVIE_GENRE = "{ CALL selectMovieGenre (?) }";
    private static final String SELECT_GENRES_FOR_MOVIE = "{ CALL selectGenresForMovie (?) }";
    private static final String SELECT_MOVIES_FOR_GENRE = "{ CALL selectMoviesForGenre (?) }";
    private static final String SELECT_MOVIE_GENRES = "{ CALL selectMovieGenres }";

    @Override
    public int create(EntityLink entityLink) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();

        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_MOVIE_GENRE)) {

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
                CallableStatement stmt = con.prepareCall(CREATE_MOVIE_GENRE)) {

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
                CallableStatement stmt = con.prepareCall(UPDATE_MOVIE_GENRE)) {

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
                CallableStatement stmt = con.prepareCall(DELETE_MOVIE_GENRE_BY_MOVIE_ID)) {

            stmt.setInt(1, entity1ID);

            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<EntityLink> select(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_MOVIE_GENRE)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(new MovieGenre(
                            rs.getInt(ID_MOVIE_GENRE),
                            rs.getInt(MOVIE_ID),
                            rs.getInt(GENRE_ID)));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Integer> selectByEntity1(int entity1ID) throws Exception {
        List<Integer> genres = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_GENRES_FOR_MOVIE)) {

            stmt.setInt(1, entity1ID);
            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    genres.add(rs.getInt(GENRE_ID));
                }
            }
        }
        return genres;
    }

    @Override
    public List<Integer> selectByEntity2(int entity2ID) throws Exception {
        List<Integer> movies = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_MOVIES_FOR_GENRE)) {

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
        List<EntityLink> movieGenres = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_MOVIE_GENRES);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                movieGenres.add(new MovieGenre(
                        rs.getInt(ID_MOVIE_GENRE),
                        rs.getInt(MOVIE_ID),
                        rs.getInt(GENRE_ID)));
            }
        }
        return movieGenres;
    }

}
