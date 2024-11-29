package sn.niit.infinitor.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import sn.niit.infinitor.domain.Etudiant;

public interface EtudiantRepositoryWithBagRelationships {
    Optional<Etudiant> fetchBagRelationships(Optional<Etudiant> etudiant);

    List<Etudiant> fetchBagRelationships(List<Etudiant> etudiants);

    Page<Etudiant> fetchBagRelationships(Page<Etudiant> etudiants);
}
