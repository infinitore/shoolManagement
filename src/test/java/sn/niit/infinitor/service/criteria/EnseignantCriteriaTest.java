package sn.niit.infinitor.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EnseignantCriteriaTest {

    @Test
    void newEnseignantCriteriaHasAllFiltersNullTest() {
        var enseignantCriteria = new EnseignantCriteria();
        assertThat(enseignantCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void enseignantCriteriaFluentMethodsCreatesFiltersTest() {
        var enseignantCriteria = new EnseignantCriteria();

        setAllFilters(enseignantCriteria);

        assertThat(enseignantCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void enseignantCriteriaCopyCreatesNullFilterTest() {
        var enseignantCriteria = new EnseignantCriteria();
        var copy = enseignantCriteria.copy();

        assertThat(enseignantCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(enseignantCriteria)
        );
    }

    @Test
    void enseignantCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var enseignantCriteria = new EnseignantCriteria();
        setAllFilters(enseignantCriteria);

        var copy = enseignantCriteria.copy();

        assertThat(enseignantCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(enseignantCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var enseignantCriteria = new EnseignantCriteria();

        assertThat(enseignantCriteria).hasToString("EnseignantCriteria{}");
    }

    private static void setAllFilters(EnseignantCriteria enseignantCriteria) {
        enseignantCriteria.id();
        enseignantCriteria.prenom();
        enseignantCriteria.nom();
        enseignantCriteria.dateEmbauche();
        enseignantCriteria.specialite();
        enseignantCriteria.email();
        enseignantCriteria.telephone();
        enseignantCriteria.coursId();
        enseignantCriteria.distinct();
    }

    private static Condition<EnseignantCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getPrenom()) &&
                condition.apply(criteria.getNom()) &&
                condition.apply(criteria.getDateEmbauche()) &&
                condition.apply(criteria.getSpecialite()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getTelephone()) &&
                condition.apply(criteria.getCoursId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<EnseignantCriteria> copyFiltersAre(EnseignantCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getPrenom(), copy.getPrenom()) &&
                condition.apply(criteria.getNom(), copy.getNom()) &&
                condition.apply(criteria.getDateEmbauche(), copy.getDateEmbauche()) &&
                condition.apply(criteria.getSpecialite(), copy.getSpecialite()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getTelephone(), copy.getTelephone()) &&
                condition.apply(criteria.getCoursId(), copy.getCoursId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
