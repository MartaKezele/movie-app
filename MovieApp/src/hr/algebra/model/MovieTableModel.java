/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author kezel
 */
public class MovieTableModel extends AbstractTableModel {

    private static final String[] COLUMN_NAMES = {
        "Id",
        "Title",
        "Published date",
        "Description",
        "Original title",
        "Directors",
        "Actors",
        "Duration",
        "Genres",
        "Poster",
        "Link",
        "Guid",
        "Trailer",
        "Start date"};

    private List<Movie> movies;

    public MovieTableModel(List movies) {
        this.movies = movies;
    }

    public void setMovies(List movies) {
        this.movies = movies;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return movies.size();

    }

    @Override
    public int getColumnCount() {
        return Movie.class.getDeclaredFields().length - 3;

    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return movies.get(rowIndex).getId();
            case 1:
                return movies.get(rowIndex).getTitle();
            case 2:
                return movies.get(rowIndex).getPubDate().format(Movie.PUB_DATE_FORMATTER);
            case 3:
                return movies.get(rowIndex).getDescription();
            case 4:
                return movies.get(rowIndex).getOriginalTitle();
            case 5:
                return movies.get(rowIndex).getDirectors();
            case 6:
                return movies.get(rowIndex).getActors();
            case 7:
                return movies.get(rowIndex).getDuration().get();
            case 8:
                return movies.get(rowIndex).getGenres();
            case 9:
                return movies.get(rowIndex).getPoster();
            case 10:
                return movies.get(rowIndex).getLink();
            case 11:
                return movies.get(rowIndex).getGuid();
            case 12:
                return movies.get(rowIndex).getTrailer();
            case 13:
                return movies.get(rowIndex).getStartDate();
            default:
                throw new RuntimeException("No such column");
        }
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Integer.class;
            case 2:
                return LocalDateTime.class;
            case 7:
                return Integer.class;
            case 13:
                return Date.class;
        }
        return super.getColumnClass(columnIndex);
    }
}
