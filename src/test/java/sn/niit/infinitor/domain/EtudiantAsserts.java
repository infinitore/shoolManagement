package sn.niit.infinitor.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class EtudiantAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEtudiantAllPropertiesEquals(Etudiant expected, Etudiant actual) {
        assertEtudiantAutoGeneratedPropertiesEquals(expected, actual);
        assertEtudiantAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEtudiantAllUpdatablePropertiesEquals(Etudiant expected, Etudiant actual) {
        assertEtudiantUpdatableFieldsEquals(expected, actual);
        assertEtudiantUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEtudiantAutoGeneratedPropertiesEquals(Etudiant expected, Etudiant actual) {
        assertThat(expected)
            .as("Verify Etudiant auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEtudiantUpdatableFieldsEquals(Etudiant expected, Etudiant actual) {
        assertThat(expected)
            .as("Verify Etudiant relevant properties")
            .satisfies(e -> assertThat(e.getPrenom()).as("check prenom").isEqualTo(actual.getPrenom()))
            .satisfies(e -> assertThat(e.getNom()).as("check nom").isEqualTo(actual.getNom()))
            .satisfies(e -> assertThat(e.getDateNaissance()).as("check dateNaissance").isEqualTo(actual.getDateNaissance()))
            .satisfies(e -> assertThat(e.getAdresse()).as("check adresse").isEqualTo(actual.getAdresse()))
            .satisfies(e -> assertThat(e.getEmail()).as("check email").isEqualTo(actual.getEmail()))
            .satisfies(e -> assertThat(e.getTelephone()).as("check telephone").isEqualTo(actual.getTelephone()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEtudiantUpdatableRelationshipsEquals(Etudiant expected, Etudiant actual) {
        assertThat(expected)
            .as("Verify Etudiant relationships")
            .satisfies(e -> assertThat(e.getCours()).as("check cours").isEqualTo(actual.getCours()))
            .satisfies(e -> assertThat(e.getClasse()).as("check classe").isEqualTo(actual.getClasse()));
    }
}
