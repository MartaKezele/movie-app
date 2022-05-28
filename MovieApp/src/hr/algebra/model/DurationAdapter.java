/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;
import java.util.Optional;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author kezel
 */
public class DurationAdapter extends XmlAdapter<Integer, Optional<Integer>> {

    @Override
    public Optional<Integer> unmarshal(Integer duration) throws Exception {
        return Optional.of(duration);
    }

    @Override
    public Integer marshal(Optional<Integer> duration) throws Exception {
        if (duration.isPresent()) {
            return duration.get();
        }
        else
        {
            return 0;
        }
    }
    
}
