package sk.sav.ibot.speciesrichness.dao;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import sk.sav.ibot.speciesrichness.model.Taxonomy;

/**
 * Implementation of the TaxonomyDAO interface. Handles data fetching from Taxonomy table.
 * @author Matus Kempa, Institute of Botany, SAS, Bratislava, Slovakia
 */
@Repository("taxonomyDAO")
public class TaxonomyDAOImpl implements TaxonomyDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sf){
        this.sessionFactory = sf;
    }

    /**
     *
     * @param supertaxon lowercase chain of properties until desired supertaxon
     * @param id
     * @return
     */
    /*
    @Override
    public List<Taxonomy> getSpeciesBySupertaxon(String supertaxon, int id) {
        Session session = this.sessionFactory.getCurrentSession();
        String q = "from Taxonomy as t where t." + supertaxon + ".id = (:id) order by t.scientificname";
        Query query = session.createQuery(q);
        query.setParameter("id", id);
        List<Taxonomy> species = query.list();
        return species;
    }*/
    
    /**
     * Gets a single row from Taxonomy table that has specified gbifkey.
     * @param gbifkey A key to get the row by.
     * @return A unique Taxonomy result.
     */
    @Override
    public Taxonomy getTaxonByGbifkey(long gbifkey) {
        if (gbifkey <= 0) {
            throw new IllegalArgumentException("gbifkey must be a positive number");
        }
        Session session = this.sessionFactory.getCurrentSession();
        Query q = session.createQuery("from Taxonomy where gbifkey = (:gbifkey)");
        q.setParameter("gbifkey", gbifkey);
        return (Taxonomy) q.uniqueResult();
    }
    
    /**
     * Gets all rows which name starts with the term.
     * @param term Value to select the rows by.
     * @return List of Taxonomy results
     */
    @Override
    public List<Taxonomy> getSuperTaxonStartsWith(String term) {
        if (term == null) {
            throw new IllegalArgumentException("term is null");
        }
        Session session = this.sessionFactory.getCurrentSession();
        String q = "from Taxonomy where lower(name) like (:name) order by name";
        Query query = session.createQuery(q);
        query.setParameter("name", term.toLowerCase() + '%');
        List<Taxonomy> taxa = query.list();
        return taxa;
    }
    
}
