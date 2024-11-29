package sn.niit.infinitor.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.niit.infinitor.service.dto.EtudiantDTO;

/**
 * Service Interface for managing {@link sn.niit.infinitor.domain.Etudiant}.
 */
public interface EtudiantService {
    /**
     * Save a etudiant.
     *
     * @param etudiantDTO the entity to save.
     * @return the persisted entity.
     */
    EtudiantDTO save(EtudiantDTO etudiantDTO);

    /**
     * Updates a etudiant.
     *
     * @param etudiantDTO the entity to update.
     * @return the persisted entity.
     */
    EtudiantDTO update(EtudiantDTO etudiantDTO);

    /**
     * Partially updates a etudiant.
     *
     * @param etudiantDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EtudiantDTO> partialUpdate(EtudiantDTO etudiantDTO);

    /**
     * Get all the etudiants with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EtudiantDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" etudiant.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EtudiantDTO> findOne(Long id);

    /**
     * Delete the "id" etudiant.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
