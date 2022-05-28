/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import java.util.Date;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author kezel
 */
public class StartDateAdapter extends XmlAdapter<String, Date> {

    @Override
    public Date unmarshal(String text) throws Exception {
        return (Date) Movie.START_DATE_FORMATTER.parse(text);
    }

    @Override
    public String marshal(Date date) throws Exception {
        return Movie.START_DATE_FORMATTER.format(date);
    }
}
