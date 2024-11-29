package sn.niit.infinitor.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.niit.infinitor.domain.ClasseAsserts.*;
import static sn.niit.infinitor.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
import sn.niit.infinitor.domain.Classe;
import sn.niit.infinitor.repository.ClasseRepository;
import sn.niit.infinitor.service.dto.ClasseDTO;
import sn.niit.infinitor.service.mapper.ClasseMapper;

/**
 * Integration tests for the {@link ClasseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClasseResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_NIVEAU = "AAAAAAAAAA";
    private static final String UPDATED_NIVEAU = "BBBBBBBBBB";

    private static final String DEFAULT_ANNEE_SCOLAIRE = "AAAAAAAAAA";
    private static final String UPDATED_ANNEE_SCOLAIRE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/classes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ClasseRepository classeRepository;

    @Autowired
    private ClasseMapper classeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClasseMockMvc;

    private Classe classe;

    private Classe insertedClasse;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Classe createEntity(EntityManager em) {
        Classe classe = new Classe().nom(DEFAULT_NOM).niveau(DEFAULT_NIVEAU).anneeScolaire(DEFAULT_ANNEE_SCOLAIRE);
        return classe;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Classe createUpdatedEntity(EntityManager em) {
        Classe classe = new Classe().nom(UPDATED_NOM).niveau(UPDATED_NIVEAU).anneeScolaire(UPDATED_ANNEE_SCOLAIRE);
        return classe;
    }

    @BeforeEach
    public void initTest() {
        classe = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedClasse != null) {
            classeRepository.delete(insertedClasse);
            insertedClasse = null;
        }
    }

    @Test
    @Transactional
    void createClasse() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Classe
        ClasseDTO classeDTO = classeMapper.toDto(classe);
        var returnedClasseDTO = om.readValue(
            restClasseMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(classeDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ClasseDTO.class
        );

        // Validate the Classe in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedClasse = classeMapper.toEntity(returnedClasseDTO);
        assertClasseUpdatableFieldsEquals(returnedClasse, getPersistedClasse(returnedClasse));

        insertedClasse = returnedClasse;
    }

    @Test
    @Transactional
    void createClasseWithExistingId() throws Exception {
        // Create the Classe with an existing ID
        classe.setId(1L);
        ClasseDTO classeDTO = classeMapper.toDto(classe);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClasseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(classeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Classe in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        classe.setNom(null);

        // Create the Classe, which fails.
        ClasseDTO classeDTO = classeMapper.toDto(classe);

        restClasseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(classeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNiveauIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        classe.setNiveau(null);

        // Create the Classe, which fails.
        ClasseDTO classeDTO = classeMapper.toDto(classe);

        restClasseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(classeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAnneeScolaireIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        classe.setAnneeScolaire(null);

        // Create the Classe, which fails.
        ClasseDTO classeDTO = classeMapper.toDto(classe);

        restClasseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(classeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClasses() throws Exception {
        // Initialize the database
        insertedClasse = classeRepository.saveAndFlush(classe);

        // Get all the classeList
        restClasseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classe.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].niveau").value(hasItem(DEFAULT_NIVEAU)))
            .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE)));
    }

    @Test
    @Transactional
    void getClasse() throws Exception {
        // Initialize the database
        insertedClasse = classeRepository.saveAndFlush(classe);

        // Get the classe
        restClasseMockMvc
            .perform(get(ENTITY_API_URL_ID, classe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(classe.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.niveau").value(DEFAULT_NIVEAU))
            .andExpect(jsonPath("$.anneeScolaire").value(DEFAULT_ANNEE_SCOLAIRE));
    }

    @Test
    @Transactional
    void getClassesByIdFiltering() throws Exception {
        // Initialize the database
        insertedClasse = classeRepository.saveAndFlush(classe);

        Long id = classe.getId();

        defaultClasseFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultClasseFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultClasseFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllClassesByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClasse = classeRepository.saveAndFlush(classe);

        // Get all the classeList where nom equals to
        defaultClasseFiltering("nom.equals=" + DEFAULT_NOM, "nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllClassesByNomIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClasse = classeRepository.saveAndFlush(classe);

        // Get all the classeList where nom in
        defaultClasseFiltering("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM, "nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllClassesByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClasse = classeRepository.saveAndFlush(classe);

        // Get all the classeList where nom is not null
        defaultClasseFiltering("nom.specified=true", "nom.specified=false");
    }

    @Test
    @Transactional
    void getAllClassesByNomContainsSomething() throws Exception {
        // Initialize the database
        insertedClasse = classeRepository.saveAndFlush(classe);

        // Get all the classeList where nom contains
        defaultClasseFiltering("nom.contains=" + DEFAULT_NOM, "nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllClassesByNomNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClasse = classeRepository.saveAndFlush(classe);

        // Get all the classeList where nom does not contain
        defaultClasseFiltering("nom.doesNotContain=" + UPDATED_NOM, "nom.doesNotContain=" + DEFAULT_NOM);
    }

    @Test
    @Transactional
    void getAllClassesByNiveauIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClasse = classeRepository.saveAndFlush(classe);

        // Get all the classeList where niveau equals to
        defaultClasseFiltering("niveau.equals=" + DEFAULT_NIVEAU, "niveau.equals=" + UPDATED_NIVEAU);
    }

    @Test
    @Transactional
    void getAllClassesByNiveauIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClasse = classeRepository.saveAndFlush(classe);

        // Get all the classeList where niveau in
        defaultClasseFiltering("niveau.in=" + DEFAULT_NIVEAU + "," + UPDATED_NIVEAU, "niveau.in=" + UPDATED_NIVEAU);
    }

    @Test
    @Transactional
    void getAllClassesByNiveauIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClasse = classeRepository.saveAndFlush(classe);

        // Get all the classeList where niveau is not null
        defaultClasseFiltering("niveau.specified=true", "niveau.specified=false");
    }

    @Test
    @Transactional
    void getAllClassesByNiveauContainsSomething() throws Exception {
        // Initialize the database
        insertedClasse = classeRepository.saveAndFlush(classe);

        // Get all the classeList where niveau contains
        defaultClasseFiltering("niveau.contains=" + DEFAULT_NIVEAU, "niveau.contains=" + UPDATED_NIVEAU);
    }

    @Test
    @Transactional
    void getAllClassesByNiveauNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClasse = classeRepository.saveAndFlush(classe);

        // Get all the classeList where niveau does not contain
        defaultClasseFiltering("niveau.doesNotContain=" + UPDATED_NIVEAU, "niveau.doesNotContain=" + DEFAULT_NIVEAU);
    }

    @Test
    @Transactional
    void getAllClassesByAnneeScolaireIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClasse = classeRepository.saveAndFlush(classe);

        // Get all the classeList where anneeScolaire equals to
        defaultClasseFiltering("anneeScolaire.equals=" + DEFAULT_ANNEE_SCOLAIRE, "anneeScolaire.equals=" + UPDATED_ANNEE_SCOLAIRE);
    }

    @Test
    @Transactional
    void getAllClassesByAnneeScolaireIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClasse = classeRepository.saveAndFlush(classe);

        // Get all the classeList where anneeScolaire in
        defaultClasseFiltering(
            "anneeScolaire.in=" + DEFAULT_ANNEE_SCOLAIRE + "," + UPDATED_ANNEE_SCOLAIRE,
            "anneeScolaire.in=" + UPDATED_ANNEE_SCOLAIRE
        );
    }

    @Test
    @Transactional
    void getAllClassesByAnneeScolaireIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClasse = classeRepository.saveAndFlush(classe);

        // Get all the classeList where anneeScolaire is not null
        defaultClasseFiltering("anneeScolaire.specified=true", "anneeScolaire.specified=false");
    }

    @Test
    @Transactional
    void getAllClassesByAnneeScolaireContainsSomething() throws Exception {
        // Initialize the database
        insertedClasse = classeRepository.saveAndFlush(classe);

        // Get all the classeList where anneeScolaire contains
        defaultClasseFiltering("anneeScolaire.contains=" + DEFAULT_ANNEE_SCOLAIRE, "anneeScolaire.contains=" + UPDATED_ANNEE_SCOLAIRE);
    }

    @Test
    @Transactional
    void getAllClassesByAnneeScolaireNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClasse = classeRepository.saveAndFlush(classe);

        // Get all the classeList where anneeScolaire does not contain
        defaultClasseFiltering(
            "anneeScolaire.doesNotContain=" + UPDATED_ANNEE_SCOLAIRE,
            "anneeScolaire.doesNotContain=" + DEFAULT_ANNEE_SCOLAIRE
        );
    }

    private void defaultClasseFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultClasseShouldBeFound(shouldBeFound);
        defaultClasseShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClasseShouldBeFound(String filter) throws Exception {
        restClasseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classe.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].niveau").value(hasItem(DEFAULT_NIVEAU)))
            .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE)));

        // Check, that the count call also returns 1
        restClasseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClasseShouldNotBeFound(String filter) throws Exception {
        restClasseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClasseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingClasse() throws Exception {
        // Get the classe
        restClasseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingClasse() throws Exception {
        // Initialize the database
        insertedClasse = classeRepository.saveAndFlush(classe);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the classe
        Classe updatedClasse = classeRepository.findById(classe.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedClasse are not directly saved in db
        em.detach(updatedClasse);
        updatedClasse.nom(UPDATED_NOM).niveau(UPDATED_NIVEAU).anneeScolaire(UPDATED_ANNEE_SCOLAIRE);
        ClasseDTO classeDTO = classeMapper.toDto(updatedClasse);

        restClasseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, classeDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(classeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Classe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedClasseToMatchAllProperties(updatedClasse);
    }

    @Test
    @Transactional
    void putNonExistingClasse() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        classe.setId(longCount.incrementAndGet());

        // Create the Classe
        ClasseDTO classeDTO = classeMapper.toDto(classe);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClasseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, classeDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(classeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Classe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClasse() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        classe.setId(longCount.incrementAndGet());

        // Create the Classe
        ClasseDTO classeDTO = classeMapper.toDto(classe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClasseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(classeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Classe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClasse() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        classe.setId(longCount.incrementAndGet());

        // Create the Classe
        ClasseDTO classeDTO = classeMapper.toDto(classe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClasseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(classeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Classe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClasseWithPatch() throws Exception {
        // Initialize the database
        insertedClasse = classeRepository.saveAndFlush(classe);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the classe using partial update
        Classe partialUpdatedClasse = new Classe();
        partialUpdatedClasse.setId(classe.getId());

        restClasseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClasse.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClasse))
            )
            .andExpect(status().isOk());

        // Validate the Classe in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClasseUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedClasse, classe), getPersistedClasse(classe));
    }

    @Test
    @Transactional
    void fullUpdateClasseWithPatch() throws Exception {
        // Initialize the database
        insertedClasse = classeRepository.saveAndFlush(classe);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the classe using partial update
        Classe partialUpdatedClasse = new Classe();
        partialUpdatedClasse.setId(classe.getId());

        partialUpdatedClasse.nom(UPDATED_NOM).niveau(UPDATED_NIVEAU).anneeScolaire(UPDATED_ANNEE_SCOLAIRE);

        restClasseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClasse.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClasse))
            )
            .andExpect(status().isOk());

        // Validate the Classe in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClasseUpdatableFieldsEquals(partialUpdatedClasse, getPersistedClasse(partialUpdatedClasse));
    }

    @Test
    @Transactional
    void patchNonExistingClasse() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        classe.setId(longCount.incrementAndGet());

        // Create the Classe
        ClasseDTO classeDTO = classeMapper.toDto(classe);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClasseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, classeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(classeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Classe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClasse() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        classe.setId(longCount.incrementAndGet());

        // Create the Classe
        ClasseDTO classeDTO = classeMapper.toDto(classe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClasseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(classeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Classe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClasse() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        classe.setId(longCount.incrementAndGet());

        // Create the Classe
        ClasseDTO classeDTO = classeMapper.toDto(classe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClasseMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(classeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Classe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClasse() throws Exception {
        // Initialize the database
        insertedClasse = classeRepository.saveAndFlush(classe);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the classe
        restClasseMockMvc
            .perform(delete(ENTITY_API_URL_ID, classe.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return classeRepository.count();
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

    protected Classe getPersistedClasse(Classe classe) {
        return classeRepository.findById(classe.getId()).orElseThrow();
    }

    protected void assertPersistedClasseToMatchAllProperties(Classe expectedClasse) {
        assertClasseAllPropertiesEquals(expectedClasse, getPersistedClasse(expectedClasse));
    }

    protected void assertPersistedClasseToMatchUpdatableProperties(Classe expectedClasse) {
        assertClasseAllUpdatablePropertiesEquals(expectedClasse, getPersistedClasse(expectedClasse));
    }
}
