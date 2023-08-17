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
 * A FileUpload.
 */
@Entity
@Table(name = "file_upload")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FileUpload implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "filename", nullable = false)
    private String filename;

    @NotNull
    @Column(name = "original_filename", nullable = false)
    private String originalFilename;

    @NotNull
    @Column(name = "extension", nullable = false)
    private String extension;

    @NotNull
    @Column(name = "size_in_bytes", nullable = false)
    private Integer sizeInBytes;

    @NotNull
    @Column(name = "sha_256", nullable = false)
    private String sha256;

    @NotNull
    @Column(name = "content_type", nullable = false)
    private String contentType;

    @NotNull
    @Column(name = "upload_date", nullable = false)
    private Instant uploadDate;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "fileUploads")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "frameworkTypes", "fileUploads" }, allowSetters = true)
    private Set<Framework> frameworks = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FileUpload id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilename() {
        return this.filename;
    }

    public FileUpload filename(String filename) {
        this.setFilename(filename);
        return this;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getOriginalFilename() {
        return this.originalFilename;
    }

    public FileUpload originalFilename(String originalFilename) {
        this.setOriginalFilename(originalFilename);
        return this;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getExtension() {
        return this.extension;
    }

    public FileUpload extension(String extension) {
        this.setExtension(extension);
        return this;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Integer getSizeInBytes() {
        return this.sizeInBytes;
    }

    public FileUpload sizeInBytes(Integer sizeInBytes) {
        this.setSizeInBytes(sizeInBytes);
        return this;
    }

    public void setSizeInBytes(Integer sizeInBytes) {
        this.sizeInBytes = sizeInBytes;
    }

    public String getSha256() {
        return this.sha256;
    }

    public FileUpload sha256(String sha256) {
        this.setSha256(sha256);
        return this;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public String getContentType() {
        return this.contentType;
    }

    public FileUpload contentType(String contentType) {
        this.setContentType(contentType);
        return this;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Instant getUploadDate() {
        return this.uploadDate;
    }

    public FileUpload uploadDate(Instant uploadDate) {
        this.setUploadDate(uploadDate);
        return this;
    }

    public void setUploadDate(Instant uploadDate) {
        this.uploadDate = uploadDate;
    }

    public Set<Framework> getFrameworks() {
        return this.frameworks;
    }

    public void setFrameworks(Set<Framework> frameworks) {
        if (this.frameworks != null) {
            this.frameworks.forEach(i -> i.removeFileUpload(this));
        }
        if (frameworks != null) {
            frameworks.forEach(i -> i.addFileUpload(this));
        }
        this.frameworks = frameworks;
    }

    public FileUpload frameworks(Set<Framework> frameworks) {
        this.setFrameworks(frameworks);
        return this;
    }

    public FileUpload addFramework(Framework framework) {
        this.frameworks.add(framework);
        framework.getFileUploads().add(this);
        return this;
    }

    public FileUpload removeFramework(Framework framework) {
        this.frameworks.remove(framework);
        framework.getFileUploads().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FileUpload)) {
            return false;
        }
        return id != null && id.equals(((FileUpload) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FileUpload{" +
            "id=" + getId() +
            ", filename='" + getFilename() + "'" +
            ", originalFilename='" + getOriginalFilename() + "'" +
            ", extension='" + getExtension() + "'" +
            ", sizeInBytes=" + getSizeInBytes() +
            ", sha256='" + getSha256() + "'" +
            ", contentType='" + getContentType() + "'" +
            ", uploadDate='" + getUploadDate() + "'" +
            "}";
    }
}
