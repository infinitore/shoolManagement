package sn.niit.infinitor.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.niit.infinitor.domain.ClasseTestSamples.*;
import static sn.niit.infinitor.domain.CoursTestSamples.*;
import static sn.niit.infinitor.domain.EnseignantTestSamples.*;
import static sn.niit.infinitor.domain.EtudiantTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.niit.infinitor.web.rest.TestUtil;

class CoursTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cours.class);
        Cours cours1 = getCoursSample1();
        Cours cours2 = new Cours();
        assertThat(cours1).isNotEqualTo(cours2);

        cours2.setId(cours1.getId());
        assertThat(cours1).isEqualTo(cours2);

        cours2 = getCoursSample2();
        assertThat(cours1).isNotEqualTo(cours2);
    }

    @Test
    void classeTest() {
        Cours cours = getCoursRandomSampleGenerator();
        Classe classeBack = getClasseRandomSampleGenerator();

        cours.setClasse(classeBack);
        assertThat(cours.getClasse()).isEqualTo(classeBack);

        cours.classe(null);
        assertThat(cours.getClasse()).isNull();
    }

    @Test
    void enseignantTest() {
        Cours cours = getCoursRandomSampleGenerator();
        Enseignant enseignantBack = getEnseignantRandomSampleGenerator();

        cours.setEnseignant(enseignantBack);
        assertThat(cours.getEnseignant()).isEqualTo(enseignantBack);

        cours.enseignant(null);
        assertThat(cours.getEnseignant()).isNull();
    }

    @Test
    void etudiantsTest() {
        Cours cours = getCoursRandomSampleGenerator();
        Etudiant etudiantBack = getEtudiantRandomSampleGenerator();

        cours.addEtudiants(etudiantBack);
        assertThat(cours.getEtudiants()).containsOnly(etudiantBack);
        assertThat(etudiantBack.getCours()).containsOnly(cours);

        cours.removeEtudiants(etudiantBack);
        assertThat(cours.getEtudiants()).doesNotContain(etudiantBack);
        assertThat(etudiantBack.getCours()).doesNotContain(cours);

        cours.etudiants(new HashSet<>(Set.of(etudiantBack)));
        assertThat(cours.getEtudiants()).containsOnly(etudiantBack);
        assertThat(etudiantBack.getCours()).containsOnly(cours);

        cours.setEtudiants(new HashSet<>());
        assertThat(cours.getEtudiants()).doesNotContain(etudiantBack);
        assertThat(etudiantBack.getCours()).doesNotContain(cours);
    }
}
