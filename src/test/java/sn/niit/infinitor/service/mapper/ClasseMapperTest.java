package sn.niit.infinitor.service.mapper;

import static sn.niit.infinitor.domain.ClasseAsserts.*;
import static sn.niit.infinitor.domain.ClasseTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClasseMapperTest {

    private ClasseMapper classeMapper;

    @BeforeEach
    void setUp() {
        classeMapper = new ClasseMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getClasseSample1();
        var actual = classeMapper.toEntity(classeMapper.toDto(expected));
        assertClasseAllPropertiesEquals(expected, actual);
    }
}
