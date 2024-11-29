package sn.niit.infinitor.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import sn.niit.infinitor.domain.Classe;
import sn.niit.infinitor.domain.Cours;
import sn.niit.infinitor.domain.Enseignant;
import sn.niit.infinitor.domain.Etudiant;
import sn.niit.infinitor.service.dto.ClasseDTO;
import sn.niit.infinitor.service.dto.CoursDTO;
import sn.niit.infinitor.service.dto.EnseignantDTO;
import sn.niit.infinitor.service.dto.EtudiantDTO;

/**
 * Mapper for the entity {@link Cours} and its DTO {@link CoursDTO}.
 */
@Mapper(componentModel = "spring")
public interface CoursMapper extends EntityMapper<CoursDTO, Cours> {
    @Mapping(target = "classe", source = "classe", qualifiedByName = "classeNom")
    @Mapping(target = "enseignant", source = "enseignant", qualifiedByName = "enseignantPrenom")
    @Mapping(target = "etudiants", source = "etudiants", qualifiedByName = "etudiantPrenomSet")
    CoursDTO toDto(Cours s);

    @Mapping(target = "etudiants", ignore = true)
    @Mapping(target = "removeEtudiants", ignore = true)
    Cours toEntity(CoursDTO coursDTO);

    @Named("classeNom")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nom", source = "nom")
    ClasseDTO toDtoClasseNom(Classe classe);

    @Named("enseignantPrenom")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "prenom", source = "prenom")
    EnseignantDTO toDtoEnseignantPrenom(Enseignant enseignant);

    @Named("etudiantPrenom")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "prenom", source = "prenom")
    EtudiantDTO toDtoEtudiantPrenom(Etudiant etudiant);

    @Named("etudiantPrenomSet")
    default Set<EtudiantDTO> toDtoEtudiantPrenomSet(Set<Etudiant> etudiant) {
        return etudiant.stream().map(this::toDtoEtudiantPrenom).collect(Collectors.toSet());
    }
}
