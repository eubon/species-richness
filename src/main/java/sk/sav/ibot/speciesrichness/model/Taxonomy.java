/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.ibot.speciesrichness.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Model for table containing taxonomy hierarchy, taken from GBIF. 
 * Contains only ranks Genus and higher.
 * Rows are identified by unique gbifkey, name, rank of the taxon, and parent in the hierarchy.
 * @author Matus
 */
@Entity
@Table(name = "taxonomy")
public class Taxonomy implements Serializable {
    
    @Id
    private Integer id;
    private Long gbifkey;
    private String name;
    private String rank;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_key", referencedColumnName = "gbifkey")
    private Taxonomy parent;

    public Taxonomy() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getGbifkey() {
        return gbifkey;
    }

    public void setGbifkey(Long gbifkey) {
        this.gbifkey = gbifkey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public Taxonomy getParent() {
        return parent;
    }

    public void setParent(Taxonomy parent) {
        this.parent = parent;
    }
    
    public boolean hasParent() {
        return this.parent != null;
    }

    public String getHigherHierarchy() {
        Taxonomy t = this;
        StringBuilder sb = new StringBuilder();
        while (t.hasParent()) {
            t = t.getParent();
            sb.append(t.getName());
            if (t.hasParent()) {
                sb.append(" < ");
            }
        }
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return this.name + " (" + this.rank.toLowerCase() + ")";
    }
    
}
