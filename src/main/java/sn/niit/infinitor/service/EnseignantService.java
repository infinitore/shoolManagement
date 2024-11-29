package sn.niit.infinitor.service;

import java.util.Optional;
import sn.niit.infinitor.service.dto.EnseignantDTO;

/**
 * Service Interface for managing {@link sn.niit.infinitor.domain.Enseignant}.
 */
public interface EnseignantService {
    /**
     * Save a enseignant.
     *
     * @param enseignantDTO the entity to save.
     * @return the persisted entity.
     */
    EnseignantDTO save(EnseignantDTO enseignantDTO);

    /**
     * Updates a enseignant.
     *
     * @param enseignantDTO the entity to update.
     * @return the persisted entity.
     */
    EnseignantDTO update(EnseignantDTO enseignantDTO);

    /**
     * Partially updates a enseignant.
     *
     * @param enseignantDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EnseignantDTO> partialUpdate(EnseignantDTO enseignantDTO);

    /**
     * Get the "id" enseignant.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EnseignantDTO> findOne(Long id);

    /**
     * Delete the "id" enseignant.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
