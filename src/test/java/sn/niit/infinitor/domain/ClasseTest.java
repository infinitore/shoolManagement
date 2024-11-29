package sn.niit.infinitor.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.niit.infinitor.domain.ClasseTestSamples.*;
import static sn.niit.infinitor.domain.EtudiantTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.niit.infinitor.web.rest.TestUtil;

class ClasseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Classe.class);
        Classe classe1 = getClasseSample1();
        Classe classe2 = new Classe();
        assertThat(classe1).isNotEqualTo(classe2);

        classe2.setId(classe1.getId());
        assertThat(classe1).isEqualTo(classe2);

        classe2 = getClasseSample2();
        assertThat(classe1).isNotEqualTo(classe2);
    }

    @Test
    void etudiantsTest() {
        Classe classe = getClasseRandomSampleGenerator();
        Etudiant etudiantBack = getEtudiantRandomSampleGenerator();

        classe.addEtudiants(etudiantBack);
        assertThat(classe.getEtudiants()).containsOnly(etudiantBack);
        assertThat(etudiantBack.getClasse()).isEqualTo(classe);

        classe.removeEtudiants(etudiantBack);
        assertThat(classe.getEtudiants()).doesNotContain(etudiantBack);
        assertThat(etudiantBack.getClasse()).isNull();

        classe.etudiants(new HashSet<>(Set.of(etudiantBack)));
        assertThat(classe.getEtudiants()).containsOnly(etudiantBack);
        assertThat(etudiantBack.getClasse()).isEqualTo(classe);

        classe.setEtudiants(new HashSet<>());
        assertThat(classe.getEtudiants()).doesNotContain(etudiantBack);
        assertThat(etudiantBack.getClasse()).isNull();
    }
}
