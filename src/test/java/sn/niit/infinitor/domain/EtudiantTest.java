package sn.niit.infinitor.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.niit.infinitor.domain.ClasseTestSamples.*;
import static sn.niit.infinitor.domain.CoursTestSamples.*;
import static sn.niit.infinitor.domain.EtudiantTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.niit.infinitor.web.rest.TestUtil;

class EtudiantTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Etudiant.class);
        Etudiant etudiant1 = getEtudiantSample1();
        Etudiant etudiant2 = new Etudiant();
        assertThat(etudiant1).isNotEqualTo(etudiant2);

        etudiant2.setId(etudiant1.getId());
        assertThat(etudiant1).isEqualTo(etudiant2);

        etudiant2 = getEtudiantSample2();
        assertThat(etudiant1).isNotEqualTo(etudiant2);
    }

    @Test
    void coursTest() {
        Etudiant etudiant = getEtudiantRandomSampleGenerator();
        Cours coursBack = getCoursRandomSampleGenerator();

        etudiant.addCours(coursBack);
        assertThat(etudiant.getCours()).containsOnly(coursBack);

        etudiant.removeCours(coursBack);
        assertThat(etudiant.getCours()).doesNotContain(coursBack);

        etudiant.cours(new HashSet<>(Set.of(coursBack)));
        assertThat(etudiant.getCours()).containsOnly(coursBack);

        etudiant.setCours(new HashSet<>());
        assertThat(etudiant.getCours()).doesNotContain(coursBack);
    }

    @Test
    void classeTest() {
        Etudiant etudiant = getEtudiantRandomSampleGenerator();
        Classe classeBack = getClasseRandomSampleGenerator();

        etudiant.setClasse(classeBack);
        assertThat(etudiant.getClasse()).isEqualTo(classeBack);

        etudiant.classe(null);
        assertThat(etudiant.getClasse()).isNull();
    }
}
