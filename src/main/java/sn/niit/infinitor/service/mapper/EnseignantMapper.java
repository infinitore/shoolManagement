package sn.niit.infinitor.service.mapper;

import org.mapstruct.*;
import sn.niit.infinitor.domain.Enseignant;
import sn.niit.infinitor.service.dto.EnseignantDTO;

/**
 * Mapper for the entity {@link Enseignant} and its DTO {@link EnseignantDTO}.
 */
@Mapper(componentModel = "spring")
public interface EnseignantMapper extends EntityMapper<EnseignantDTO, Enseignant> {}
