package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Cso;
import com.mycompany.myapp.repository.CsoRepository;
import com.mycompany.myapp.service.CsoService;
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
 * Integration tests for the {@link CsoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CsoResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ORGANISATION = "AAAAAAAAAA";
    private static final String UPDATED_ORGANISATION = "BBBBBBBBBB";

    private static final String DEFAULT_CELL = "AAAAAAAAAA";
    private static final String UPDATED_CELL = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_REGISTRATION_CERTIFICATE = false;
    private static final Boolean UPDATED_REGISTRATION_CERTIFICATE = true;

    private static final Boolean DEFAULT_ORGANISATION_PROFILE = false;
    private static final Boolean UPDATED_ORGANISATION_PROFILE = true;

    private static final Boolean DEFAULT_MANAGEMENT_STRUCTURE = false;
    private static final Boolean UPDATED_MANAGEMENT_STRUCTURE = true;

    private static final Boolean DEFAULT_STRATEGIC_PLAN = false;
    private static final Boolean UPDATED_STRATEGIC_PLAN = true;

    private static final Boolean DEFAULT_RESOURCE_MOBILISATION_PLAN = false;
    private static final Boolean UPDATED_RESOURCE_MOBILISATION_PLAN = true;

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    private static final String DEFAULT_ENQUIRIES = "AAAAAAAAAA";
    private static final String UPDATED_ENQUIRIES = "BBBBBBBBBB";

    private static final Float DEFAULT_LAT = 1F;
    private static final Float UPDATED_LAT = 2F;

    private static final Float DEFAULT_LNG = 1F;
    private static final Float UPDATED_LNG = 2F;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/csos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CsoRepository csoRepository;

    @Mock
    private CsoRepository csoRepositoryMock;

    @Mock
    private CsoService csoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCsoMockMvc;

    private Cso cso;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cso createEntity(EntityManager em) {
        Cso cso = new Cso()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .organisation(DEFAULT_ORGANISATION)
            .cell(DEFAULT_CELL)
            .city(DEFAULT_CITY)
            .email(DEFAULT_EMAIL)
            .registrationCertificate(DEFAULT_REGISTRATION_CERTIFICATE)
            .organisationProfile(DEFAULT_ORGANISATION_PROFILE)
            .managementStructure(DEFAULT_MANAGEMENT_STRUCTURE)
            .strategicPlan(DEFAULT_STRATEGIC_PLAN)
            .resourceMobilisationPlan(DEFAULT_RESOURCE_MOBILISATION_PLAN)
            .comments(DEFAULT_COMMENTS)
            .enquiries(DEFAULT_ENQUIRIES)
            .lat(DEFAULT_LAT)
            .lng(DEFAULT_LNG)
            .createdDate(DEFAULT_CREATED_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
        return cso;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cso createUpdatedEntity(EntityManager em) {
        Cso cso = new Cso()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .organisation(UPDATED_ORGANISATION)
            .cell(UPDATED_CELL)
            .city(UPDATED_CITY)
            .email(UPDATED_EMAIL)
            .registrationCertificate(UPDATED_REGISTRATION_CERTIFICATE)
            .organisationProfile(UPDATED_ORGANISATION_PROFILE)
            .managementStructure(UPDATED_MANAGEMENT_STRUCTURE)
            .strategicPlan(UPDATED_STRATEGIC_PLAN)
            .resourceMobilisationPlan(UPDATED_RESOURCE_MOBILISATION_PLAN)
            .comments(UPDATED_COMMENTS)
            .enquiries(UPDATED_ENQUIRIES)
            .lat(UPDATED_LAT)
            .lng(UPDATED_LNG)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
        return cso;
    }

    @BeforeEach
    public void initTest() {
        cso = createEntity(em);
    }

    @Test
    @Transactional
    void createCso() throws Exception {
        int databaseSizeBeforeCreate = csoRepository.findAll().size();
        // Create the Cso
        restCsoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cso)))
            .andExpect(status().isCreated());

        // Validate the Cso in the database
        List<Cso> csoList = csoRepository.findAll();
        assertThat(csoList).hasSize(databaseSizeBeforeCreate + 1);
        Cso testCso = csoList.get(csoList.size() - 1);
        assertThat(testCso.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testCso.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testCso.getOrganisation()).isEqualTo(DEFAULT_ORGANISATION);
        assertThat(testCso.getCell()).isEqualTo(DEFAULT_CELL);
        assertThat(testCso.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testCso.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCso.getRegistrationCertificate()).isEqualTo(DEFAULT_REGISTRATION_CERTIFICATE);
        assertThat(testCso.getOrganisationProfile()).isEqualTo(DEFAULT_ORGANISATION_PROFILE);
        assertThat(testCso.getManagementStructure()).isEqualTo(DEFAULT_MANAGEMENT_STRUCTURE);
        assertThat(testCso.getStrategicPlan()).isEqualTo(DEFAULT_STRATEGIC_PLAN);
        assertThat(testCso.getResourceMobilisationPlan()).isEqualTo(DEFAULT_RESOURCE_MOBILISATION_PLAN);
        assertThat(testCso.getComments()).isEqualTo(DEFAULT_COMMENTS);
        assertThat(testCso.getEnquiries()).isEqualTo(DEFAULT_ENQUIRIES);
        assertThat(testCso.getLat()).isEqualTo(DEFAULT_LAT);
        assertThat(testCso.getLng()).isEqualTo(DEFAULT_LNG);
        assertThat(testCso.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testCso.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createCsoWithExistingId() throws Exception {
        // Create the Cso with an existing ID
        cso.setId(1L);

        int databaseSizeBeforeCreate = csoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCsoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cso)))
            .andExpect(status().isBadRequest());

        // Validate the Cso in the database
        List<Cso> csoList = csoRepository.findAll();
        assertThat(csoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = csoRepository.findAll().size();
        // set the field null
        cso.setFirstName(null);

        // Create the Cso, which fails.

        restCsoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cso)))
            .andExpect(status().isBadRequest());

        List<Cso> csoList = csoRepository.findAll();
        assertThat(csoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = csoRepository.findAll().size();
        // set the field null
        cso.setLastName(null);

        // Create the Cso, which fails.

        restCsoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cso)))
            .andExpect(status().isBadRequest());

        List<Cso> csoList = csoRepository.findAll();
        assertThat(csoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOrganisationIsRequired() throws Exception {
        int databaseSizeBeforeTest = csoRepository.findAll().size();
        // set the field null
        cso.setOrganisation(null);

        // Create the Cso, which fails.

        restCsoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cso)))
            .andExpect(status().isBadRequest());

        List<Cso> csoList = csoRepository.findAll();
        assertThat(csoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCellIsRequired() throws Exception {
        int databaseSizeBeforeTest = csoRepository.findAll().size();
        // set the field null
        cso.setCell(null);

        // Create the Cso, which fails.

        restCsoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cso)))
            .andExpect(status().isBadRequest());

        List<Cso> csoList = csoRepository.findAll();
        assertThat(csoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = csoRepository.findAll().size();
        // set the field null
        cso.setCity(null);

        // Create the Cso, which fails.

        restCsoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cso)))
            .andExpect(status().isBadRequest());

        List<Cso> csoList = csoRepository.findAll();
        assertThat(csoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = csoRepository.findAll().size();
        // set the field null
        cso.setEmail(null);

        // Create the Cso, which fails.

        restCsoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cso)))
            .andExpect(status().isBadRequest());

        List<Cso> csoList = csoRepository.findAll();
        assertThat(csoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLatIsRequired() throws Exception {
        int databaseSizeBeforeTest = csoRepository.findAll().size();
        // set the field null
        cso.setLat(null);

        // Create the Cso, which fails.

        restCsoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cso)))
            .andExpect(status().isBadRequest());

        List<Cso> csoList = csoRepository.findAll();
        assertThat(csoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLngIsRequired() throws Exception {
        int databaseSizeBeforeTest = csoRepository.findAll().size();
        // set the field null
        cso.setLng(null);

        // Create the Cso, which fails.

        restCsoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cso)))
            .andExpect(status().isBadRequest());

        List<Cso> csoList = csoRepository.findAll();
        assertThat(csoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCsos() throws Exception {
        // Initialize the database
        csoRepository.saveAndFlush(cso);

        // Get all the csoList
        restCsoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cso.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].organisation").value(hasItem(DEFAULT_ORGANISATION)))
            .andExpect(jsonPath("$.[*].cell").value(hasItem(DEFAULT_CELL)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].registrationCertificate").value(hasItem(DEFAULT_REGISTRATION_CERTIFICATE.booleanValue())))
            .andExpect(jsonPath("$.[*].organisationProfile").value(hasItem(DEFAULT_ORGANISATION_PROFILE.booleanValue())))
            .andExpect(jsonPath("$.[*].managementStructure").value(hasItem(DEFAULT_MANAGEMENT_STRUCTURE.booleanValue())))
            .andExpect(jsonPath("$.[*].strategicPlan").value(hasItem(DEFAULT_STRATEGIC_PLAN.booleanValue())))
            .andExpect(jsonPath("$.[*].resourceMobilisationPlan").value(hasItem(DEFAULT_RESOURCE_MOBILISATION_PLAN.booleanValue())))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)))
            .andExpect(jsonPath("$.[*].enquiries").value(hasItem(DEFAULT_ENQUIRIES)))
            .andExpect(jsonPath("$.[*].lat").value(hasItem(DEFAULT_LAT.doubleValue())))
            .andExpect(jsonPath("$.[*].lng").value(hasItem(DEFAULT_LNG.doubleValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCsosWithEagerRelationshipsIsEnabled() throws Exception {
        when(csoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCsoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(csoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCsosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(csoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCsoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(csoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCso() throws Exception {
        // Initialize the database
        csoRepository.saveAndFlush(cso);

        // Get the cso
        restCsoMockMvc
            .perform(get(ENTITY_API_URL_ID, cso.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cso.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.organisation").value(DEFAULT_ORGANISATION))
            .andExpect(jsonPath("$.cell").value(DEFAULT_CELL))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.registrationCertificate").value(DEFAULT_REGISTRATION_CERTIFICATE.booleanValue()))
            .andExpect(jsonPath("$.organisationProfile").value(DEFAULT_ORGANISATION_PROFILE.booleanValue()))
            .andExpect(jsonPath("$.managementStructure").value(DEFAULT_MANAGEMENT_STRUCTURE.booleanValue()))
            .andExpect(jsonPath("$.strategicPlan").value(DEFAULT_STRATEGIC_PLAN.booleanValue()))
            .andExpect(jsonPath("$.resourceMobilisationPlan").value(DEFAULT_RESOURCE_MOBILISATION_PLAN.booleanValue()))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS))
            .andExpect(jsonPath("$.enquiries").value(DEFAULT_ENQUIRIES))
            .andExpect(jsonPath("$.lat").value(DEFAULT_LAT.doubleValue()))
            .andExpect(jsonPath("$.lng").value(DEFAULT_LNG.doubleValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCso() throws Exception {
        // Get the cso
        restCsoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCso() throws Exception {
        // Initialize the database
        csoRepository.saveAndFlush(cso);

        int databaseSizeBeforeUpdate = csoRepository.findAll().size();

        // Update the cso
        Cso updatedCso = csoRepository.findById(cso.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCso are not directly saved in db
        em.detach(updatedCso);
        updatedCso
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .organisation(UPDATED_ORGANISATION)
            .cell(UPDATED_CELL)
            .city(UPDATED_CITY)
            .email(UPDATED_EMAIL)
            .registrationCertificate(UPDATED_REGISTRATION_CERTIFICATE)
            .organisationProfile(UPDATED_ORGANISATION_PROFILE)
            .managementStructure(UPDATED_MANAGEMENT_STRUCTURE)
            .strategicPlan(UPDATED_STRATEGIC_PLAN)
            .resourceMobilisationPlan(UPDATED_RESOURCE_MOBILISATION_PLAN)
            .comments(UPDATED_COMMENTS)
            .enquiries(UPDATED_ENQUIRIES)
            .lat(UPDATED_LAT)
            .lng(UPDATED_LNG)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restCsoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCso.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCso))
            )
            .andExpect(status().isOk());

        // Validate the Cso in the database
        List<Cso> csoList = csoRepository.findAll();
        assertThat(csoList).hasSize(databaseSizeBeforeUpdate);
        Cso testCso = csoList.get(csoList.size() - 1);
        assertThat(testCso.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testCso.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testCso.getOrganisation()).isEqualTo(UPDATED_ORGANISATION);
        assertThat(testCso.getCell()).isEqualTo(UPDATED_CELL);
        assertThat(testCso.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testCso.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCso.getRegistrationCertificate()).isEqualTo(UPDATED_REGISTRATION_CERTIFICATE);
        assertThat(testCso.getOrganisationProfile()).isEqualTo(UPDATED_ORGANISATION_PROFILE);
        assertThat(testCso.getManagementStructure()).isEqualTo(UPDATED_MANAGEMENT_STRUCTURE);
        assertThat(testCso.getStrategicPlan()).isEqualTo(UPDATED_STRATEGIC_PLAN);
        assertThat(testCso.getResourceMobilisationPlan()).isEqualTo(UPDATED_RESOURCE_MOBILISATION_PLAN);
        assertThat(testCso.getComments()).isEqualTo(UPDATED_COMMENTS);
        assertThat(testCso.getEnquiries()).isEqualTo(UPDATED_ENQUIRIES);
        assertThat(testCso.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testCso.getLng()).isEqualTo(UPDATED_LNG);
        assertThat(testCso.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testCso.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingCso() throws Exception {
        int databaseSizeBeforeUpdate = csoRepository.findAll().size();
        cso.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCsoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cso.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cso))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cso in the database
        List<Cso> csoList = csoRepository.findAll();
        assertThat(csoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCso() throws Exception {
        int databaseSizeBeforeUpdate = csoRepository.findAll().size();
        cso.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCsoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cso))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cso in the database
        List<Cso> csoList = csoRepository.findAll();
        assertThat(csoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCso() throws Exception {
        int databaseSizeBeforeUpdate = csoRepository.findAll().size();
        cso.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCsoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cso)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cso in the database
        List<Cso> csoList = csoRepository.findAll();
        assertThat(csoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCsoWithPatch() throws Exception {
        // Initialize the database
        csoRepository.saveAndFlush(cso);

        int databaseSizeBeforeUpdate = csoRepository.findAll().size();

        // Update the cso using partial update
        Cso partialUpdatedCso = new Cso();
        partialUpdatedCso.setId(cso.getId());

        partialUpdatedCso
            .firstName(UPDATED_FIRST_NAME)
            .organisation(UPDATED_ORGANISATION)
            .city(UPDATED_CITY)
            .organisationProfile(UPDATED_ORGANISATION_PROFILE)
            .resourceMobilisationPlan(UPDATED_RESOURCE_MOBILISATION_PLAN)
            .comments(UPDATED_COMMENTS)
            .enquiries(UPDATED_ENQUIRIES)
            .lat(UPDATED_LAT)
            .createdDate(UPDATED_CREATED_DATE);

        restCsoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCso.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCso))
            )
            .andExpect(status().isOk());

        // Validate the Cso in the database
        List<Cso> csoList = csoRepository.findAll();
        assertThat(csoList).hasSize(databaseSizeBeforeUpdate);
        Cso testCso = csoList.get(csoList.size() - 1);
        assertThat(testCso.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testCso.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testCso.getOrganisation()).isEqualTo(UPDATED_ORGANISATION);
        assertThat(testCso.getCell()).isEqualTo(DEFAULT_CELL);
        assertThat(testCso.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testCso.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCso.getRegistrationCertificate()).isEqualTo(DEFAULT_REGISTRATION_CERTIFICATE);
        assertThat(testCso.getOrganisationProfile()).isEqualTo(UPDATED_ORGANISATION_PROFILE);
        assertThat(testCso.getManagementStructure()).isEqualTo(DEFAULT_MANAGEMENT_STRUCTURE);
        assertThat(testCso.getStrategicPlan()).isEqualTo(DEFAULT_STRATEGIC_PLAN);
        assertThat(testCso.getResourceMobilisationPlan()).isEqualTo(UPDATED_RESOURCE_MOBILISATION_PLAN);
        assertThat(testCso.getComments()).isEqualTo(UPDATED_COMMENTS);
        assertThat(testCso.getEnquiries()).isEqualTo(UPDATED_ENQUIRIES);
        assertThat(testCso.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testCso.getLng()).isEqualTo(DEFAULT_LNG);
        assertThat(testCso.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testCso.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateCsoWithPatch() throws Exception {
        // Initialize the database
        csoRepository.saveAndFlush(cso);

        int databaseSizeBeforeUpdate = csoRepository.findAll().size();

        // Update the cso using partial update
        Cso partialUpdatedCso = new Cso();
        partialUpdatedCso.setId(cso.getId());

        partialUpdatedCso
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .organisation(UPDATED_ORGANISATION)
            .cell(UPDATED_CELL)
            .city(UPDATED_CITY)
            .email(UPDATED_EMAIL)
            .registrationCertificate(UPDATED_REGISTRATION_CERTIFICATE)
            .organisationProfile(UPDATED_ORGANISATION_PROFILE)
            .managementStructure(UPDATED_MANAGEMENT_STRUCTURE)
            .strategicPlan(UPDATED_STRATEGIC_PLAN)
            .resourceMobilisationPlan(UPDATED_RESOURCE_MOBILISATION_PLAN)
            .comments(UPDATED_COMMENTS)
            .enquiries(UPDATED_ENQUIRIES)
            .lat(UPDATED_LAT)
            .lng(UPDATED_LNG)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restCsoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCso.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCso))
            )
            .andExpect(status().isOk());

        // Validate the Cso in the database
        List<Cso> csoList = csoRepository.findAll();
        assertThat(csoList).hasSize(databaseSizeBeforeUpdate);
        Cso testCso = csoList.get(csoList.size() - 1);
        assertThat(testCso.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testCso.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testCso.getOrganisation()).isEqualTo(UPDATED_ORGANISATION);
        assertThat(testCso.getCell()).isEqualTo(UPDATED_CELL);
        assertThat(testCso.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testCso.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCso.getRegistrationCertificate()).isEqualTo(UPDATED_REGISTRATION_CERTIFICATE);
        assertThat(testCso.getOrganisationProfile()).isEqualTo(UPDATED_ORGANISATION_PROFILE);
        assertThat(testCso.getManagementStructure()).isEqualTo(UPDATED_MANAGEMENT_STRUCTURE);
        assertThat(testCso.getStrategicPlan()).isEqualTo(UPDATED_STRATEGIC_PLAN);
        assertThat(testCso.getResourceMobilisationPlan()).isEqualTo(UPDATED_RESOURCE_MOBILISATION_PLAN);
        assertThat(testCso.getComments()).isEqualTo(UPDATED_COMMENTS);
        assertThat(testCso.getEnquiries()).isEqualTo(UPDATED_ENQUIRIES);
        assertThat(testCso.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testCso.getLng()).isEqualTo(UPDATED_LNG);
        assertThat(testCso.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testCso.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingCso() throws Exception {
        int databaseSizeBeforeUpdate = csoRepository.findAll().size();
        cso.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCsoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cso.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cso))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cso in the database
        List<Cso> csoList = csoRepository.findAll();
        assertThat(csoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCso() throws Exception {
        int databaseSizeBeforeUpdate = csoRepository.findAll().size();
        cso.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCsoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cso))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cso in the database
        List<Cso> csoList = csoRepository.findAll();
        assertThat(csoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCso() throws Exception {
        int databaseSizeBeforeUpdate = csoRepository.findAll().size();
        cso.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCsoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cso)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cso in the database
        List<Cso> csoList = csoRepository.findAll();
        assertThat(csoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCso() throws Exception {
        // Initialize the database
        csoRepository.saveAndFlush(cso);

        int databaseSizeBeforeDelete = csoRepository.findAll().size();

        // Delete the cso
        restCsoMockMvc.perform(delete(ENTITY_API_URL_ID, cso.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cso> csoList = csoRepository.findAll();
        assertThat(csoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
