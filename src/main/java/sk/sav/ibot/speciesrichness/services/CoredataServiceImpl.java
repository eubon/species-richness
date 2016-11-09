package sk.sav.ibot.speciesrichness.services;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import sk.sav.ibot.speciesrichness.dao.CoredataDAO;
import sk.sav.ibot.speciesrichness.model.Coredata;

/**
 * Service layer calling DAO layer for Coredata.
 * @author Matus Kempa, Institute of Botany, SAS, Bratislava, Slovakia
 */
@Service
@Scope(value = "singleton")
public class CoredataServiceImpl implements CoredataService {

    private CoredataDAO coredataDAO;

    public void setCoredataDAO(CoredataDAO coredataDAO) {
        this.coredataDAO = coredataDAO;
    }

    /**
     * Fetches coredata by specified taxonkeys within the specified range of years.
     * The results are fetched for every 500 taxonkeys in order to avoid long queries in DAO.
     * @param taxonkeys list of species' taxonkeys whose occurences are fetched
     * @param sinceYear lower value of the years range. This value is included.
     * @param untilYear Upper value of the years range. This value is included.
     * @return list of occurence data meeting the conditions
     */
    @Override
    @Transactional
    public List<Coredata> getCoredataByTaxonkeys(List<Integer> taxonkeys, int sinceYear, int untilYear) {
        //taking care of too many taxonkeys in one query causing StackOverflow error
        //chopping keys to sublists of 500 elements
        if (taxonkeys.size() > 500) {
            List<Coredata> coredata = new ArrayList<>();
            int size = taxonkeys.size();
            for (int i = 0; i < size; i += 500) {
                List<Integer> subkeys = taxonkeys.subList(i, Math.min(i + 500, size));
                coredata.addAll(this.coredataDAO.getCoredataByTaxonkeys(subkeys, sinceYear, untilYear));
            }
            return coredata;
        }
        return this.coredataDAO.getCoredataByTaxonkeys(taxonkeys, sinceYear, untilYear);
    }

    @Override
    @Transactional
    public List<Integer> getAllTaxonkeys(int sinceYear, int untilYear) {
        return this.coredataDAO.getAllTaxonkeys(sinceYear, untilYear);
    }
    
}
