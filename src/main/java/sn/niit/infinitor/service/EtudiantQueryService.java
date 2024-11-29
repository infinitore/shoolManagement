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
import sn.niit.infinitor.domain.Etudiant;
import sn.niit.infinitor.repository.EtudiantRepository;
import sn.niit.infinitor.service.criteria.EtudiantCriteria;
import sn.niit.infinitor.service.dto.EtudiantDTO;
import sn.niit.infinitor.service.mapper.EtudiantMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Etudiant} entities in the database.
 * The main input is a {@link EtudiantCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link EtudiantDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EtudiantQueryService extends QueryService<Etudiant> {

    private final Logger log = LoggerFactory.getLogger(EtudiantQueryService.class);

    private final EtudiantRepository etudiantRepository;

    private final EtudiantMapper etudiantMapper;

    public EtudiantQueryService(EtudiantRepository etudiantRepository, EtudiantMapper etudiantMapper) {
        this.etudiantRepository = etudiantRepository;
        this.etudiantMapper = etudiantMapper;
    }

    /**
     * Return a {@link Page} of {@link EtudiantDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EtudiantDTO> findByCriteria(EtudiantCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Etudiant> specification = createSpecification(criteria);
        return etudiantRepository.fetchBagRelationships(etudiantRepository.findAll(specification, page)).map(etudiantMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EtudiantCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Etudiant> specification = createSpecification(criteria);
        return etudiantRepository.count(specification);
    }

    /**
     * Function to convert {@link EtudiantCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Etudiant> createSpecification(EtudiantCriteria criteria) {
        Specification<Etudiant> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Etudiant_.id));
            }
            if (criteria.getPrenom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrenom(), Etudiant_.prenom));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), Etudiant_.nom));
            }
            if (criteria.getDateNaissance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateNaissance(), Etudiant_.dateNaissance));
            }
            if (criteria.getAdresse() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAdresse(), Etudiant_.adresse));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Etudiant_.email));
            }
            if (criteria.getTelephone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTelephone(), Etudiant_.telephone));
            }
            if (criteria.getCoursId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCoursId(), root -> root.join(Etudiant_.cours, JoinType.LEFT).get(Cours_.id))
                );
            }
            if (criteria.getClasseId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getClasseId(), root -> root.join(Etudiant_.classe, JoinType.LEFT).get(Classe_.id))
                );
            }
        }
        return specification;
    }
}
