package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.FrameworkType;
import com.mycompany.myapp.repository.FrameworkTypeRepository;
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
 * Integration tests for the {@link FrameworkTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FrameworkTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/framework-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FrameworkTypeRepository frameworkTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFrameworkTypeMockMvc;

    private FrameworkType frameworkType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FrameworkType createEntity(EntityManager em) {
        FrameworkType frameworkType = new FrameworkType()
            .name(DEFAULT_NAME)
            .createdDate(DEFAULT_CREATED_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
        return frameworkType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FrameworkType createUpdatedEntity(EntityManager em) {
        FrameworkType frameworkType = new FrameworkType()
            .name(UPDATED_NAME)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
        return frameworkType;
    }

    @BeforeEach
    public void initTest() {
        frameworkType = createEntity(em);
    }

    @Test
    @Transactional
    void createFrameworkType() throws Exception {
        int databaseSizeBeforeCreate = frameworkTypeRepository.findAll().size();
        // Create the FrameworkType
        restFrameworkTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(frameworkType)))
            .andExpect(status().isCreated());

        // Validate the FrameworkType in the database
        List<FrameworkType> frameworkTypeList = frameworkTypeRepository.findAll();
        assertThat(frameworkTypeList).hasSize(databaseSizeBeforeCreate + 1);
        FrameworkType testFrameworkType = frameworkTypeList.get(frameworkTypeList.size() - 1);
        assertThat(testFrameworkType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFrameworkType.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testFrameworkType.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createFrameworkTypeWithExistingId() throws Exception {
        // Create the FrameworkType with an existing ID
        frameworkType.setId(1L);

        int databaseSizeBeforeCreate = frameworkTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFrameworkTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(frameworkType)))
            .andExpect(status().isBadRequest());

        // Validate the FrameworkType in the database
        List<FrameworkType> frameworkTypeList = frameworkTypeRepository.findAll();
        assertThat(frameworkTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = frameworkTypeRepository.findAll().size();
        // set the field null
        frameworkType.setName(null);

        // Create the FrameworkType, which fails.

        restFrameworkTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(frameworkType)))
            .andExpect(status().isBadRequest());

        List<FrameworkType> frameworkTypeList = frameworkTypeRepository.findAll();
        assertThat(frameworkTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFrameworkTypes() throws Exception {
        // Initialize the database
        frameworkTypeRepository.saveAndFlush(frameworkType);

        // Get all the frameworkTypeList
        restFrameworkTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(frameworkType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getFrameworkType() throws Exception {
        // Initialize the database
        frameworkTypeRepository.saveAndFlush(frameworkType);

        // Get the frameworkType
        restFrameworkTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, frameworkType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(frameworkType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFrameworkType() throws Exception {
        // Get the frameworkType
        restFrameworkTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFrameworkType() throws Exception {
        // Initialize the database
        frameworkTypeRepository.saveAndFlush(frameworkType);

        int databaseSizeBeforeUpdate = frameworkTypeRepository.findAll().size();

        // Update the frameworkType
        FrameworkType updatedFrameworkType = frameworkTypeRepository.findById(frameworkType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFrameworkType are not directly saved in db
        em.detach(updatedFrameworkType);
        updatedFrameworkType.name(UPDATED_NAME).createdDate(UPDATED_CREATED_DATE).modifiedDate(UPDATED_MODIFIED_DATE);

        restFrameworkTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFrameworkType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFrameworkType))
            )
            .andExpect(status().isOk());

        // Validate the FrameworkType in the database
        List<FrameworkType> frameworkTypeList = frameworkTypeRepository.findAll();
        assertThat(frameworkTypeList).hasSize(databaseSizeBeforeUpdate);
        FrameworkType testFrameworkType = frameworkTypeList.get(frameworkTypeList.size() - 1);
        assertThat(testFrameworkType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFrameworkType.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testFrameworkType.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingFrameworkType() throws Exception {
        int databaseSizeBeforeUpdate = frameworkTypeRepository.findAll().size();
        frameworkType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFrameworkTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, frameworkType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(frameworkType))
            )
            .andExpect(status().isBadRequest());

        // Validate the FrameworkType in the database
        List<FrameworkType> frameworkTypeList = frameworkTypeRepository.findAll();
        assertThat(frameworkTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFrameworkType() throws Exception {
        int databaseSizeBeforeUpdate = frameworkTypeRepository.findAll().size();
        frameworkType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFrameworkTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(frameworkType))
            )
            .andExpect(status().isBadRequest());

        // Validate the FrameworkType in the database
        List<FrameworkType> frameworkTypeList = frameworkTypeRepository.findAll();
        assertThat(frameworkTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFrameworkType() throws Exception {
        int databaseSizeBeforeUpdate = frameworkTypeRepository.findAll().size();
        frameworkType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFrameworkTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(frameworkType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FrameworkType in the database
        List<FrameworkType> frameworkTypeList = frameworkTypeRepository.findAll();
        assertThat(frameworkTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFrameworkTypeWithPatch() throws Exception {
        // Initialize the database
        frameworkTypeRepository.saveAndFlush(frameworkType);

        int databaseSizeBeforeUpdate = frameworkTypeRepository.findAll().size();

        // Update the frameworkType using partial update
        FrameworkType partialUpdatedFrameworkType = new FrameworkType();
        partialUpdatedFrameworkType.setId(frameworkType.getId());

        partialUpdatedFrameworkType.name(UPDATED_NAME).createdDate(UPDATED_CREATED_DATE);

        restFrameworkTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFrameworkType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFrameworkType))
            )
            .andExpect(status().isOk());

        // Validate the FrameworkType in the database
        List<FrameworkType> frameworkTypeList = frameworkTypeRepository.findAll();
        assertThat(frameworkTypeList).hasSize(databaseSizeBeforeUpdate);
        FrameworkType testFrameworkType = frameworkTypeList.get(frameworkTypeList.size() - 1);
        assertThat(testFrameworkType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFrameworkType.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testFrameworkType.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateFrameworkTypeWithPatch() throws Exception {
        // Initialize the database
        frameworkTypeRepository.saveAndFlush(frameworkType);

        int databaseSizeBeforeUpdate = frameworkTypeRepository.findAll().size();

        // Update the frameworkType using partial update
        FrameworkType partialUpdatedFrameworkType = new FrameworkType();
        partialUpdatedFrameworkType.setId(frameworkType.getId());

        partialUpdatedFrameworkType.name(UPDATED_NAME).createdDate(UPDATED_CREATED_DATE).modifiedDate(UPDATED_MODIFIED_DATE);

        restFrameworkTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFrameworkType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFrameworkType))
            )
            .andExpect(status().isOk());

        // Validate the FrameworkType in the database
        List<FrameworkType> frameworkTypeList = frameworkTypeRepository.findAll();
        assertThat(frameworkTypeList).hasSize(databaseSizeBeforeUpdate);
        FrameworkType testFrameworkType = frameworkTypeList.get(frameworkTypeList.size() - 1);
        assertThat(testFrameworkType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFrameworkType.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testFrameworkType.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingFrameworkType() throws Exception {
        int databaseSizeBeforeUpdate = frameworkTypeRepository.findAll().size();
        frameworkType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFrameworkTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, frameworkType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(frameworkType))
            )
            .andExpect(status().isBadRequest());

        // Validate the FrameworkType in the database
        List<FrameworkType> frameworkTypeList = frameworkTypeRepository.findAll();
        assertThat(frameworkTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFrameworkType() throws Exception {
        int databaseSizeBeforeUpdate = frameworkTypeRepository.findAll().size();
        frameworkType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFrameworkTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(frameworkType))
            )
            .andExpect(status().isBadRequest());

        // Validate the FrameworkType in the database
        List<FrameworkType> frameworkTypeList = frameworkTypeRepository.findAll();
        assertThat(frameworkTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFrameworkType() throws Exception {
        int databaseSizeBeforeUpdate = frameworkTypeRepository.findAll().size();
        frameworkType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFrameworkTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(frameworkType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FrameworkType in the database
        List<FrameworkType> frameworkTypeList = frameworkTypeRepository.findAll();
        assertThat(frameworkTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFrameworkType() throws Exception {
        // Initialize the database
        frameworkTypeRepository.saveAndFlush(frameworkType);

        int databaseSizeBeforeDelete = frameworkTypeRepository.findAll().size();

        // Delete the frameworkType
        restFrameworkTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, frameworkType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FrameworkType> frameworkTypeList = frameworkTypeRepository.findAll();
        assertThat(frameworkTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
