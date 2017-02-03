package sk.sav.ibot.speciesrichness.dao;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import sk.sav.ibot.speciesrichness.model.Coredata;


/**
 * Implementation of CoredataDAO interface. Handles data fetching from Coredata table.
 * @author Matus Kempa, Institute of Botany, SAS, Bratislava, Slovakia
 */
@Repository("coredataDAO")
public class CoredataDAOImpl implements CoredataDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    /**
     * Gets rows from Coredata table that have specified taxonkeys AND are within the specified range of years.
     * Both years are included in the range.
     * @param taxonkeys List of taxonkeys the rows are chosen by
     * @param sinceYear Lower value of the years range. This value is included.
     * @param untilYear Upper value of the years range. This value is included
     * @return List of Coredata rows meeting conditions
     */
    @Override
    public List<Coredata> getCoredataByTaxonkeys(List<Integer> taxonkeys, int sinceYear, int untilYear) {
        if (sinceYear <= 0) {
            throw new IllegalArgumentException("sinceYear must be a positive number");
        }
        if (untilYear <= 0) {
            throw new IllegalArgumentException("untilYear must be a positive number");
        }
        if (sinceYear > untilYear) {
            throw new IllegalArgumentException("sinceYear must be less or equal to untilYear");
        }
        if (taxonkeys == null) {
            throw new IllegalArgumentException("taxonkeys is null");
        }
        Session session = this.sessionFactory.getCurrentSession();
        String q = "from Coredata where taxonkey in :taxonkeys and cyear >= :syear and cyear <= :uyear order by id";
        Query query = session.createQuery(q);
        query.setParameterList("taxonkeys", taxonkeys).setParameter("syear", sinceYear).setParameter("uyear", untilYear);
        List<Coredata> coredata = query.list();
        return coredata;
    }
    
    /**
     * For testing
     * @param sinceYear
     * @param untilYear
     * @return 
     */
    @Override
    public List<Integer> getAllTaxonkeys(int sinceYear, int untilYear) {
        if (sinceYear <= 0) {
            throw new IllegalArgumentException("sinceYear must be a positive number");
        }
        if (untilYear <= 0) {
            throw new IllegalArgumentException("untilYear must be a positive number");
        }
        if (sinceYear > untilYear) {
            throw new IllegalArgumentException("sinceYear must be less or equal to untilYear");
        }
        Session session = this.sessionFactory.getCurrentSession();
        String q = "select distinct taxonkey from Coredata where cyear >= :syear and cyear <= :uyear";
        Query query = session.createQuery(q);
        query.setParameter("syear", sinceYear).setParameter("uyear", untilYear);
        List<Integer> taxonkeys = query.list();
        return taxonkeys;
    }
    
    /*
    public List<Coredata> getCoredataByTaxonkeys(List<Integer> taxonkeys) {
        List<Coredata> coredata;
        sessionFactory.beginTransaction();
        String q = "from Coredata where taxonkey in (:keys)";
        Query query = sessionFactory.createQuery(q);
        query.setParameterList("keys", taxonkeys);
        coredata = query.list();
        sessionFactory.getTransaction().commit();
        return coredata;
    }
    
    public List<Coredata> getCoredataByTaxonkeys(List<Integer> taxonkeys, int sinceYear, int untilYear) {
        long start = System.currentTimeMillis();
        if (sinceYear < 1758) {
            throw new IllegalArgumentException("sinceYear is less than 1758");
        }
        if (untilYear < sinceYear) {
            throw new IllegalArgumentException("untilYear is less than sinceYear");
        }
        List<Coredata> coredata;
        sessionFactory.beginTransaction();
        String q = "from Coredata where taxonkey in (:taxonkeys) and (cyear between (:syear) and (:uyear))";
        Query query = sessionFactory.createQuery(q);
        query.setParameterList("taxonkeys", taxonkeys).setParameter("syear", sinceYear).setParameter("uyear", untilYear);
        coredata = query.list();
        sessionFactory.getTransaction().commit();
        long end = System.currentTimeMillis();
        long time = end - start;
        System.out.println("HibernateQuery.getCoredataByTaxonkeys: " + time);
        return coredata;
    }
    
    public List<Coredata> getCoredataByTaxonkey(Integer taxonkey, int sinceYear, int untilYear) {
        long start = System.currentTimeMillis();
        if (sinceYear < 1758) {
            throw new IllegalArgumentException("sinceYear is less than 1758");
        }
        if (untilYear < sinceYear) {
            throw new IllegalArgumentException("untilYear is less than sinceYear");
        }
        List<Coredata> coredata;
        sessionFactory.beginTransaction();
        String q = "from Coredata where taxonkey = (:taxonkey) and (cyear between (:syear) and (:uyear))";
        Query query = sessionFactory.createQuery(q);
        query.setParameter("taxonkey", taxonkey).setParameter("syear", sinceYear).setParameter("uyear", untilYear);
        coredata = query.list();
        sessionFactory.getTransaction().commit();
        long end = System.currentTimeMillis();
        long time = end - start;
        System.out.println("HibernateQuery.getCoredataByTaxonkey: " + time);
        return coredata;
    }
    */
    
}
