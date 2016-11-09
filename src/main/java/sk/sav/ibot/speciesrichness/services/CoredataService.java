/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.ibot.speciesrichness.services;

import java.io.Serializable;
import java.util.List;
import sk.sav.ibot.speciesrichness.model.Coredata;

/**
 *
 * @author Matus
 */
public interface CoredataService {
    
    public List<Coredata> getCoredataByTaxonkeys(List<Integer> taxonkeys, int sinceYear, int untilYear);
    public List<Integer> getAllTaxonkeys(int sinceYear, int untilYear);
    
}
