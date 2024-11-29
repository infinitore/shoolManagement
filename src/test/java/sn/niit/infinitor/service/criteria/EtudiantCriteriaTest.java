package sn.niit.infinitor.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EtudiantCriteriaTest {

    @Test
    void newEtudiantCriteriaHasAllFiltersNullTest() {
        var etudiantCriteria = new EtudiantCriteria();
        assertThat(etudiantCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void etudiantCriteriaFluentMethodsCreatesFiltersTest() {
        var etudiantCriteria = new EtudiantCriteria();

        setAllFilters(etudiantCriteria);

        assertThat(etudiantCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void etudiantCriteriaCopyCreatesNullFilterTest() {
        var etudiantCriteria = new EtudiantCriteria();
        var copy = etudiantCriteria.copy();

        assertThat(etudiantCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(etudiantCriteria)
        );
    }

    @Test
    void etudiantCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var etudiantCriteria = new EtudiantCriteria();
        setAllFilters(etudiantCriteria);

        var copy = etudiantCriteria.copy();

        assertThat(etudiantCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(etudiantCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var etudiantCriteria = new EtudiantCriteria();

        assertThat(etudiantCriteria).hasToString("EtudiantCriteria{}");
    }

    private static void setAllFilters(EtudiantCriteria etudiantCriteria) {
        etudiantCriteria.id();
        etudiantCriteria.prenom();
        etudiantCriteria.nom();
        etudiantCriteria.dateNaissance();
        etudiantCriteria.adresse();
        etudiantCriteria.email();
        etudiantCriteria.telephone();
        etudiantCriteria.coursId();
        etudiantCriteria.classeId();
        etudiantCriteria.distinct();
    }

    private static Condition<EtudiantCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getPrenom()) &&
                condition.apply(criteria.getNom()) &&
                condition.apply(criteria.getDateNaissance()) &&
                condition.apply(criteria.getAdresse()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getTelephone()) &&
                condition.apply(criteria.getCoursId()) &&
                condition.apply(criteria.getClasseId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<EtudiantCriteria> copyFiltersAre(EtudiantCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getPrenom(), copy.getPrenom()) &&
                condition.apply(criteria.getNom(), copy.getNom()) &&
                condition.apply(criteria.getDateNaissance(), copy.getDateNaissance()) &&
                condition.apply(criteria.getAdresse(), copy.getAdresse()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getTelephone(), copy.getTelephone()) &&
                condition.apply(criteria.getCoursId(), copy.getCoursId()) &&
                condition.apply(criteria.getClasseId(), copy.getClasseId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
