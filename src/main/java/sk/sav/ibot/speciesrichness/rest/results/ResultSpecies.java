package sk.sav.ibot.speciesrichness.rest.results;

import io.swagger.annotations.ApiModel;
import java.util.Objects;

/**
 * POJO class representing species in the result JSON or xml. Each species is
 * represented as a pair of gbif key and scientific name.
 *
 * @author Matus Kempa, Institute of Botany, SAS, Bratislava, Slovakia
 */
@ApiModel(value = "Species")
public class ResultSpecies implements Comparable<ResultSpecies> {

    private Integer key;
    private String name;

    public ResultSpecies() {
    }

    public ResultSpecies(Integer key, String name) {
        this.key = key;
        this.name = name;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ResultSpecies{" + "key=" + key + ", name=" + name + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.key);
        hash = 17 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ResultSpecies other = (ResultSpecies) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return Objects.equals(this.key, other.key);
    }

    /**
     * Object are lexicographically compared by name, then by key
     *
     * @param o object to compare with
     * @return Negative value if this object is less than argument object, zero
     * value if this object is equal to argument object, positive value if this
     * object is greater than argument object
     */
    @Override
    public int compareTo(ResultSpecies o) {
        int nameCmp = this.name.compareTo(o.name);
        if (nameCmp != 0) {
            return nameCmp;
        }
        return this.key - o.key;
    }

}
