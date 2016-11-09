package sk.sav.ibot.speciesrichness.dao;

import java.util.List;
import sk.sav.ibot.speciesrichness.model.Taxonomy;

/**
 *
 * @author Matus Kempa, Institute of Botany, SAS, Bratislava, Slovakia
 */
public interface TaxonomyDAO {

    public Taxonomy getTaxonByGbifkey(long gbifkey);
    public List<Taxonomy> getSuperTaxonStartsWith(String term);
}
