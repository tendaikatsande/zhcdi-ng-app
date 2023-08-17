package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Framework;
import com.mycompany.myapp.repository.FrameworkRepository;
import com.mycompany.myapp.service.FrameworkService;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FrameworkResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FrameworkResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/frameworks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FrameworkRepository frameworkRepository;

    @Mock
    private FrameworkRepository frameworkRepositoryMock;

    @Mock
    private FrameworkService frameworkServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFrameworkMockMvc;

    private Framework framework;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Framework createEntity(EntityManager em) {
        Framework framework = new Framework().name(DEFAULT_NAME).createdDate(DEFAULT_CREATED_DATE).modifiedDate(DEFAULT_MODIFIED_DATE);
        return framework;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Framework createUpdatedEntity(EntityManager em) {
        Framework framework = new Framework().name(UPDATED_NAME).createdDate(UPDATED_CREATED_DATE).modifiedDate(UPDATED_MODIFIED_DATE);
        return framework;
    }

    @BeforeEach
    public void initTest() {
        framework = createEntity(em);
    }

    @Test
    @Transactional
    void createFramework() throws Exception {
        int databaseSizeBeforeCreate = frameworkRepository.findAll().size();
        // Create the Framework
        restFrameworkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(framework)))
            .andExpect(status().isCreated());

        // Validate the Framework in the database
        List<Framework> frameworkList = frameworkRepository.findAll();
        assertThat(frameworkList).hasSize(databaseSizeBeforeCreate + 1);
        Framework testFramework = frameworkList.get(frameworkList.size() - 1);
        assertThat(testFramework.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFramework.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testFramework.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createFrameworkWithExistingId() throws Exception {
        // Create the Framework with an existing ID
        framework.setId(1L);

        int databaseSizeBeforeCreate = frameworkRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFrameworkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(framework)))
            .andExpect(status().isBadRequest());

        // Validate the Framework in the database
        List<Framework> frameworkList = frameworkRepository.findAll();
        assertThat(frameworkList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = frameworkRepository.findAll().size();
        // set the field null
        framework.setName(null);

        // Create the Framework, which fails.

        restFrameworkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(framework)))
            .andExpect(status().isBadRequest());

        List<Framework> frameworkList = frameworkRepository.findAll();
        assertThat(frameworkList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFrameworks() throws Exception {
        // Initialize the database
        frameworkRepository.saveAndFlush(framework);

        // Get all the frameworkList
        restFrameworkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(framework.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFrameworksWithEagerRelationshipsIsEnabled() throws Exception {
        when(frameworkServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFrameworkMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(frameworkServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFrameworksWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(frameworkServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFrameworkMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(frameworkRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getFramework() throws Exception {
        // Initialize the database
        frameworkRepository.saveAndFlush(framework);

        // Get the framework
        restFrameworkMockMvc
            .perform(get(ENTITY_API_URL_ID, framework.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(framework.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFramework() throws Exception {
        // Get the framework
        restFrameworkMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFramework() throws Exception {
        // Initialize the database
        frameworkRepository.saveAndFlush(framework);

        int databaseSizeBeforeUpdate = frameworkRepository.findAll().size();

        // Update the framework
        Framework updatedFramework = frameworkRepository.findById(framework.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFramework are not directly saved in db
        em.detach(updatedFramework);
        updatedFramework.name(UPDATED_NAME).createdDate(UPDATED_CREATED_DATE).modifiedDate(UPDATED_MODIFIED_DATE);

        restFrameworkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFramework.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFramework))
            )
            .andExpect(status().isOk());

        // Validate the Framework in the database
        List<Framework> frameworkList = frameworkRepository.findAll();
        assertThat(frameworkList).hasSize(databaseSizeBeforeUpdate);
        Framework testFramework = frameworkList.get(frameworkList.size() - 1);
        assertThat(testFramework.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFramework.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testFramework.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingFramework() throws Exception {
        int databaseSizeBeforeUpdate = frameworkRepository.findAll().size();
        framework.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFrameworkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, framework.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(framework))
            )
            .andExpect(status().isBadRequest());

        // Validate the Framework in the database
        List<Framework> frameworkList = frameworkRepository.findAll();
        assertThat(frameworkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFramework() throws Exception {
        int databaseSizeBeforeUpdate = frameworkRepository.findAll().size();
        framework.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFrameworkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(framework))
            )
            .andExpect(status().isBadRequest());

        // Validate the Framework in the database
        List<Framework> frameworkList = frameworkRepository.findAll();
        assertThat(frameworkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFramework() throws Exception {
        int databaseSizeBeforeUpdate = frameworkRepository.findAll().size();
        framework.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFrameworkMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(framework)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Framework in the database
        List<Framework> frameworkList = frameworkRepository.findAll();
        assertThat(frameworkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFrameworkWithPatch() throws Exception {
        // Initialize the database
        frameworkRepository.saveAndFlush(framework);

        int databaseSizeBeforeUpdate = frameworkRepository.findAll().size();

        // Update the framework using partial update
        Framework partialUpdatedFramework = new Framework();
        partialUpdatedFramework.setId(framework.getId());

        partialUpdatedFramework.name(UPDATED_NAME).createdDate(UPDATED_CREATED_DATE).modifiedDate(UPDATED_MODIFIED_DATE);

        restFrameworkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFramework.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFramework))
            )
            .andExpect(status().isOk());

        // Validate the Framework in the database
        List<Framework> frameworkList = frameworkRepository.findAll();
        assertThat(frameworkList).hasSize(databaseSizeBeforeUpdate);
        Framework testFramework = frameworkList.get(frameworkList.size() - 1);
        assertThat(testFramework.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFramework.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testFramework.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateFrameworkWithPatch() throws Exception {
        // Initialize the database
        frameworkRepository.saveAndFlush(framework);

        int databaseSizeBeforeUpdate = frameworkRepository.findAll().size();

        // Update the framework using partial update
        Framework partialUpdatedFramework = new Framework();
        partialUpdatedFramework.setId(framework.getId());

        partialUpdatedFramework.name(UPDATED_NAME).createdDate(UPDATED_CREATED_DATE).modifiedDate(UPDATED_MODIFIED_DATE);

        restFrameworkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFramework.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFramework))
            )
            .andExpect(status().isOk());

        // Validate the Framework in the database
        List<Framework> frameworkList = frameworkRepository.findAll();
        assertThat(frameworkList).hasSize(databaseSizeBeforeUpdate);
        Framework testFramework = frameworkList.get(frameworkList.size() - 1);
        assertThat(testFramework.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFramework.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testFramework.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingFramework() throws Exception {
        int databaseSizeBeforeUpdate = frameworkRepository.findAll().size();
        framework.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFrameworkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, framework.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(framework))
            )
            .andExpect(status().isBadRequest());

        // Validate the Framework in the database
        List<Framework> frameworkList = frameworkRepository.findAll();
        assertThat(frameworkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFramework() throws Exception {
        int databaseSizeBeforeUpdate = frameworkRepository.findAll().size();
        framework.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFrameworkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(framework))
            )
            .andExpect(status().isBadRequest());

        // Validate the Framework in the database
        List<Framework> frameworkList = frameworkRepository.findAll();
        assertThat(frameworkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFramework() throws Exception {
        int databaseSizeBeforeUpdate = frameworkRepository.findAll().size();
        framework.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFrameworkMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(framework))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Framework in the database
        List<Framework> frameworkList = frameworkRepository.findAll();
        assertThat(frameworkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFramework() throws Exception {
        // Initialize the database
        frameworkRepository.saveAndFlush(framework);

        int databaseSizeBeforeDelete = frameworkRepository.findAll().size();

        // Delete the framework
        restFrameworkMockMvc
            .perform(delete(ENTITY_API_URL_ID, framework.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Framework> frameworkList = frameworkRepository.findAll();
        assertThat(frameworkList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
