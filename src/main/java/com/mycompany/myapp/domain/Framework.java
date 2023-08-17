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
 * A Framework.
 */
@Entity
@Table(name = "framework")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Framework implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "modified_date")
    private Instant modifiedDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_framework__framework_type",
        joinColumns = @JoinColumn(name = "framework_id"),
        inverseJoinColumns = @JoinColumn(name = "framework_type_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "frameworks" }, allowSetters = true)
    private Set<FrameworkType> frameworkTypes = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_framework__file_upload",
        joinColumns = @JoinColumn(name = "framework_id"),
        inverseJoinColumns = @JoinColumn(name = "file_upload_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "frameworks" }, allowSetters = true)
    private Set<FileUpload> fileUploads = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Framework id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Framework name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Framework createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getModifiedDate() {
        return this.modifiedDate;
    }

    public Framework modifiedDate(Instant modifiedDate) {
        this.setModifiedDate(modifiedDate);
        return this;
    }

    public void setModifiedDate(Instant modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Set<FrameworkType> getFrameworkTypes() {
        return this.frameworkTypes;
    }

    public void setFrameworkTypes(Set<FrameworkType> frameworkTypes) {
        this.frameworkTypes = frameworkTypes;
    }

    public Framework frameworkTypes(Set<FrameworkType> frameworkTypes) {
        this.setFrameworkTypes(frameworkTypes);
        return this;
    }

    public Framework addFrameworkType(FrameworkType frameworkType) {
        this.frameworkTypes.add(frameworkType);
        frameworkType.getFrameworks().add(this);
        return this;
    }

    public Framework removeFrameworkType(FrameworkType frameworkType) {
        this.frameworkTypes.remove(frameworkType);
        frameworkType.getFrameworks().remove(this);
        return this;
    }

    public Set<FileUpload> getFileUploads() {
        return this.fileUploads;
    }

    public void setFileUploads(Set<FileUpload> fileUploads) {
        this.fileUploads = fileUploads;
    }

    public Framework fileUploads(Set<FileUpload> fileUploads) {
        this.setFileUploads(fileUploads);
        return this;
    }

    public Framework addFileUpload(FileUpload fileUpload) {
        this.fileUploads.add(fileUpload);
        fileUpload.getFrameworks().add(this);
        return this;
    }

    public Framework removeFileUpload(FileUpload fileUpload) {
        this.fileUploads.remove(fileUpload);
        fileUpload.getFrameworks().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Framework)) {
            return false;
        }
        return id != null && id.equals(((Framework) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Framework{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            "}";
    }
}
