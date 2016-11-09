/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.ibot.speciesrichness.services;

import java.util.List;
import sk.sav.ibot.speciesrichness.model.Taxonomy;

/**
 *
 * @author Matus
 */
public interface TaxonomyService {
    
    /*
    public List<Kingdom> getKingdoms();
    public List<Phylum> getPhylae();
    public List<RClass> getRClasses();
    public List<ROrder> getROrders();
    public List<Family> getFamilies();
    public List<Genus> getGenera();
    public List<Taxonomy> getSpeciesBySupertaxon(String supertaxon, int id);
*/
    public Taxonomy getTaxonByGbifkey(long gbifkey);
    public List<Taxonomy> getHigherTaxonStartsWith(String query);
    
}
