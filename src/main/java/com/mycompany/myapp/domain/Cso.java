package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Cso.
 */
@Entity
@Table(name = "cso")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Cso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Column(name = "organisation", nullable = false)
    private String organisation;

    @NotNull
    @Column(name = "cell", nullable = false)
    private String cell;

    @NotNull
    @Column(name = "city", nullable = false)
    private String city;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "registration_certificate")
    private Boolean registrationCertificate;

    @Column(name = "organisation_profile")
    private Boolean organisationProfile;

    @Column(name = "management_structure")
    private Boolean managementStructure;

    @Column(name = "strategic_plan")
    private Boolean strategicPlan;

    @Column(name = "resource_mobilisation_plan")
    private Boolean resourceMobilisationPlan;

    @Column(name = "comments")
    private String comments;

    @Column(name = "enquiries")
    private String enquiries;

    @NotNull
    @Column(name = "lat", nullable = false)
    private Float lat;

    @NotNull
    @Column(name = "lng", nullable = false)
    private Float lng;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "modified_date")
    private Instant modifiedDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_cso__province",
        joinColumns = @JoinColumn(name = "cso_id"),
        inverseJoinColumns = @JoinColumn(name = "province_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "csos" }, allowSetters = true)
    private Set<Province> provinces = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cso id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Cso firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Cso lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getOrganisation() {
        return this.organisation;
    }

    public Cso organisation(String organisation) {
        this.setOrganisation(organisation);
        return this;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public String getCell() {
        return this.cell;
    }

    public Cso cell(String cell) {
        this.setCell(cell);
        return this;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getCity() {
        return this.city;
    }

    public Cso city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return this.email;
    }

    public Cso email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getRegistrationCertificate() {
        return this.registrationCertificate;
    }

    public Cso registrationCertificate(Boolean registrationCertificate) {
        this.setRegistrationCertificate(registrationCertificate);
        return this;
    }

    public void setRegistrationCertificate(Boolean registrationCertificate) {
        this.registrationCertificate = registrationCertificate;
    }

    public Boolean getOrganisationProfile() {
        return this.organisationProfile;
    }

    public Cso organisationProfile(Boolean organisationProfile) {
        this.setOrganisationProfile(organisationProfile);
        return this;
    }

    public void setOrganisationProfile(Boolean organisationProfile) {
        this.organisationProfile = organisationProfile;
    }

    public Boolean getManagementStructure() {
        return this.managementStructure;
    }

    public Cso managementStructure(Boolean managementStructure) {
        this.setManagementStructure(managementStructure);
        return this;
    }

    public void setManagementStructure(Boolean managementStructure) {
        this.managementStructure = managementStructure;
    }

    public Boolean getStrategicPlan() {
        return this.strategicPlan;
    }

    public Cso strategicPlan(Boolean strategicPlan) {
        this.setStrategicPlan(strategicPlan);
        return this;
    }

    public void setStrategicPlan(Boolean strategicPlan) {
        this.strategicPlan = strategicPlan;
    }

    public Boolean getResourceMobilisationPlan() {
        return this.resourceMobilisationPlan;
    }

    public Cso resourceMobilisationPlan(Boolean resourceMobilisationPlan) {
        this.setResourceMobilisationPlan(resourceMobilisationPlan);
        return this;
    }

    public void setResourceMobilisationPlan(Boolean resourceMobilisationPlan) {
        this.resourceMobilisationPlan = resourceMobilisationPlan;
    }

    public String getComments() {
        return this.comments;
    }

    public Cso comments(String comments) {
        this.setComments(comments);
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getEnquiries() {
        return this.enquiries;
    }

    public Cso enquiries(String enquiries) {
        this.setEnquiries(enquiries);
        return this;
    }

    public void setEnquiries(String enquiries) {
        this.enquiries = enquiries;
    }

    public Float getLat() {
        return this.lat;
    }

    public Cso lat(Float lat) {
        this.setLat(lat);
        return this;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public Float getLng() {
        return this.lng;
    }

    public Cso lng(Float lng) {
        this.setLng(lng);
        return this;
    }

    public void setLng(Float lng) {
        this.lng = lng;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Cso createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getModifiedDate() {
        return this.modifiedDate;
    }

    public Cso modifiedDate(Instant modifiedDate) {
        this.setModifiedDate(modifiedDate);
        return this;
    }

    public void setModifiedDate(Instant modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Set<Province> getProvinces() {
        return this.provinces;
    }

    public void setProvinces(Set<Province> provinces) {
        this.provinces = provinces;
    }

    public Cso provinces(Set<Province> provinces) {
        this.setProvinces(provinces);
        return this;
    }

    public Cso addProvince(Province province) {
        this.provinces.add(province);
        province.getCsos().add(this);
        return this;
    }

    public Cso removeProvince(Province province) {
        this.provinces.remove(province);
        province.getCsos().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cso)) {
            return false;
        }
        return id != null && id.equals(((Cso) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cso{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", organisation='" + getOrganisation() + "'" +
            ", cell='" + getCell() + "'" +
            ", city='" + getCity() + "'" +
            ", email='" + getEmail() + "'" +
            ", registrationCertificate='" + getRegistrationCertificate() + "'" +
            ", organisationProfile='" + getOrganisationProfile() + "'" +
            ", managementStructure='" + getManagementStructure() + "'" +
            ", strategicPlan='" + getStrategicPlan() + "'" +
            ", resourceMobilisationPlan='" + getResourceMobilisationPlan() + "'" +
            ", comments='" + getComments() + "'" +
            ", enquiries='" + getEnquiries() + "'" +
            ", lat=" + getLat() +
            ", lng=" + getLng() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            "}";
    }
}
