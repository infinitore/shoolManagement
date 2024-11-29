package sn.niit.infinitor.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CoursCriteriaTest {

    @Test
    void newCoursCriteriaHasAllFiltersNullTest() {
        var coursCriteria = new CoursCriteria();
        assertThat(coursCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void coursCriteriaFluentMethodsCreatesFiltersTest() {
        var coursCriteria = new CoursCriteria();

        setAllFilters(coursCriteria);

        assertThat(coursCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void coursCriteriaCopyCreatesNullFilterTest() {
        var coursCriteria = new CoursCriteria();
        var copy = coursCriteria.copy();

        assertThat(coursCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(coursCriteria)
        );
    }

    @Test
    void coursCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var coursCriteria = new CoursCriteria();
        setAllFilters(coursCriteria);

        var copy = coursCriteria.copy();

        assertThat(coursCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(coursCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var coursCriteria = new CoursCriteria();

        assertThat(coursCriteria).hasToString("CoursCriteria{}");
    }

    private static void setAllFilters(CoursCriteria coursCriteria) {
        coursCriteria.id();
        coursCriteria.nom();
        coursCriteria.description();
        coursCriteria.nombreHeures();
        coursCriteria.classeId();
        coursCriteria.enseignantId();
        coursCriteria.etudiantsId();
        coursCriteria.distinct();
    }

    private static Condition<CoursCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNom()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getNombreHeures()) &&
                condition.apply(criteria.getClasseId()) &&
                condition.apply(criteria.getEnseignantId()) &&
                condition.apply(criteria.getEtudiantsId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CoursCriteria> copyFiltersAre(CoursCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNom(), copy.getNom()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getNombreHeures(), copy.getNombreHeures()) &&
                condition.apply(criteria.getClasseId(), copy.getClasseId()) &&
                condition.apply(criteria.getEnseignantId(), copy.getEnseignantId()) &&
                condition.apply(criteria.getEtudiantsId(), copy.getEtudiantsId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
