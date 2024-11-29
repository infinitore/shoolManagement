package sn.niit.infinitor.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.niit.infinitor.domain.Etudiant;

/**
 * Spring Data JPA repository for the Etudiant entity.
 *
 * When extending this class, extend EtudiantRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface EtudiantRepository
    extends EtudiantRepositoryWithBagRelationships, JpaRepository<Etudiant, Long>, JpaSpecificationExecutor<Etudiant> {
    default Optional<Etudiant> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Etudiant> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Etudiant> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select etudiant from Etudiant etudiant left join fetch etudiant.classe",
        countQuery = "select count(etudiant) from Etudiant etudiant"
    )
    Page<Etudiant> findAllWithToOneRelationships(Pageable pageable);

    @Query("select etudiant from Etudiant etudiant left join fetch etudiant.classe")
    List<Etudiant> findAllWithToOneRelationships();

    @Query("select etudiant from Etudiant etudiant left join fetch etudiant.classe where etudiant.id =:id")
    Optional<Etudiant> findOneWithToOneRelationships(@Param("id") Long id);
}
