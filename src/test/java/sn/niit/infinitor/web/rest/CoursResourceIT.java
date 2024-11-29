package sn.niit.infinitor.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.niit.infinitor.domain.CoursAsserts.*;
import static sn.niit.infinitor.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
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
import sn.niit.infinitor.IntegrationTest;
import sn.niit.infinitor.domain.Classe;
import sn.niit.infinitor.domain.Cours;
import sn.niit.infinitor.domain.Enseignant;
import sn.niit.infinitor.domain.Etudiant;
import sn.niit.infinitor.repository.CoursRepository;
import sn.niit.infinitor.service.CoursService;
import sn.niit.infinitor.service.dto.CoursDTO;
import sn.niit.infinitor.service.mapper.CoursMapper;

/**
 * Integration tests for the {@link CoursResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CoursResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_NOMBRE_HEURES = 1;
    private static final Integer UPDATED_NOMBRE_HEURES = 2;
    private static final Integer SMALLER_NOMBRE_HEURES = 1 - 1;

    private static final String ENTITY_API_URL = "/api/cours";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CoursRepository coursRepository;

    @Mock
    private CoursRepository coursRepositoryMock;

    @Autowired
    private CoursMapper coursMapper;

    @Mock
    private CoursService coursServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCoursMockMvc;

    private Cours cours;

    private Cours insertedCours;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cours createEntity(EntityManager em) {
        Cours cours = new Cours().nom(DEFAULT_NOM).description(DEFAULT_DESCRIPTION).nombreHeures(DEFAULT_NOMBRE_HEURES);
        return cours;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cours createUpdatedEntity(EntityManager em) {
        Cours cours = new Cours().nom(UPDATED_NOM).description(UPDATED_DESCRIPTION).nombreHeures(UPDATED_NOMBRE_HEURES);
        return cours;
    }

    @BeforeEach
    public void initTest() {
        cours = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedCours != null) {
            coursRepository.delete(insertedCours);
            insertedCours = null;
        }
    }

    @Test
    @Transactional
    void createCours() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Cours
        CoursDTO coursDTO = coursMapper.toDto(cours);
        var returnedCoursDTO = om.readValue(
            restCoursMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coursDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CoursDTO.class
        );

        // Validate the Cours in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCours = coursMapper.toEntity(returnedCoursDTO);
        assertCoursUpdatableFieldsEquals(returnedCours, getPersistedCours(returnedCours));

        insertedCours = returnedCours;
    }

    @Test
    @Transactional
    void createCoursWithExistingId() throws Exception {
        // Create the Cours with an existing ID
        cours.setId(1L);
        CoursDTO coursDTO = coursMapper.toDto(cours);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCoursMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coursDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cours in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cours.setNom(null);

        // Create the Cours, which fails.
        CoursDTO coursDTO = coursMapper.toDto(cours);

        restCoursMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coursDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCours() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList
        restCoursMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cours.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].nombreHeures").value(hasItem(DEFAULT_NOMBRE_HEURES)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCoursWithEagerRelationshipsIsEnabled() throws Exception {
        when(coursServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCoursMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(coursServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCoursWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(coursServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCoursMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(coursRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCours() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get the cours
        restCoursMockMvc
            .perform(get(ENTITY_API_URL_ID, cours.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cours.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.nombreHeures").value(DEFAULT_NOMBRE_HEURES));
    }

    @Test
    @Transactional
    void getCoursByIdFiltering() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        Long id = cours.getId();

        defaultCoursFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCoursFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCoursFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCoursByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where nom equals to
        defaultCoursFiltering("nom.equals=" + DEFAULT_NOM, "nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllCoursByNomIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where nom in
        defaultCoursFiltering("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM, "nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllCoursByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where nom is not null
        defaultCoursFiltering("nom.specified=true", "nom.specified=false");
    }

    @Test
    @Transactional
    void getAllCoursByNomContainsSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where nom contains
        defaultCoursFiltering("nom.contains=" + DEFAULT_NOM, "nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllCoursByNomNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where nom does not contain
        defaultCoursFiltering("nom.doesNotContain=" + UPDATED_NOM, "nom.doesNotContain=" + DEFAULT_NOM);
    }

    @Test
    @Transactional
    void getAllCoursByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where description equals to
        defaultCoursFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCoursByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where description in
        defaultCoursFiltering("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION, "description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCoursByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where description is not null
        defaultCoursFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllCoursByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where description contains
        defaultCoursFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCoursByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where description does not contain
        defaultCoursFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCoursByNombreHeuresIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where nombreHeures equals to
        defaultCoursFiltering("nombreHeures.equals=" + DEFAULT_NOMBRE_HEURES, "nombreHeures.equals=" + UPDATED_NOMBRE_HEURES);
    }

    @Test
    @Transactional
    void getAllCoursByNombreHeuresIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where nombreHeures in
        defaultCoursFiltering(
            "nombreHeures.in=" + DEFAULT_NOMBRE_HEURES + "," + UPDATED_NOMBRE_HEURES,
            "nombreHeures.in=" + UPDATED_NOMBRE_HEURES
        );
    }

    @Test
    @Transactional
    void getAllCoursByNombreHeuresIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where nombreHeures is not null
        defaultCoursFiltering("nombreHeures.specified=true", "nombreHeures.specified=false");
    }

    @Test
    @Transactional
    void getAllCoursByNombreHeuresIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where nombreHeures is greater than or equal to
        defaultCoursFiltering(
            "nombreHeures.greaterThanOrEqual=" + DEFAULT_NOMBRE_HEURES,
            "nombreHeures.greaterThanOrEqual=" + UPDATED_NOMBRE_HEURES
        );
    }

    @Test
    @Transactional
    void getAllCoursByNombreHeuresIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where nombreHeures is less than or equal to
        defaultCoursFiltering(
            "nombreHeures.lessThanOrEqual=" + DEFAULT_NOMBRE_HEURES,
            "nombreHeures.lessThanOrEqual=" + SMALLER_NOMBRE_HEURES
        );
    }

    @Test
    @Transactional
    void getAllCoursByNombreHeuresIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where nombreHeures is less than
        defaultCoursFiltering("nombreHeures.lessThan=" + UPDATED_NOMBRE_HEURES, "nombreHeures.lessThan=" + DEFAULT_NOMBRE_HEURES);
    }

    @Test
    @Transactional
    void getAllCoursByNombreHeuresIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where nombreHeures is greater than
        defaultCoursFiltering("nombreHeures.greaterThan=" + SMALLER_NOMBRE_HEURES, "nombreHeures.greaterThan=" + DEFAULT_NOMBRE_HEURES);
    }

    @Test
    @Transactional
    void getAllCoursByClasseIsEqualToSomething() throws Exception {
        Classe classe;
        if (TestUtil.findAll(em, Classe.class).isEmpty()) {
            coursRepository.saveAndFlush(cours);
            classe = ClasseResourceIT.createEntity(em);
        } else {
            classe = TestUtil.findAll(em, Classe.class).get(0);
        }
        em.persist(classe);
        em.flush();
        cours.setClasse(classe);
        coursRepository.saveAndFlush(cours);
        Long classeId = classe.getId();
        // Get all the coursList where classe equals to classeId
        defaultCoursShouldBeFound("classeId.equals=" + classeId);

        // Get all the coursList where classe equals to (classeId + 1)
        defaultCoursShouldNotBeFound("classeId.equals=" + (classeId + 1));
    }

    @Test
    @Transactional
    void getAllCoursByEnseignantIsEqualToSomething() throws Exception {
        Enseignant enseignant;
        if (TestUtil.findAll(em, Enseignant.class).isEmpty()) {
            coursRepository.saveAndFlush(cours);
            enseignant = EnseignantResourceIT.createEntity(em);
        } else {
            enseignant = TestUtil.findAll(em, Enseignant.class).get(0);
        }
        em.persist(enseignant);
        em.flush();
        cours.setEnseignant(enseignant);
        coursRepository.saveAndFlush(cours);
        Long enseignantId = enseignant.getId();
        // Get all the coursList where enseignant equals to enseignantId
        defaultCoursShouldBeFound("enseignantId.equals=" + enseignantId);

        // Get all the coursList where enseignant equals to (enseignantId + 1)
        defaultCoursShouldNotBeFound("enseignantId.equals=" + (enseignantId + 1));
    }

    @Test
    @Transactional
    void getAllCoursByEtudiantsIsEqualToSomething() throws Exception {
        Etudiant etudiants;
        if (TestUtil.findAll(em, Etudiant.class).isEmpty()) {
            coursRepository.saveAndFlush(cours);
            etudiants = EtudiantResourceIT.createEntity(em);
        } else {
            etudiants = TestUtil.findAll(em, Etudiant.class).get(0);
        }
        em.persist(etudiants);
        em.flush();
        cours.addEtudiants(etudiants);
        coursRepository.saveAndFlush(cours);
        Long etudiantsId = etudiants.getId();
        // Get all the coursList where etudiants equals to etudiantsId
        defaultCoursShouldBeFound("etudiantsId.equals=" + etudiantsId);

        // Get all the coursList where etudiants equals to (etudiantsId + 1)
        defaultCoursShouldNotBeFound("etudiantsId.equals=" + (etudiantsId + 1));
    }

    private void defaultCoursFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCoursShouldBeFound(shouldBeFound);
        defaultCoursShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCoursShouldBeFound(String filter) throws Exception {
        restCoursMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cours.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].nombreHeures").value(hasItem(DEFAULT_NOMBRE_HEURES)));

        // Check, that the count call also returns 1
        restCoursMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCoursShouldNotBeFound(String filter) throws Exception {
        restCoursMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCoursMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCours() throws Exception {
        // Get the cours
        restCoursMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCours() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cours
        Cours updatedCours = coursRepository.findById(cours.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCours are not directly saved in db
        em.detach(updatedCours);
        updatedCours.nom(UPDATED_NOM).description(UPDATED_DESCRIPTION).nombreHeures(UPDATED_NOMBRE_HEURES);
        CoursDTO coursDTO = coursMapper.toDto(updatedCours);

        restCoursMockMvc
            .perform(
                put(ENTITY_API_URL_ID, coursDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coursDTO))
            )
            .andExpect(status().isOk());

        // Validate the Cours in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCoursToMatchAllProperties(updatedCours);
    }

    @Test
    @Transactional
    void putNonExistingCours() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cours.setId(longCount.incrementAndGet());

        // Create the Cours
        CoursDTO coursDTO = coursMapper.toDto(cours);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCoursMockMvc
            .perform(
                put(ENTITY_API_URL_ID, coursDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coursDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cours in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCours() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cours.setId(longCount.incrementAndGet());

        // Create the Cours
        CoursDTO coursDTO = coursMapper.toDto(cours);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoursMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(coursDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cours in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCours() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cours.setId(longCount.incrementAndGet());

        // Create the Cours
        CoursDTO coursDTO = coursMapper.toDto(cours);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoursMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coursDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cours in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCoursWithPatch() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cours using partial update
        Cours partialUpdatedCours = new Cours();
        partialUpdatedCours.setId(cours.getId());

        restCoursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCours.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCours))
            )
            .andExpect(status().isOk());

        // Validate the Cours in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCoursUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCours, cours), getPersistedCours(cours));
    }

    @Test
    @Transactional
    void fullUpdateCoursWithPatch() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cours using partial update
        Cours partialUpdatedCours = new Cours();
        partialUpdatedCours.setId(cours.getId());

        partialUpdatedCours.nom(UPDATED_NOM).description(UPDATED_DESCRIPTION).nombreHeures(UPDATED_NOMBRE_HEURES);

        restCoursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCours.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCours))
            )
            .andExpect(status().isOk());

        // Validate the Cours in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCoursUpdatableFieldsEquals(partialUpdatedCours, getPersistedCours(partialUpdatedCours));
    }

    @Test
    @Transactional
    void patchNonExistingCours() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cours.setId(longCount.incrementAndGet());

        // Create the Cours
        CoursDTO coursDTO = coursMapper.toDto(cours);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCoursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, coursDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(coursDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cours in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCours() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cours.setId(longCount.incrementAndGet());

        // Create the Cours
        CoursDTO coursDTO = coursMapper.toDto(cours);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(coursDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cours in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCours() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cours.setId(longCount.incrementAndGet());

        // Create the Cours
        CoursDTO coursDTO = coursMapper.toDto(cours);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoursMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(coursDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cours in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCours() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cours
        restCoursMockMvc
            .perform(delete(ENTITY_API_URL_ID, cours.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return coursRepository.count();
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

    protected Cours getPersistedCours(Cours cours) {
        return coursRepository.findById(cours.getId()).orElseThrow();
    }

    protected void assertPersistedCoursToMatchAllProperties(Cours expectedCours) {
        assertCoursAllPropertiesEquals(expectedCours, getPersistedCours(expectedCours));
    }

    protected void assertPersistedCoursToMatchUpdatableProperties(Cours expectedCours) {
        assertCoursAllUpdatablePropertiesEquals(expectedCours, getPersistedCours(expectedCours));
    }
}
