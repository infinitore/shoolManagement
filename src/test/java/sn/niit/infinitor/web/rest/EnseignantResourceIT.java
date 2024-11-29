package sn.niit.infinitor.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.niit.infinitor.domain.EnseignantAsserts.*;
import static sn.niit.infinitor.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sn.niit.infinitor.IntegrationTest;
import sn.niit.infinitor.domain.Enseignant;
import sn.niit.infinitor.repository.EnseignantRepository;
import sn.niit.infinitor.service.dto.EnseignantDTO;
import sn.niit.infinitor.service.mapper.EnseignantMapper;

/**
 * Integration tests for the {@link EnseignantResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EnseignantResourceIT {

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_EMBAUCHE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_EMBAUCHE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_EMBAUCHE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_SPECIALITE = "AAAAAAAAAA";
    private static final String UPDATED_SPECIALITE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/enseignants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EnseignantRepository enseignantRepository;

    @Autowired
    private EnseignantMapper enseignantMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEnseignantMockMvc;

    private Enseignant enseignant;

    private Enseignant insertedEnseignant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enseignant createEntity(EntityManager em) {
        Enseignant enseignant = new Enseignant()
            .prenom(DEFAULT_PRENOM)
            .nom(DEFAULT_NOM)
            .dateEmbauche(DEFAULT_DATE_EMBAUCHE)
            .specialite(DEFAULT_SPECIALITE)
            .email(DEFAULT_EMAIL)
            .telephone(DEFAULT_TELEPHONE);
        return enseignant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enseignant createUpdatedEntity(EntityManager em) {
        Enseignant enseignant = new Enseignant()
            .prenom(UPDATED_PRENOM)
            .nom(UPDATED_NOM)
            .dateEmbauche(UPDATED_DATE_EMBAUCHE)
            .specialite(UPDATED_SPECIALITE)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE);
        return enseignant;
    }

    @BeforeEach
    public void initTest() {
        enseignant = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedEnseignant != null) {
            enseignantRepository.delete(insertedEnseignant);
            insertedEnseignant = null;
        }
    }

    @Test
    @Transactional
    void createEnseignant() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Enseignant
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(enseignant);
        var returnedEnseignantDTO = om.readValue(
            restEnseignantMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enseignantDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EnseignantDTO.class
        );

        // Validate the Enseignant in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEnseignant = enseignantMapper.toEntity(returnedEnseignantDTO);
        assertEnseignantUpdatableFieldsEquals(returnedEnseignant, getPersistedEnseignant(returnedEnseignant));

        insertedEnseignant = returnedEnseignant;
    }

    @Test
    @Transactional
    void createEnseignantWithExistingId() throws Exception {
        // Create the Enseignant with an existing ID
        enseignant.setId(1L);
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(enseignant);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEnseignantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enseignantDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Enseignant in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPrenomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        enseignant.setPrenom(null);

        // Create the Enseignant, which fails.
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(enseignant);

        restEnseignantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enseignantDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        enseignant.setNom(null);

        // Create the Enseignant, which fails.
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(enseignant);

        restEnseignantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enseignantDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateEmbaucheIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        enseignant.setDateEmbauche(null);

        // Create the Enseignant, which fails.
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(enseignant);

        restEnseignantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enseignantDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSpecialiteIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        enseignant.setSpecialite(null);

        // Create the Enseignant, which fails.
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(enseignant);

        restEnseignantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enseignantDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEnseignants() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList
        restEnseignantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(enseignant.getId().intValue())))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].dateEmbauche").value(hasItem(DEFAULT_DATE_EMBAUCHE.toString())))
            .andExpect(jsonPath("$.[*].specialite").value(hasItem(DEFAULT_SPECIALITE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)));
    }

    @Test
    @Transactional
    void getEnseignant() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get the enseignant
        restEnseignantMockMvc
            .perform(get(ENTITY_API_URL_ID, enseignant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(enseignant.getId().intValue()))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.dateEmbauche").value(DEFAULT_DATE_EMBAUCHE.toString()))
            .andExpect(jsonPath("$.specialite").value(DEFAULT_SPECIALITE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE));
    }

    @Test
    @Transactional
    void getEnseignantsByIdFiltering() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        Long id = enseignant.getId();

        defaultEnseignantFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEnseignantFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEnseignantFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEnseignantsByPrenomIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where prenom equals to
        defaultEnseignantFiltering("prenom.equals=" + DEFAULT_PRENOM, "prenom.equals=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllEnseignantsByPrenomIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where prenom in
        defaultEnseignantFiltering("prenom.in=" + DEFAULT_PRENOM + "," + UPDATED_PRENOM, "prenom.in=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllEnseignantsByPrenomIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where prenom is not null
        defaultEnseignantFiltering("prenom.specified=true", "prenom.specified=false");
    }

    @Test
    @Transactional
    void getAllEnseignantsByPrenomContainsSomething() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where prenom contains
        defaultEnseignantFiltering("prenom.contains=" + DEFAULT_PRENOM, "prenom.contains=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllEnseignantsByPrenomNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where prenom does not contain
        defaultEnseignantFiltering("prenom.doesNotContain=" + UPDATED_PRENOM, "prenom.doesNotContain=" + DEFAULT_PRENOM);
    }

    @Test
    @Transactional
    void getAllEnseignantsByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where nom equals to
        defaultEnseignantFiltering("nom.equals=" + DEFAULT_NOM, "nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllEnseignantsByNomIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where nom in
        defaultEnseignantFiltering("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM, "nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllEnseignantsByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where nom is not null
        defaultEnseignantFiltering("nom.specified=true", "nom.specified=false");
    }

    @Test
    @Transactional
    void getAllEnseignantsByNomContainsSomething() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where nom contains
        defaultEnseignantFiltering("nom.contains=" + DEFAULT_NOM, "nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllEnseignantsByNomNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where nom does not contain
        defaultEnseignantFiltering("nom.doesNotContain=" + UPDATED_NOM, "nom.doesNotContain=" + DEFAULT_NOM);
    }

    @Test
    @Transactional
    void getAllEnseignantsByDateEmbaucheIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where dateEmbauche equals to
        defaultEnseignantFiltering("dateEmbauche.equals=" + DEFAULT_DATE_EMBAUCHE, "dateEmbauche.equals=" + UPDATED_DATE_EMBAUCHE);
    }

    @Test
    @Transactional
    void getAllEnseignantsByDateEmbaucheIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where dateEmbauche in
        defaultEnseignantFiltering(
            "dateEmbauche.in=" + DEFAULT_DATE_EMBAUCHE + "," + UPDATED_DATE_EMBAUCHE,
            "dateEmbauche.in=" + UPDATED_DATE_EMBAUCHE
        );
    }

    @Test
    @Transactional
    void getAllEnseignantsByDateEmbaucheIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where dateEmbauche is not null
        defaultEnseignantFiltering("dateEmbauche.specified=true", "dateEmbauche.specified=false");
    }

    @Test
    @Transactional
    void getAllEnseignantsByDateEmbaucheIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where dateEmbauche is greater than or equal to
        defaultEnseignantFiltering(
            "dateEmbauche.greaterThanOrEqual=" + DEFAULT_DATE_EMBAUCHE,
            "dateEmbauche.greaterThanOrEqual=" + UPDATED_DATE_EMBAUCHE
        );
    }

    @Test
    @Transactional
    void getAllEnseignantsByDateEmbaucheIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where dateEmbauche is less than or equal to
        defaultEnseignantFiltering(
            "dateEmbauche.lessThanOrEqual=" + DEFAULT_DATE_EMBAUCHE,
            "dateEmbauche.lessThanOrEqual=" + SMALLER_DATE_EMBAUCHE
        );
    }

    @Test
    @Transactional
    void getAllEnseignantsByDateEmbaucheIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where dateEmbauche is less than
        defaultEnseignantFiltering("dateEmbauche.lessThan=" + UPDATED_DATE_EMBAUCHE, "dateEmbauche.lessThan=" + DEFAULT_DATE_EMBAUCHE);
    }

    @Test
    @Transactional
    void getAllEnseignantsByDateEmbaucheIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where dateEmbauche is greater than
        defaultEnseignantFiltering(
            "dateEmbauche.greaterThan=" + SMALLER_DATE_EMBAUCHE,
            "dateEmbauche.greaterThan=" + DEFAULT_DATE_EMBAUCHE
        );
    }

    @Test
    @Transactional
    void getAllEnseignantsBySpecialiteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where specialite equals to
        defaultEnseignantFiltering("specialite.equals=" + DEFAULT_SPECIALITE, "specialite.equals=" + UPDATED_SPECIALITE);
    }

    @Test
    @Transactional
    void getAllEnseignantsBySpecialiteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where specialite in
        defaultEnseignantFiltering("specialite.in=" + DEFAULT_SPECIALITE + "," + UPDATED_SPECIALITE, "specialite.in=" + UPDATED_SPECIALITE);
    }

    @Test
    @Transactional
    void getAllEnseignantsBySpecialiteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where specialite is not null
        defaultEnseignantFiltering("specialite.specified=true", "specialite.specified=false");
    }

    @Test
    @Transactional
    void getAllEnseignantsBySpecialiteContainsSomething() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where specialite contains
        defaultEnseignantFiltering("specialite.contains=" + DEFAULT_SPECIALITE, "specialite.contains=" + UPDATED_SPECIALITE);
    }

    @Test
    @Transactional
    void getAllEnseignantsBySpecialiteNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where specialite does not contain
        defaultEnseignantFiltering("specialite.doesNotContain=" + UPDATED_SPECIALITE, "specialite.doesNotContain=" + DEFAULT_SPECIALITE);
    }

    @Test
    @Transactional
    void getAllEnseignantsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where email equals to
        defaultEnseignantFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEnseignantsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where email in
        defaultEnseignantFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEnseignantsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where email is not null
        defaultEnseignantFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllEnseignantsByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where email contains
        defaultEnseignantFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEnseignantsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where email does not contain
        defaultEnseignantFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllEnseignantsByTelephoneIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where telephone equals to
        defaultEnseignantFiltering("telephone.equals=" + DEFAULT_TELEPHONE, "telephone.equals=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllEnseignantsByTelephoneIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where telephone in
        defaultEnseignantFiltering("telephone.in=" + DEFAULT_TELEPHONE + "," + UPDATED_TELEPHONE, "telephone.in=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllEnseignantsByTelephoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where telephone is not null
        defaultEnseignantFiltering("telephone.specified=true", "telephone.specified=false");
    }

    @Test
    @Transactional
    void getAllEnseignantsByTelephoneContainsSomething() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where telephone contains
        defaultEnseignantFiltering("telephone.contains=" + DEFAULT_TELEPHONE, "telephone.contains=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllEnseignantsByTelephoneNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where telephone does not contain
        defaultEnseignantFiltering("telephone.doesNotContain=" + UPDATED_TELEPHONE, "telephone.doesNotContain=" + DEFAULT_TELEPHONE);
    }

    private void defaultEnseignantFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEnseignantShouldBeFound(shouldBeFound);
        defaultEnseignantShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEnseignantShouldBeFound(String filter) throws Exception {
        restEnseignantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(enseignant.getId().intValue())))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].dateEmbauche").value(hasItem(DEFAULT_DATE_EMBAUCHE.toString())))
            .andExpect(jsonPath("$.[*].specialite").value(hasItem(DEFAULT_SPECIALITE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)));

        // Check, that the count call also returns 1
        restEnseignantMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEnseignantShouldNotBeFound(String filter) throws Exception {
        restEnseignantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEnseignantMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEnseignant() throws Exception {
        // Get the enseignant
        restEnseignantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEnseignant() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the enseignant
        Enseignant updatedEnseignant = enseignantRepository.findById(enseignant.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEnseignant are not directly saved in db
        em.detach(updatedEnseignant);
        updatedEnseignant
            .prenom(UPDATED_PRENOM)
            .nom(UPDATED_NOM)
            .dateEmbauche(UPDATED_DATE_EMBAUCHE)
            .specialite(UPDATED_SPECIALITE)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE);
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(updatedEnseignant);

        restEnseignantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, enseignantDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(enseignantDTO))
            )
            .andExpect(status().isOk());

        // Validate the Enseignant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEnseignantToMatchAllProperties(updatedEnseignant);
    }

    @Test
    @Transactional
    void putNonExistingEnseignant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enseignant.setId(longCount.incrementAndGet());

        // Create the Enseignant
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(enseignant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnseignantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, enseignantDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(enseignantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enseignant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEnseignant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enseignant.setId(longCount.incrementAndGet());

        // Create the Enseignant
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(enseignant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnseignantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(enseignantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enseignant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEnseignant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enseignant.setId(longCount.incrementAndGet());

        // Create the Enseignant
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(enseignant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnseignantMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enseignantDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Enseignant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEnseignantWithPatch() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the enseignant using partial update
        Enseignant partialUpdatedEnseignant = new Enseignant();
        partialUpdatedEnseignant.setId(enseignant.getId());

        partialUpdatedEnseignant.nom(UPDATED_NOM).specialite(UPDATED_SPECIALITE).email(UPDATED_EMAIL);

        restEnseignantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnseignant.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEnseignant))
            )
            .andExpect(status().isOk());

        // Validate the Enseignant in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEnseignantUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEnseignant, enseignant),
            getPersistedEnseignant(enseignant)
        );
    }

    @Test
    @Transactional
    void fullUpdateEnseignantWithPatch() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the enseignant using partial update
        Enseignant partialUpdatedEnseignant = new Enseignant();
        partialUpdatedEnseignant.setId(enseignant.getId());

        partialUpdatedEnseignant
            .prenom(UPDATED_PRENOM)
            .nom(UPDATED_NOM)
            .dateEmbauche(UPDATED_DATE_EMBAUCHE)
            .specialite(UPDATED_SPECIALITE)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE);

        restEnseignantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnseignant.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEnseignant))
            )
            .andExpect(status().isOk());

        // Validate the Enseignant in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEnseignantUpdatableFieldsEquals(partialUpdatedEnseignant, getPersistedEnseignant(partialUpdatedEnseignant));
    }

    @Test
    @Transactional
    void patchNonExistingEnseignant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enseignant.setId(longCount.incrementAndGet());

        // Create the Enseignant
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(enseignant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnseignantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, enseignantDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(enseignantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enseignant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEnseignant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enseignant.setId(longCount.incrementAndGet());

        // Create the Enseignant
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(enseignant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnseignantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(enseignantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enseignant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEnseignant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enseignant.setId(longCount.incrementAndGet());

        // Create the Enseignant
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(enseignant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnseignantMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(enseignantDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Enseignant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEnseignant() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the enseignant
        restEnseignantMockMvc
            .perform(delete(ENTITY_API_URL_ID, enseignant.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return enseignantRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Enseignant getPersistedEnseignant(Enseignant enseignant) {
        return enseignantRepository.findById(enseignant.getId()).orElseThrow();
    }

    protected void assertPersistedEnseignantToMatchAllProperties(Enseignant expectedEnseignant) {
        assertEnseignantAllPropertiesEquals(expectedEnseignant, getPersistedEnseignant(expectedEnseignant));
    }

    protected void assertPersistedEnseignantToMatchUpdatableProperties(Enseignant expectedEnseignant) {
        assertEnseignantAllUpdatablePropertiesEquals(expectedEnseignant, getPersistedEnseignant(expectedEnseignant));
    }
}
