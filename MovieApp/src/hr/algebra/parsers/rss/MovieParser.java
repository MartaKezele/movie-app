/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.parsers.rss;

import hr.algebra.factory.ParserFactory;
import hr.algebra.factory.UrlConnectionFactory;
import hr.algebra.model.Actor;
import hr.algebra.model.Director;
import hr.algebra.model.Genre;
import hr.algebra.model.Movie;
import hr.algebra.utils.FileUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 *
 * @author kezel
 */
public class MovieParser {

    private static final String RSS_URL = "https://www.blitz-cinestar.hr/rss.aspx?najava=1";
    private static final int TIMEOUT = 1000000;
    private static final String REQUEST_METHOD = "GET";
    private static final String EXT = ".jpg";
    private static final String DIR = "assets";

    private MovieParser() {
    }

    private enum TagType {

        ITEM("item"),
        TITLE("title"),
        PUB_DATE("pubDate"),
        DESCRIPTION("description"),
        ORIGINAL_TITLE("orignaziv"),
        DIRECTORS("redatelj"),
        ACTORS("glumci"),
        DURATION("trajanje"),
        GENRE("zanr"),
        POSTER("plakat"),
        LINK("link"),
        GUID("guid"),
        TRAILER("trailer"),
        START_DATE("pocetak");

        private final String name;

        private TagType(String name) {
            this.name = name;
        }

        private static Optional<TagType> from(String name) {
            for (TagType value : values()) {
                if (value.name.equals(name)) {
                    return Optional.of(value);
                }
            }
            return Optional.empty();
        }
    }

    public static List<Movie> parse() throws IOException, XMLStreamException {
        List<Movie> movies = new ArrayList<>();
        HttpURLConnection con = UrlConnectionFactory.getHttpUrlConnection(RSS_URL, TIMEOUT, REQUEST_METHOD);
        try (InputStream is = con.getInputStream()) {
            XMLEventReader reader = ParserFactory.createStaxParser(is);

            Optional<TagType> tagType = Optional.empty();
            Movie movie = null;
            StartElement startElement = null;
            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();
                switch (event.getEventType()) {
                    case XMLStreamConstants.START_ELEMENT:
                        startElement = event.asStartElement();
                        String qName = startElement.getName().getLocalPart();
                        tagType = TagType.from(qName);
                        break;
                    case XMLStreamConstants.CHARACTERS:
                        if (tagType.isPresent()) {
                            Characters characters = event.asCharacters();
                            String data = characters.getData().trim();
                            switch (tagType.get()) {
                                case ITEM:
                                    movie = new Movie();
                                    movies.add(movie);
                                    break;
                                case TITLE:
                                    if (movie != null && !data.isEmpty()) {
                                        movie.setTitle(data.trim());
                                    }
                                    break;
                                case PUB_DATE:
                                    if (movie != null && !data.isEmpty()) {
                                        try {
                                            LocalDateTime pubDate = LocalDateTime.parse(data.trim(), DateTimeFormatter.RFC_1123_DATE_TIME);
                                            movie.setPubDate(pubDate);
                                        } catch (Exception ex) {
                                            Logger.getLogger(MovieParser.class.getName()).log(Level.SEVERE, null, ex);
                                            movie.setPubDate(LocalDateTime.now());
                                        }
                                    }
                                    break;
                                case DESCRIPTION:
                                    if (movie != null && !data.isEmpty()) {
                                        movie.setDescription(getDescription(data.trim()));
                                    }
                                    break;
                                case ORIGINAL_TITLE:
                                    if (movie != null && !data.isEmpty()) {
                                        movie.setOriginalTitle(data.trim());
                                    }
                                    break;
                                case DIRECTORS:
                                    if (movie != null && !data.isEmpty()) {
                                        movie.setDirectors(Director.parse(data.trim()));
                                    }
                                    break;
                                case ACTORS:
                                    if (movie != null && !data.isEmpty()) {
                                        movie.setActors(Actor.parse(data.trim()));
                                    }
                                    break;
                                case DURATION:
                                    if (movie != null && !data.isEmpty()) {
                                        try {
                                            movie.setDuration(Optional.of(Integer.parseInt(data.trim())));
                                        } catch (NumberFormatException ex) {
                                            movie.setDuration(Optional.empty());
                                        }
                                    }
                                    break;
                                case GENRE:
                                    if (movie != null && !data.isEmpty()) {
                                        movie.setGenres(Genre.parse(data.trim()));
                                    }
                                    break;
                                case POSTER:
                                    if (movie != null && startElement != null && movie.getPoster() == null && !data.isEmpty()) {
                                        handlePicture(movie, data.trim());
                                    }
                                    break;
                                case LINK:
                                    if (movie != null && !data.isEmpty()) {
                                        movie.setLink(data.trim());
                                    }
                                    break;
                                case GUID:
                                    if (movie != null && !data.isEmpty()) {
                                        movie.setGuid(data.trim());
                                    }
                                    break;
                                case TRAILER:
                                    if (movie != null && !data.isEmpty()) {
                                        movie.setTrailer(data.trim());
                                    }
                                    break;
                                case START_DATE:
                                    if (movie != null && !data.isEmpty()) {
                                        try {
                                            movie.setStartDate((Date) Movie.START_DATE_FORMATTER.parse(data.trim()));
                                        } catch (ParseException ex) {
                                            Logger.getLogger(MovieParser.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                    break;
                            }
                        }
                        break;
                }
            }
        }
        return movies;

    }

    private static void handlePicture(Movie movie, String pictureUrl) {

        try {
            String ext = pictureUrl.substring(pictureUrl.lastIndexOf("."));
            if (ext.length() > 4) {
                ext = EXT;
            }
            String pictureName = UUID.randomUUID() + ext;
            String localPicturePath = DIR + File.separator + pictureName;

            FileUtils.copyFromUrl(pictureUrl, localPicturePath);
            movie.setPoster(localPicturePath);
        } catch (IOException ex) {
            Logger.getLogger(MovieParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static String getDescription(String description) {

        StringBuilder sb = new StringBuilder();
        int position = 0;
        int beginIdx = 0;
        int endIdx = 0;

        while (position < description.length()) {

            endIdx = description.indexOf('>', position);
            beginIdx = description.indexOf('<', endIdx + 1);

            if (beginIdx != -1) {
                sb.append(description.substring(endIdx + 1, beginIdx));
                position = beginIdx;
            } else {
                endIdx = description.lastIndexOf('>');
                sb.append(description.substring(endIdx + 1));
                position = description.length();
            }
        }

        return sb.toString();
    }
}
