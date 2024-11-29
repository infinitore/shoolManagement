package sn.niit.infinitor.service.mapper;

import static sn.niit.infinitor.domain.EnseignantAsserts.*;
import static sn.niit.infinitor.domain.EnseignantTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EnseignantMapperTest {

    private EnseignantMapper enseignantMapper;

    @BeforeEach
    void setUp() {
        enseignantMapper = new EnseignantMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEnseignantSample1();
        var actual = enseignantMapper.toEntity(enseignantMapper.toDto(expected));
        assertEnseignantAllPropertiesEquals(expected, actual);
    }
}
