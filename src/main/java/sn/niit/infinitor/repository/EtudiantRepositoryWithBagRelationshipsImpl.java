package sn.niit.infinitor.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import sn.niit.infinitor.domain.Etudiant;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class EtudiantRepositoryWithBagRelationshipsImpl implements EtudiantRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String ETUDIANTS_PARAMETER = "etudiants";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Etudiant> fetchBagRelationships(Optional<Etudiant> etudiant) {
        return etudiant.map(this::fetchCours);
    }

    @Override
    public Page<Etudiant> fetchBagRelationships(Page<Etudiant> etudiants) {
        return new PageImpl<>(fetchBagRelationships(etudiants.getContent()), etudiants.getPageable(), etudiants.getTotalElements());
    }

    @Override
    public List<Etudiant> fetchBagRelationships(List<Etudiant> etudiants) {
        return Optional.of(etudiants).map(this::fetchCours).orElse(Collections.emptyList());
    }

    Etudiant fetchCours(Etudiant result) {
        return entityManager
            .createQuery("select etudiant from Etudiant etudiant left join fetch etudiant.cours where etudiant.id = :id", Etudiant.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Etudiant> fetchCours(List<Etudiant> etudiants) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, etudiants.size()).forEach(index -> order.put(etudiants.get(index).getId(), index));
        List<Etudiant> result = entityManager
            .createQuery(
                "select etudiant from Etudiant etudiant left join fetch etudiant.cours where etudiant in :etudiants",
                Etudiant.class
            )
            .setParameter(ETUDIANTS_PARAMETER, etudiants)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
