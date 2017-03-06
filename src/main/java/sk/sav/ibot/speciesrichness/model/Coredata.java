package sk.sav.ibot.speciesrichness.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import sk.sav.ibot.speciesrichness.geo.LatLon;

/**
 * Model for table containing all occurences data. Row contains taxonkey
 * (species), coordinates, year of the record, number of records at this
 * location
 *
 * @author Matus Kempa, Institute of Botany, SAS, Bratislava, Slovakia
 */
@Entity
@Table(name = "coredata_sorted")
//@Table(name = "coredata_t")
public class Coredata implements Serializable {

    @Id
    private Integer id;
    private Integer taxonkey;
    private Float latitude;
    private Float longitude;
    private String countrycode;
    private Integer cyear;
    private Integer numRecords;

    public Coredata() {
    }

    public Coredata(Integer id, Integer taxonkey, Float latitude, Float longitude, String countrycode, Integer cyear, Integer numRecords, Taxonomy taxonomy) {
        this.id = id;
        this.taxonkey = taxonkey;
        this.latitude = latitude;
        this.longitude = longitude;
        this.countrycode = countrycode;
        this.cyear = cyear;
        this.numRecords = numRecords;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTaxonkey() {
        return taxonkey;
    }

    public void setTaxonkey(Integer taxonkey) {
        this.taxonkey = taxonkey;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }

    public Integer getCyear() {
        return cyear;
    }

    public void setCyear(Integer cyear) {
        this.cyear = cyear;
    }

    public Integer getNumRecords() {
        return numRecords;
    }

    public void setNumRecords(Integer numRecords) {
        this.numRecords = numRecords;
    }

    public LatLon getCoordinates() {
        return new LatLon(this.getLatitude(), this.getLongitude());
    }

    @Override
    public String toString() {
        return "Coredata{" + "id=" + id + ", taxonkey=" + taxonkey + ", latitude=" + latitude + ", longitude=" + longitude + ", countrycode=" + countrycode + ", cyear=" + cyear + ", numRecords=" + numRecords + '}';
    }

}
