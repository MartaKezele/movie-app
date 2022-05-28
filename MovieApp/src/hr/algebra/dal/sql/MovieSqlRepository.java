/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dal.sql;

import hr.algebra.UploadAndDeleteMoviesPanel;
import hr.algebra.dal.EntityLinkRepository;
import hr.algebra.dal.EntityRepository;
import hr.algebra.dal.EnumRepository;
import hr.algebra.dal.RepositoryFactory;
import hr.algebra.model.EntityLink;
import hr.algebra.model.Genre;
import hr.algebra.model.Movie;
import hr.algebra.model.MovieActor;
import hr.algebra.model.MovieDirector;
import hr.algebra.model.MovieGenre;
import hr.algebra.model.Person;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
import javax.sql.DataSource;

/**
 *
 * @author kezel
 */
public class MovieSqlRepository implements EntityRepository<Movie> {

    private static final String ID_MOVIE = "IDMovie";
    private static final String TITLE = "Title";
    private static final String PUB_DATE = "PubDate";
    private static final String DESCRIPTION = "Description";
    private static final String ORIGINAL_TITLE = "OriginalTitle";
    private static final String DURATION = "Duration";
    private static final String POSTER = "Poster";
    private static final String LINK = "Link";
    private static final String GUID = "Guid";
    private static final String TRAILER = "Trailer";
    private static final String START_DATE = "StartDate";

    private static final String CREATE_MOVIE = "{ CALL createMovie (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }";
    private static final String UPDATE_MOVIE = "{ CALL updateMovie (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }";
    private static final String DELETE_MOVIE = "{ CALL deleteMovie (?) }";
    private static final String SELECT_MOVIE = "{ CALL selectMovie (?) }";
    private static final String SELECT_MOVIES = "{ CALL selectMovies }";

    @Override
    public int create(Movie entity) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();

