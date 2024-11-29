package sn.niit.infinitor.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.niit.infinitor.domain.CoursTestSamples.*;
import static sn.niit.infinitor.domain.EnseignantTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.niit.infinitor.web.rest.TestUtil;

class EnseignantTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Enseignant.class);
        Enseignant enseignant1 = getEnseignantSample1();
        Enseignant enseignant2 = new Enseignant();
        assertThat(enseignant1).isNotEqualTo(enseignant2);

        enseignant2.setId(enseignant1.getId());
        assertThat(enseignant1).isEqualTo(enseignant2);

        enseignant2 = getEnseignantSample2();
        assertThat(enseignant1).isNotEqualTo(enseignant2);
    }

    @Test
    void coursTest() {
        Enseignant enseignant = getEnseignantRandomSampleGenerator();
        Cours coursBack = getCoursRandomSampleGenerator();

        enseignant.addCours(coursBack);
        assertThat(enseignant.getCours()).containsOnly(coursBack);
        assertThat(coursBack.getEnseignant()).isEqualTo(enseignant);

        enseignant.removeCours(coursBack);
        assertThat(enseignant.getCours()).doesNotContain(coursBack);
        assertThat(coursBack.getEnseignant()).isNull();

        enseignant.cours(new HashSet<>(Set.of(coursBack)));
        assertThat(enseignant.getCours()).containsOnly(coursBack);
        assertThat(coursBack.getEnseignant()).isEqualTo(enseignant);

        enseignant.setCours(new HashSet<>());
        assertThat(enseignant.getCours()).doesNotContain(coursBack);
        assertThat(coursBack.getEnseignant()).isNull();
    }
}
