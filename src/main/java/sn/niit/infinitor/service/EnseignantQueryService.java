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
import sn.niit.infinitor.domain.Enseignant;
import sn.niit.infinitor.repository.EnseignantRepository;
import sn.niit.infinitor.service.criteria.EnseignantCriteria;
import sn.niit.infinitor.service.dto.EnseignantDTO;
import sn.niit.infinitor.service.mapper.EnseignantMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Enseignant} entities in the database.
 * The main input is a {@link EnseignantCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link EnseignantDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EnseignantQueryService extends QueryService<Enseignant> {

    private final Logger log = LoggerFactory.getLogger(EnseignantQueryService.class);

    private final EnseignantRepository enseignantRepository;

    private final EnseignantMapper enseignantMapper;

    public EnseignantQueryService(EnseignantRepository enseignantRepository, EnseignantMapper enseignantMapper) {
        this.enseignantRepository = enseignantRepository;
        this.enseignantMapper = enseignantMapper;
    }

    /**
     * Return a {@link Page} of {@link EnseignantDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EnseignantDTO> findByCriteria(EnseignantCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Enseignant> specification = createSpecification(criteria);
        return enseignantRepository.findAll(specification, page).map(enseignantMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EnseignantCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Enseignant> specification = createSpecification(criteria);
        return enseignantRepository.count(specification);
    }

    /**
     * Function to convert {@link EnseignantCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Enseignant> createSpecification(EnseignantCriteria criteria) {
        Specification<Enseignant> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Enseignant_.id));
            }
            if (criteria.getPrenom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrenom(), Enseignant_.prenom));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), Enseignant_.nom));
            }
            if (criteria.getDateEmbauche() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateEmbauche(), Enseignant_.dateEmbauche));
            }
            if (criteria.getSpecialite() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSpecialite(), Enseignant_.specialite));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Enseignant_.email));
            }
            if (criteria.getTelephone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTelephone(), Enseignant_.telephone));
            }
            if (criteria.getCoursId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCoursId(), root -> root.join(Enseignant_.cours, JoinType.LEFT).get(Cours_.id))
                );
            }
        }
        return specification;
    }
}
