package sn.niit.infinitor.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.niit.infinitor.domain.Cours;
import sn.niit.infinitor.repository.CoursRepository;
import sn.niit.infinitor.service.CoursService;
import sn.niit.infinitor.service.dto.CoursDTO;
import sn.niit.infinitor.service.mapper.CoursMapper;

/**
 * Service Implementation for managing {@link sn.niit.infinitor.domain.Cours}.
 */
@Service
@Transactional
public class CoursServiceImpl implements CoursService {

    private final Logger log = LoggerFactory.getLogger(CoursServiceImpl.class);

    private final CoursRepository coursRepository;

    private final CoursMapper coursMapper;

    public CoursServiceImpl(CoursRepository coursRepository, CoursMapper coursMapper) {
        this.coursRepository = coursRepository;
        this.coursMapper = coursMapper;
    }

    @Override
    public CoursDTO save(CoursDTO coursDTO) {
        log.debug("Request to save Cours : {}", coursDTO);
        Cours cours = coursMapper.toEntity(coursDTO);
        cours = coursRepository.save(cours);
        return coursMapper.toDto(cours);
    }

    @Override
    public CoursDTO update(CoursDTO coursDTO) {
        log.debug("Request to update Cours : {}", coursDTO);
        Cours cours = coursMapper.toEntity(coursDTO);
        cours = coursRepository.save(cours);
        return coursMapper.toDto(cours);
    }

    @Override
    public Optional<CoursDTO> partialUpdate(CoursDTO coursDTO) {
        log.debug("Request to partially update Cours : {}", coursDTO);

        return coursRepository
            .findById(coursDTO.getId())
            .map(existingCours -> {
                coursMapper.partialUpdate(existingCours, coursDTO);

                return existingCours;
            })
            .map(coursRepository::save)
            .map(coursMapper::toDto);
    }

    public Page<CoursDTO> findAllWithEagerRelationships(Pageable pageable) {
        return coursRepository.findAllWithEagerRelationships(pageable).map(coursMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CoursDTO> findOne(Long id) {
        log.debug("Request to get Cours : {}", id);
        return coursRepository.findOneWithEagerRelationships(id).map(coursMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Cours : {}", id);
        coursRepository.deleteById(id);
    }
}
