/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import java.time.LocalDateTime;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author kezel
 */
public class PubDateAdapter extends XmlAdapter<String, LocalDateTime> {

    @Override
    public LocalDateTime unmarshal(String text) throws Exception {
        return LocalDateTime.parse(text, Movie.PUB_DATE_FORMATTER);
    }

    @Override
    public String marshal(LocalDateTime date) throws Exception {
        return date.format(Movie.PUB_DATE_FORMATTER);
    }
}
