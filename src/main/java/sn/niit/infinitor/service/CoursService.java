package sn.niit.infinitor.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.niit.infinitor.service.dto.CoursDTO;

/**
 * Service Interface for managing {@link sn.niit.infinitor.domain.Cours}.
 */
public interface CoursService {
    /**
     * Save a cours.
     *
     * @param coursDTO the entity to save.
     * @return the persisted entity.
     */
    CoursDTO save(CoursDTO coursDTO);

    /**
     * Updates a cours.
     *
     * @param coursDTO the entity to update.
     * @return the persisted entity.
     */
    CoursDTO update(CoursDTO coursDTO);

    /**
     * Partially updates a cours.
     *
     * @param coursDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CoursDTO> partialUpdate(CoursDTO coursDTO);

    /**
     * Get all the cours with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CoursDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" cours.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CoursDTO> findOne(Long id);

    /**
     * Delete the "id" cours.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
