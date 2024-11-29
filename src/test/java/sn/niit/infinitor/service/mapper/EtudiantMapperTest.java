package sn.niit.infinitor.service.mapper;

import static sn.niit.infinitor.domain.EtudiantAsserts.*;
import static sn.niit.infinitor.domain.EtudiantTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EtudiantMapperTest {

    private EtudiantMapper etudiantMapper;

    @BeforeEach
    void setUp() {
        etudiantMapper = new EtudiantMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEtudiantSample1();
        var actual = etudiantMapper.toEntity(etudiantMapper.toDto(expected));
        assertEtudiantAllPropertiesEquals(expected, actual);
    }
}
