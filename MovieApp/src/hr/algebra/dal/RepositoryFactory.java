/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dal;

import hr.algebra.dal.sql.ActorSqlRepository;
import hr.algebra.dal.sql.DirectorSqlRepository;
import hr.algebra.dal.sql.EnumSqlRepository;
import hr.algebra.dal.sql.MovieActorSqlRepository;
import hr.algebra.dal.sql.MovieDirectorSqlRepository;
import hr.algebra.dal.sql.MovieGenreSqlRepository;
import hr.algebra.dal.sql.MovieSqlRepository;
import hr.algebra.dal.sql.UserSqlRepository;
import hr.algebra.model.EntityLink;
import hr.algebra.model.Genre;
import hr.algebra.model.Movie;
import hr.algebra.model.Person;
import hr.algebra.model.User;
import hr.algebra.model.UserRole;

/**
 *
 * @author kezel
 */
public class RepositoryFactory {

    private RepositoryFactory() {
    }

    public static EntityRepository<Movie> getMovieRepository() throws Exception {
        return new MovieSqlRepository();
    }

    public static EntityRepository<Person> getDirectorRepository() throws Exception {
        return new DirectorSqlRepository();
    }

    public static EntityRepository<Person> getActorRepository() throws Exception {
        return new ActorSqlRepository();
    }

    public static EntityRepository<User> getUserRepository() throws Exception {
        return new UserSqlRepository();
    }

    // genre 
    public static EnumRepository getGenreRepository() throws Exception {
        return new EnumSqlRepository(Genre.ID_GENRE, Genre.NAME, Genre.CREATE_GENRE, Genre.UPDATE_GENRE, Genre.DELETE_GENRE, Genre.SELECT_GENRE, Genre.SELECT_GENRES);
    }

    // userRole 
    public static EnumRepository getUserRoleRepository() throws Exception {
        return new EnumSqlRepository(UserRole.ID_USER_ROLE, UserRole.NAME, UserRole.CREATE_USER_ROLE, UserRole.UPDATE_USER_ROLE, UserRole.DELETE_USER_ROLE, UserRole.SELECT_USER_ROLE, UserRole.SELECT_USER_ROLES);
    }

    public static EntityLinkRepository<EntityLink> getMovieDirectorRepository() {
        return new MovieDirectorSqlRepository();
    }

    public static EntityLinkRepository<EntityLink> getMovieActorRepository() {
        return new MovieActorSqlRepository();
    }

    public static EntityLinkRepository<EntityLink> getMovieGenreRepository() {
        return new MovieGenreSqlRepository();
    }
}
