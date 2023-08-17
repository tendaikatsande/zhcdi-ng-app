package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.FileUpload;
import com.mycompany.myapp.repository.FileUploadRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FileUploadResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FileUploadResourceIT {

    private static final String DEFAULT_FILENAME = "AAAAAAAAAA";
    private static final String UPDATED_FILENAME = "BBBBBBBBBB";

    private static final String DEFAULT_ORIGINAL_FILENAME = "AAAAAAAAAA";
    private static final String UPDATED_ORIGINAL_FILENAME = "BBBBBBBBBB";

    private static final String DEFAULT_EXTENSION = "AAAAAAAAAA";
    private static final String UPDATED_EXTENSION = "BBBBBBBBBB";

    private static final Integer DEFAULT_SIZE_IN_BYTES = 1;
    private static final Integer UPDATED_SIZE_IN_BYTES = 2;

    private static final String DEFAULT_SHA_256 = "AAAAAAAAAA";
    private static final String UPDATED_SHA_256 = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_TYPE = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPLOAD_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPLOAD_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/file-uploads";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FileUploadRepository fileUploadRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFileUploadMockMvc;

    private FileUpload fileUpload;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FileUpload createEntity(EntityManager em) {
        FileUpload fileUpload = new FileUpload()
            .filename(DEFAULT_FILENAME)
            .originalFilename(DEFAULT_ORIGINAL_FILENAME)
            .extension(DEFAULT_EXTENSION)
            .sizeInBytes(DEFAULT_SIZE_IN_BYTES)
            .sha256(DEFAULT_SHA_256)
            .contentType(DEFAULT_CONTENT_TYPE)
            .uploadDate(DEFAULT_UPLOAD_DATE);
        return fileUpload;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FileUpload createUpdatedEntity(EntityManager em) {
        FileUpload fileUpload = new FileUpload()
            .filename(UPDATED_FILENAME)
            .originalFilename(UPDATED_ORIGINAL_FILENAME)
            .extension(UPDATED_EXTENSION)
            .sizeInBytes(UPDATED_SIZE_IN_BYTES)
            .sha256(UPDATED_SHA_256)
            .contentType(UPDATED_CONTENT_TYPE)
            .uploadDate(UPDATED_UPLOAD_DATE);
        return fileUpload;
    }

    @BeforeEach
    public void initTest() {
        fileUpload = createEntity(em);
    }

    @Test
    @Transactional
    void createFileUpload() throws Exception {
        int databaseSizeBeforeCreate = fileUploadRepository.findAll().size();
        // Create the FileUpload
        restFileUploadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileUpload)))
            .andExpect(status().isCreated());

        // Validate the FileUpload in the database
        List<FileUpload> fileUploadList = fileUploadRepository.findAll();
        assertThat(fileUploadList).hasSize(databaseSizeBeforeCreate + 1);
        FileUpload testFileUpload = fileUploadList.get(fileUploadList.size() - 1);
        assertThat(testFileUpload.getFilename()).isEqualTo(DEFAULT_FILENAME);
        assertThat(testFileUpload.getOriginalFilename()).isEqualTo(DEFAULT_ORIGINAL_FILENAME);
        assertThat(testFileUpload.getExtension()).isEqualTo(DEFAULT_EXTENSION);
        assertThat(testFileUpload.getSizeInBytes()).isEqualTo(DEFAULT_SIZE_IN_BYTES);
        assertThat(testFileUpload.getSha256()).isEqualTo(DEFAULT_SHA_256);
        assertThat(testFileUpload.getContentType()).isEqualTo(DEFAULT_CONTENT_TYPE);
        assertThat(testFileUpload.getUploadDate()).isEqualTo(DEFAULT_UPLOAD_DATE);
    }

    @Test
    @Transactional
    void createFileUploadWithExistingId() throws Exception {
        // Create the FileUpload with an existing ID
        fileUpload.setId(1L);

        int databaseSizeBeforeCreate = fileUploadRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFileUploadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileUpload)))
            .andExpect(status().isBadRequest());

        // Validate the FileUpload in the database
        List<FileUpload> fileUploadList = fileUploadRepository.findAll();
        assertThat(fileUploadList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFilenameIsRequired() throws Exception {
        int databaseSizeBeforeTest = fileUploadRepository.findAll().size();
        // set the field null
        fileUpload.setFilename(null);

        // Create the FileUpload, which fails.

        restFileUploadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileUpload)))
            .andExpect(status().isBadRequest());

        List<FileUpload> fileUploadList = fileUploadRepository.findAll();
        assertThat(fileUploadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOriginalFilenameIsRequired() throws Exception {
        int databaseSizeBeforeTest = fileUploadRepository.findAll().size();
        // set the field null
        fileUpload.setOriginalFilename(null);

        // Create the FileUpload, which fails.

        restFileUploadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileUpload)))
            .andExpect(status().isBadRequest());

        List<FileUpload> fileUploadList = fileUploadRepository.findAll();
        assertThat(fileUploadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExtensionIsRequired() throws Exception {
        int databaseSizeBeforeTest = fileUploadRepository.findAll().size();
        // set the field null
        fileUpload.setExtension(null);

        // Create the FileUpload, which fails.

        restFileUploadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileUpload)))
            .andExpect(status().isBadRequest());

        List<FileUpload> fileUploadList = fileUploadRepository.findAll();
        assertThat(fileUploadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSizeInBytesIsRequired() throws Exception {
        int databaseSizeBeforeTest = fileUploadRepository.findAll().size();
        // set the field null
        fileUpload.setSizeInBytes(null);

        // Create the FileUpload, which fails.

        restFileUploadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileUpload)))
            .andExpect(status().isBadRequest());

        List<FileUpload> fileUploadList = fileUploadRepository.findAll();
        assertThat(fileUploadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSha256IsRequired() throws Exception {
        int databaseSizeBeforeTest = fileUploadRepository.findAll().size();
        // set the field null
        fileUpload.setSha256(null);

        // Create the FileUpload, which fails.

        restFileUploadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileUpload)))
            .andExpect(status().isBadRequest());

        List<FileUpload> fileUploadList = fileUploadRepository.findAll();
        assertThat(fileUploadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContentTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = fileUploadRepository.findAll().size();
        // set the field null
        fileUpload.setContentType(null);

        // Create the FileUpload, which fails.

        restFileUploadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileUpload)))
            .andExpect(status().isBadRequest());

        List<FileUpload> fileUploadList = fileUploadRepository.findAll();
        assertThat(fileUploadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUploadDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = fileUploadRepository.findAll().size();
        // set the field null
        fileUpload.setUploadDate(null);

        // Create the FileUpload, which fails.

        restFileUploadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileUpload)))
            .andExpect(status().isBadRequest());

        List<FileUpload> fileUploadList = fileUploadRepository.findAll();
        assertThat(fileUploadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFileUploads() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList
        restFileUploadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fileUpload.getId().intValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME)))
            .andExpect(jsonPath("$.[*].originalFilename").value(hasItem(DEFAULT_ORIGINAL_FILENAME)))
            .andExpect(jsonPath("$.[*].extension").value(hasItem(DEFAULT_EXTENSION)))
            .andExpect(jsonPath("$.[*].sizeInBytes").value(hasItem(DEFAULT_SIZE_IN_BYTES)))
            .andExpect(jsonPath("$.[*].sha256").value(hasItem(DEFAULT_SHA_256)))
            .andExpect(jsonPath("$.[*].contentType").value(hasItem(DEFAULT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].uploadDate").value(hasItem(DEFAULT_UPLOAD_DATE.toString())));
    }

    @Test
    @Transactional
    void getFileUpload() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get the fileUpload
        restFileUploadMockMvc
            .perform(get(ENTITY_API_URL_ID, fileUpload.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fileUpload.getId().intValue()))
            .andExpect(jsonPath("$.filename").value(DEFAULT_FILENAME))
            .andExpect(jsonPath("$.originalFilename").value(DEFAULT_ORIGINAL_FILENAME))
            .andExpect(jsonPath("$.extension").value(DEFAULT_EXTENSION))
            .andExpect(jsonPath("$.sizeInBytes").value(DEFAULT_SIZE_IN_BYTES))
            .andExpect(jsonPath("$.sha256").value(DEFAULT_SHA_256))
            .andExpect(jsonPath("$.contentType").value(DEFAULT_CONTENT_TYPE))
            .andExpect(jsonPath("$.uploadDate").value(DEFAULT_UPLOAD_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFileUpload() throws Exception {
        // Get the fileUpload
        restFileUploadMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFileUpload() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        int databaseSizeBeforeUpdate = fileUploadRepository.findAll().size();

        // Update the fileUpload
        FileUpload updatedFileUpload = fileUploadRepository.findById(fileUpload.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFileUpload are not directly saved in db
        em.detach(updatedFileUpload);
        updatedFileUpload
            .filename(UPDATED_FILENAME)
            .originalFilename(UPDATED_ORIGINAL_FILENAME)
            .extension(UPDATED_EXTENSION)
            .sizeInBytes(UPDATED_SIZE_IN_BYTES)
            .sha256(UPDATED_SHA_256)
            .contentType(UPDATED_CONTENT_TYPE)
            .uploadDate(UPDATED_UPLOAD_DATE);

        restFileUploadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFileUpload.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFileUpload))
            )
            .andExpect(status().isOk());

        // Validate the FileUpload in the database
        List<FileUpload> fileUploadList = fileUploadRepository.findAll();
        assertThat(fileUploadList).hasSize(databaseSizeBeforeUpdate);
        FileUpload testFileUpload = fileUploadList.get(fileUploadList.size() - 1);
        assertThat(testFileUpload.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testFileUpload.getOriginalFilename()).isEqualTo(UPDATED_ORIGINAL_FILENAME);
        assertThat(testFileUpload.getExtension()).isEqualTo(UPDATED_EXTENSION);
        assertThat(testFileUpload.getSizeInBytes()).isEqualTo(UPDATED_SIZE_IN_BYTES);
        assertThat(testFileUpload.getSha256()).isEqualTo(UPDATED_SHA_256);
        assertThat(testFileUpload.getContentType()).isEqualTo(UPDATED_CONTENT_TYPE);
        assertThat(testFileUpload.getUploadDate()).isEqualTo(UPDATED_UPLOAD_DATE);
    }

    @Test
    @Transactional
    void putNonExistingFileUpload() throws Exception {
        int databaseSizeBeforeUpdate = fileUploadRepository.findAll().size();
        fileUpload.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileUploadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fileUpload.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fileUpload))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileUpload in the database
        List<FileUpload> fileUploadList = fileUploadRepository.findAll();
        assertThat(fileUploadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFileUpload() throws Exception {
        int databaseSizeBeforeUpdate = fileUploadRepository.findAll().size();
        fileUpload.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileUploadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fileUpload))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileUpload in the database
        List<FileUpload> fileUploadList = fileUploadRepository.findAll();
        assertThat(fileUploadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFileUpload() throws Exception {
        int databaseSizeBeforeUpdate = fileUploadRepository.findAll().size();
        fileUpload.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileUploadMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileUpload)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FileUpload in the database
        List<FileUpload> fileUploadList = fileUploadRepository.findAll();
        assertThat(fileUploadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFileUploadWithPatch() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        int databaseSizeBeforeUpdate = fileUploadRepository.findAll().size();

        // Update the fileUpload using partial update
        FileUpload partialUpdatedFileUpload = new FileUpload();
        partialUpdatedFileUpload.setId(fileUpload.getId());

        partialUpdatedFileUpload
            .filename(UPDATED_FILENAME)
            .originalFilename(UPDATED_ORIGINAL_FILENAME)
            .extension(UPDATED_EXTENSION)
            .sizeInBytes(UPDATED_SIZE_IN_BYTES)
            .contentType(UPDATED_CONTENT_TYPE)
            .uploadDate(UPDATED_UPLOAD_DATE);

        restFileUploadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFileUpload.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFileUpload))
            )
            .andExpect(status().isOk());

        // Validate the FileUpload in the database
        List<FileUpload> fileUploadList = fileUploadRepository.findAll();
        assertThat(fileUploadList).hasSize(databaseSizeBeforeUpdate);
        FileUpload testFileUpload = fileUploadList.get(fileUploadList.size() - 1);
        assertThat(testFileUpload.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testFileUpload.getOriginalFilename()).isEqualTo(UPDATED_ORIGINAL_FILENAME);
        assertThat(testFileUpload.getExtension()).isEqualTo(UPDATED_EXTENSION);
        assertThat(testFileUpload.getSizeInBytes()).isEqualTo(UPDATED_SIZE_IN_BYTES);
        assertThat(testFileUpload.getSha256()).isEqualTo(DEFAULT_SHA_256);
        assertThat(testFileUpload.getContentType()).isEqualTo(UPDATED_CONTENT_TYPE);
        assertThat(testFileUpload.getUploadDate()).isEqualTo(UPDATED_UPLOAD_DATE);
    }

    @Test
    @Transactional
    void fullUpdateFileUploadWithPatch() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        int databaseSizeBeforeUpdate = fileUploadRepository.findAll().size();

        // Update the fileUpload using partial update
        FileUpload partialUpdatedFileUpload = new FileUpload();
        partialUpdatedFileUpload.setId(fileUpload.getId());

        partialUpdatedFileUpload
            .filename(UPDATED_FILENAME)
            .originalFilename(UPDATED_ORIGINAL_FILENAME)
            .extension(UPDATED_EXTENSION)
            .sizeInBytes(UPDATED_SIZE_IN_BYTES)
            .sha256(UPDATED_SHA_256)
            .contentType(UPDATED_CONTENT_TYPE)
            .uploadDate(UPDATED_UPLOAD_DATE);

        restFileUploadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFileUpload.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFileUpload))
            )
            .andExpect(status().isOk());

        // Validate the FileUpload in the database
        List<FileUpload> fileUploadList = fileUploadRepository.findAll();
        assertThat(fileUploadList).hasSize(databaseSizeBeforeUpdate);
        FileUpload testFileUpload = fileUploadList.get(fileUploadList.size() - 1);
        assertThat(testFileUpload.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testFileUpload.getOriginalFilename()).isEqualTo(UPDATED_ORIGINAL_FILENAME);
        assertThat(testFileUpload.getExtension()).isEqualTo(UPDATED_EXTENSION);
        assertThat(testFileUpload.getSizeInBytes()).isEqualTo(UPDATED_SIZE_IN_BYTES);
        assertThat(testFileUpload.getSha256()).isEqualTo(UPDATED_SHA_256);
        assertThat(testFileUpload.getContentType()).isEqualTo(UPDATED_CONTENT_TYPE);
        assertThat(testFileUpload.getUploadDate()).isEqualTo(UPDATED_UPLOAD_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingFileUpload() throws Exception {
        int databaseSizeBeforeUpdate = fileUploadRepository.findAll().size();
        fileUpload.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileUploadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fileUpload.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fileUpload))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileUpload in the database
        List<FileUpload> fileUploadList = fileUploadRepository.findAll();
        assertThat(fileUploadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFileUpload() throws Exception {
        int databaseSizeBeforeUpdate = fileUploadRepository.findAll().size();
        fileUpload.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileUploadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fileUpload))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileUpload in the database
        List<FileUpload> fileUploadList = fileUploadRepository.findAll();
        assertThat(fileUploadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFileUpload() throws Exception {
        int databaseSizeBeforeUpdate = fileUploadRepository.findAll().size();
        fileUpload.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileUploadMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(fileUpload))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FileUpload in the database
        List<FileUpload> fileUploadList = fileUploadRepository.findAll();
        assertThat(fileUploadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFileUpload() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        int databaseSizeBeforeDelete = fileUploadRepository.findAll().size();

        // Delete the fileUpload
        restFileUploadMockMvc
            .perform(delete(ENTITY_API_URL_ID, fileUpload.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FileUpload> fileUploadList = fileUploadRepository.findAll();
        assertThat(fileUploadList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
