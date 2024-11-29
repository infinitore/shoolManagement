package sn.niit.infinitor.service.mapper;

import org.mapstruct.*;
import sn.niit.infinitor.domain.Classe;
import sn.niit.infinitor.service.dto.ClasseDTO;

/**
 * Mapper for the entity {@link Classe} and its DTO {@link ClasseDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClasseMapper extends EntityMapper<ClasseDTO, Classe> {}
