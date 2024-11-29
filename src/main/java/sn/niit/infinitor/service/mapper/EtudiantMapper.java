package sn.niit.infinitor.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import sn.niit.infinitor.domain.Classe;
import sn.niit.infinitor.domain.Cours;
import sn.niit.infinitor.domain.Etudiant;
import sn.niit.infinitor.service.dto.ClasseDTO;
import sn.niit.infinitor.service.dto.CoursDTO;
import sn.niit.infinitor.service.dto.EtudiantDTO;

/**
 * Mapper for the entity {@link Etudiant} and its DTO {@link EtudiantDTO}.
 */
@Mapper(componentModel = "spring")
public interface EtudiantMapper extends EntityMapper<EtudiantDTO, Etudiant> {
    @Mapping(target = "cours", source = "cours", qualifiedByName = "coursNomSet")
    @Mapping(target = "classe", source = "classe", qualifiedByName = "classeNom")
    EtudiantDTO toDto(Etudiant s);

    @Mapping(target = "removeCours", ignore = true)
    Etudiant toEntity(EtudiantDTO etudiantDTO);

    @Named("coursNom")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nom", source = "nom")
    CoursDTO toDtoCoursNom(Cours cours);

    @Named("coursNomSet")
    default Set<CoursDTO> toDtoCoursNomSet(Set<Cours> cours) {
        return cours.stream().map(this::toDtoCoursNom).collect(Collectors.toSet());
    }

    @Named("classeNom")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nom", source = "nom")
    ClasseDTO toDtoClasseNom(Classe classe);
}
