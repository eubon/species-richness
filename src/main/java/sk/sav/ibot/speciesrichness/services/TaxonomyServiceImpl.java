/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.ibot.speciesrichness.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.sav.ibot.speciesrichness.dao.TaxonomyDAO;
import sk.sav.ibot.speciesrichness.model.Taxonomy;

/**
 * Service layer calling DAO layer for Taxonomy
 * @author Matus Kempa, Institute of Botany, SAS, Bratislava, Slovakia
 */
@Service("taxonomyService")
@Scope(value = "singleton")
public class TaxonomyServiceImpl implements TaxonomyService{

    @Autowired
    private TaxonomyDAO taxonomyDAO;

    public void setTaxonomyDAO(TaxonomyDAO taxonomyDAO) {
        this.taxonomyDAO = taxonomyDAO;
    }
    
    /**
     * Gets single Taxonomy result by unique gbifkey.
     * @param gbifkey
     * @return Taxonomy object with given gbifkey
     */
    @Override
    @Transactional(readOnly = true)
    public Taxonomy getTaxonByGbifkey(long gbifkey) {
        return this.taxonomyDAO.getTaxonByGbifkey(gbifkey);
    }
    
    /**
     * Gets all taxa whose names start with query
     * @param query term to search taxa by
     * @return List of taxa 
     */
    @Override
    @Transactional(readOnly = true)
    public List<Taxonomy> getHigherTaxonStartsWith(String query) {
        return this.taxonomyDAO.getSuperTaxonStartsWith(query);
    }
    
}
