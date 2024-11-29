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
import sn.niit.infinitor.domain.Classe;
import sn.niit.infinitor.repository.ClasseRepository;
import sn.niit.infinitor.service.criteria.ClasseCriteria;
import sn.niit.infinitor.service.dto.ClasseDTO;
import sn.niit.infinitor.service.mapper.ClasseMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Classe} entities in the database.
 * The main input is a {@link ClasseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ClasseDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ClasseQueryService extends QueryService<Classe> {

    private final Logger log = LoggerFactory.getLogger(ClasseQueryService.class);

    private final ClasseRepository classeRepository;

    private final ClasseMapper classeMapper;

    public ClasseQueryService(ClasseRepository classeRepository, ClasseMapper classeMapper) {
        this.classeRepository = classeRepository;
        this.classeMapper = classeMapper;
    }

    /**
     * Return a {@link Page} of {@link ClasseDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ClasseDTO> findByCriteria(ClasseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Classe> specification = createSpecification(criteria);
        return classeRepository.findAll(specification, page).map(classeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ClasseCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Classe> specification = createSpecification(criteria);
        return classeRepository.count(specification);
    }

    /**
     * Function to convert {@link ClasseCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Classe> createSpecification(ClasseCriteria criteria) {
        Specification<Classe> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Classe_.id));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), Classe_.nom));
            }
            if (criteria.getNiveau() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNiveau(), Classe_.niveau));
            }
            if (criteria.getAnneeScolaire() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAnneeScolaire(), Classe_.anneeScolaire));
            }
            if (criteria.getEtudiantsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getEtudiantsId(), root -> root.join(Classe_.etudiants, JoinType.LEFT).get(Etudiant_.id))
                );
            }
        }
        return specification;
    }
}
