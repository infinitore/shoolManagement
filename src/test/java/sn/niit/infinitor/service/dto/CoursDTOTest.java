package sn.niit.infinitor.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.niit.infinitor.web.rest.TestUtil;

class CoursDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CoursDTO.class);
        CoursDTO coursDTO1 = new CoursDTO();
        coursDTO1.setId(1L);
        CoursDTO coursDTO2 = new CoursDTO();
        assertThat(coursDTO1).isNotEqualTo(coursDTO2);
        coursDTO2.setId(coursDTO1.getId());
        assertThat(coursDTO1).isEqualTo(coursDTO2);
        coursDTO2.setId(2L);
        assertThat(coursDTO1).isNotEqualTo(coursDTO2);
        coursDTO1.setId(null);
        assertThat(coursDTO1).isNotEqualTo(coursDTO2);
    }
}
