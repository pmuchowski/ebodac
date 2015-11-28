package org.motechproject.bookingapp.domain;

import org.motechproject.mds.annotations.Cascade;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.NonEditable;

import javax.jdo.annotations.Persistent;
import java.util.List;

@Entity
public class Site {

    @Field
    private Long id;

    @Field
    @Cascade(delete = true)
    @Persistent(mappedBy = "site")
    private List<Clinic> clinics;

    @Field(required = true)
    private String siteId;

    @NonEditable(display = false)
    @Field
    private String owner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Clinic> getClinics() {
        return clinics;
    }

    public void setClinics(List<Clinic> clinics) {
        this.clinics = clinics;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return siteId;
    }
}