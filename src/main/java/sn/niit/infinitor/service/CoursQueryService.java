package sn.niit.infinitor.service;

import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.niit.infinitor.domain.*; // for static metamodels
import sn.niit.infinitor.domain.Cours;
import sn.niit.infinitor.repository.CoursRepository;
import sn.niit.infinitor.service.criteria.CoursCriteria;
import sn.niit.infinitor.service.dto.CoursDTO;
import sn.niit.infinitor.service.mapper.CoursMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Cours} entities in the database.
 * The main input is a {@link CoursCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CoursDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CoursQueryService extends QueryService<Cours> {

    private final Logger log = LoggerFactory.getLogger(CoursQueryService.class);

    private final CoursRepository coursRepository;

    private final CoursMapper coursMapper;

    public CoursQueryService(CoursRepository coursRepository, CoursMapper coursMapper) {
        this.coursRepository = coursRepository;
        this.coursMapper = coursMapper;
    }

    /**
     * Return a {@link Page} of {@link CoursDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CoursDTO> findByCriteria(CoursCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Cours> specification = createSpecification(criteria);
        return coursRepository.findAll(specification, page).map(coursMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CoursCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Cours> specification = createSpecification(criteria);
        return coursRepository.count(specification);
    }

    /**
     * Function to convert {@link CoursCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Cours> createSpecification(CoursCriteria criteria) {
        Specification<Cours> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Cours_.id));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), Cours_.nom));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Cours_.description));
            }
            if (criteria.getNombreHeures() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNombreHeures(), Cours_.nombreHeures));
            }
            if (criteria.getClasseId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getClasseId(), root -> root.join(Cours_.classe, JoinType.LEFT).get(Classe_.id))
                );
            }
            if (criteria.getEnseignantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getEnseignantId(), root -> root.join(Cours_.enseignant, JoinType.LEFT).get(Enseignant_.id))
                );
            }
            if (criteria.getEtudiantsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getEtudiantsId(), root -> root.join(Cours_.etudiants, JoinType.LEFT).get(Etudiant_.id))
                );
            }
        }
        return specification;
    }
}