        int movieID;

        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_MOVIE)) {

            stmt.setString(1, entity.getTitle());
            stmt.setString(2, entity.getPubDate().format(Movie.PUB_DATE_FORMATTER));
            stmt.setString(3, entity.getDescription());
            stmt.setString(4, entity.getOriginalTitle());
            if (entity.getDuration().isPresent()) {
                stmt.setInt(5, entity.getDuration().get());
            } else {
                stmt.setNull(5, 4);
            }
            stmt.setString(6, entity.getPoster());
            stmt.setString(7, entity.getLink());
            stmt.setString(8, entity.getGuid());
            stmt.setString(9, entity.getTrailer());
            stmt.setString(10, Movie.START_DATE_FORMATTER.format(entity.getStartDate()));
            stmt.registerOutParameter(11, Types.INTEGER);

            stmt.executeUpdate();
            movieID = stmt.getInt(11);

            createDirectorsAndMovieDirectors(entity.getDirectors(), movieID);
            createActorsAndMovieActors(entity.getActors(), movieID);
            createGenresAndMovieGenres(entity.getGenres(), movieID);

            return movieID;
        }
    }

    @Override
    public void create(List<Movie> entities) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_MOVIE)) {

            entities.forEach((entity) -> {
                try {
                    stmt.setString(1, entity.getTitle());
                    stmt.setString(2, entity.getPubDate().format(Movie.PUB_DATE_FORMATTER));
                    stmt.setString(3, entity.getDescription());
                    stmt.setString(4, entity.getOriginalTitle());
                    if (entity.getDuration() != null && entity.getDuration().isPresent()) {
                        stmt.setInt(5, entity.getDuration().get());
                    } else {
                        stmt.setNull(5, 4);
                    }
                    stmt.setString(6, entity.getPoster());
                    stmt.setString(7, entity.getLink());
                    stmt.setString(8, entity.getGuid());
                    stmt.setString(9, entity.getTrailer());
                    stmt.setString(10, Movie.START_DATE_FORMATTER.format(entity.getStartDate()));
                    stmt.registerOutParameter(11, Types.INTEGER);

                    stmt.executeUpdate();
                    int movieID = stmt.getInt(11);

                    createDirectorsAndMovieDirectors(entity.getDirectors(), movieID);
                    createActorsAndMovieActors(entity.getActors(), movieID);
                    createGenresAndMovieGenres(entity.getGenres(), movieID);
                } catch (Exception ex) {
                    Logger.getLogger(MovieSqlRepository.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    }

    @Override
    public void update(int id, Movie entity) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(UPDATE_MOVIE)) {

            stmt.setString(1, entity.getTitle());
            stmt.setString(2, entity.getPubDate().format(Movie.PUB_DATE_FORMATTER));
            stmt.setString(3, entity.getDescription());
            stmt.setString(4, entity.getOriginalTitle());
            if (entity.getDuration().isPresent()) {
                stmt.setInt(5, entity.getDuration().get());
            } else {
                stmt.setNull(5, 4);
            }
            stmt.setString(6, entity.getPoster());
            stmt.setString(7, entity.getLink());
            stmt.setString(8, entity.getGuid());
            stmt.setString(9, entity.getTrailer());
            stmt.setString(10, Movie.START_DATE_FORMATTER.format(entity.getStartDate()));
            stmt.setInt(11, id);

            stmt.executeUpdate();

            // update directors
            EntityLinkRepository<EntityLink> movieDirectorRepository = RepositoryFactory.getMovieDirectorRepository();

            movieDirectorRepository.deleteByEntity1ID(id);
            createDirectorsAndMovieDirectors(entity.getDirectors(), id);

            // update actors
            EntityLinkRepository<EntityLink> movieActorRepository = RepositoryFactory.getMovieActorRepository();

            movieActorRepository.deleteByEntity1ID(id);
            createActorsAndMovieActors(entity.getActors(), id);

            // update genres
            EntityLinkRepository<EntityLink> movieGenreRepository = RepositoryFactory.getMovieGenreRepository();

            movieGenreRepository.deleteByEntity1ID(id);
            createGenresAndMovieGenres(entity.getGenres(), id);
        }
    }

    @Override
    public void delete(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(DELETE_MOVIE)) {

            stmt.setInt(1, id);

            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<Movie> select(int id) throws Exception {

        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_MOVIE)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    Movie movie = new Movie(
                            rs.getInt(ID_MOVIE),
                            rs.getString(TITLE),
                            LocalDateTime.parse(rs.getString(PUB_DATE), Movie.PUB_DATE_FORMATTER),
                            rs.getString(DESCRIPTION),
                            rs.getString(ORIGINAL_TITLE),
                            Optional.of(rs.getInt(DURATION)),
                            rs.getString(POSTER),
                            rs.getString(LINK),
                            rs.getString(GUID),
                            rs.getString(TRAILER),
                            (Date) Movie.START_DATE_FORMATTER.parse(rs.getString(START_DATE)));

                    movie.setDirectors(getPersons(movie.getId(), RepositoryFactory.getMovieDirectorRepository(), RepositoryFactory.getDirectorRepository()));
                    movie.setActors(getPersons(movie.getId(), RepositoryFactory.getMovieActorRepository(), RepositoryFactory.getActorRepository()));
                    movie.setGenres(getGenres(movie.getId()));

                    return Optional.of(movie);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Movie> selectAll() throws Exception {

        List<Movie> movies = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_MOVIES);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Movie movie = new Movie(
                        rs.getInt(ID_MOVIE),
                        rs.getString(TITLE),
                        LocalDateTime.parse(rs.getString(PUB_DATE), Movie.PUB_DATE_FORMATTER),
                        rs.getString(DESCRIPTION),
                        rs.getString(ORIGINAL_TITLE),
                        Optional.of(rs.getInt(DURATION)),
                        rs.getString(POSTER),
                        rs.getString(LINK),
                        rs.getString(GUID),
                        rs.getString(TRAILER),
                        (Date) Movie.START_DATE_FORMATTER.parse(rs.getString(START_DATE)));

                movie.setDirectors(getPersons(movie.getId(), RepositoryFactory.getMovieDirectorRepository(), RepositoryFactory.getDirectorRepository()));
                movie.setActors(getPersons(movie.getId(), RepositoryFactory.getMovieActorRepository(), RepositoryFactory.getActorRepository()));
                movie.setGenres(getGenres(movie.getId()));

                movies.add(movie);
            }
        }
        return movies;
    }

    private void createDirectorsAndMovieDirectors(Set<Person> directors, int movieID) throws Exception {

        if (directors == null || directors.isEmpty()) {
            return;
        }

        EntityRepository<Person> directorRepository = RepositoryFactory.getDirectorRepository();
        EntityLinkRepository<EntityLink> movieDirectorRepository = RepositoryFactory.getMovieDirectorRepository();

        List<Person> allDirectors = directorRepository.selectAll();

        for (Person director : directors) {
            if (allDirectors.isEmpty()) {
                int directorID = directorRepository.create(director);
                movieDirectorRepository.create(new MovieDirector(movieID, directorID));
            } else {
                boolean foundDirector = false;
                for (Person allDirector : allDirectors) {
                    if (director.getFirstName().equals(allDirector.getFirstName()) && director.getLastName().equals(allDirector.getLastName())) {
                        movieDirectorRepository.create(new MovieDirector(movieID, allDirector.getId()));
                        foundDirector = true;
                    }
                }
                if (!foundDirector) {
                    int directorID = directorRepository.create(director);
                    movieDirectorRepository.create(new MovieDirector(movieID, directorID));
                }
            }
        }
    }

    private void createActorsAndMovieActors(Set<Person> actors, int movieID) throws Exception {

        if (actors == null || actors.isEmpty()) {
            return;
        }

        EntityRepository<Person> actorRepository = RepositoryFactory.getActorRepository();
        EntityLinkRepository<EntityLink> movieActorRepository = RepositoryFactory.getMovieActorRepository();

        List<Person> allActors = actorRepository.selectAll();

        for (Person actor : actors) {
            if (allActors.isEmpty()) {
                int actorID = actorRepository.create(actor);
                movieActorRepository.create(new MovieActor(movieID, actorID));
            } else {
                boolean foundActor = false;
                for (Person allActor : allActors) {
                    if (actor.getFirstName().equals(allActor.getFirstName()) && actor.getLastName().equals(allActor.getLastName())) {
                        movieActorRepository.create(new MovieActor(movieID, allActor.getId()));
                        foundActor = true;
                    }
                }
                if (!foundActor) {
                    int actorID = actorRepository.create(actor);
                    movieActorRepository.create(new MovieActor(movieID, actorID));
                }
            }
        }
    }

    private void createGenresAndMovieGenres(Set<Genre> genres, int movieID) throws Exception {

        if (genres == null || genres.isEmpty()) {
            return;
        }

        EnumRepository genreRepository = RepositoryFactory.getGenreRepository();
        EntityLinkRepository<EntityLink> movieGenreRepository = RepositoryFactory.getMovieGenreRepository();

        List<Pair<Integer, String>> allGenres = genreRepository.selectAll();

        for (Genre genre : genres) {
            if (allGenres.isEmpty()) {
                int genreID = genreRepository.create(genre.name());
                movieGenreRepository.create(new MovieGenre(movieID, genreID));
            } else {
                boolean foundGenre = false;
                for (Pair<Integer, String> allGenre : allGenres) {
                    if (genre.name().equals(allGenre.getValue())) {
                        movieGenreRepository.create(new MovieGenre(movieID, allGenre.getKey()));
                        foundGenre = true;
                    }
                }
                if (!foundGenre) {
                    int genreID = genreRepository.create(genre.name());
                    movieGenreRepository.create(new MovieGenre(movieID, genreID));
                }
            }
        }
    }

    private Set<Person> getPersons(int movieID, EntityLinkRepository<EntityLink> moviePersonRepository, EntityRepository<Person> personRepository) throws Exception {

        List<Integer> personIDs = moviePersonRepository.selectByEntity1(movieID);
        Set<Person> persons = new TreeSet<>();

        for (Integer personID : personIDs) {
            Optional<Person> person = personRepository.select(personID);
            if (person.isPresent()) {
                persons.add(person.get());
            }
        }

        return persons;
    }

    private Set<Genre> getGenres(int movieID) throws Exception {

        EntityLinkRepository<EntityLink> movieGenreRepository = RepositoryFactory.getMovieGenreRepository();
        EnumRepository genreRepository = RepositoryFactory.getGenreRepository();

        List<Integer> genreIDs = movieGenreRepository.selectByEntity1(movieID);
        Set<Genre> genres = new TreeSet<>();

        for (Integer genreID : genreIDs) {
            Optional<Pair<Integer, String>> genre = genreRepository.select(genreID);
            if (genre.isPresent()) {
                genres.add(Genre.fromName(genre.get().getValue()));
            }
        }

        return genres;
    }
}
