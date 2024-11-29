package sn.niit.infinitor.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link sn.niit.infinitor.domain.Cours} entity. This class is used
 * in {@link sn.niit.infinitor.web.rest.CoursResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cours?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CoursCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nom;

    private StringFilter description;

    private IntegerFilter nombreHeures;

    private LongFilter classeId;

    private LongFilter enseignantId;

    private LongFilter etudiantsId;

    private Boolean distinct;

    public CoursCriteria() {}

    public CoursCriteria(CoursCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nom = other.optionalNom().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.nombreHeures = other.optionalNombreHeures().map(IntegerFilter::copy).orElse(null);
        this.classeId = other.optionalClasseId().map(LongFilter::copy).orElse(null);
        this.enseignantId = other.optionalEnseignantId().map(LongFilter::copy).orElse(null);
        this.etudiantsId = other.optionalEtudiantsId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CoursCriteria copy() {
        return new CoursCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNom() {
        return nom;
    }

    public Optional<StringFilter> optionalNom() {
        return Optional.ofNullable(nom);
    }

    public StringFilter nom() {
        if (nom == null) {
            setNom(new StringFilter());
        }
        return nom;
    }

    public void setNom(StringFilter nom) {
        this.nom = nom;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public IntegerFilter getNombreHeures() {
        return nombreHeures;
    }

    public Optional<IntegerFilter> optionalNombreHeures() {
        return Optional.ofNullable(nombreHeures);
    }

    public IntegerFilter nombreHeures() {
        if (nombreHeures == null) {
            setNombreHeures(new IntegerFilter());
        }
        return nombreHeures;
    }

    public void setNombreHeures(IntegerFilter nombreHeures) {
        this.nombreHeures = nombreHeures;
    }

    public LongFilter getClasseId() {
        return classeId;
    }

    public Optional<LongFilter> optionalClasseId() {
        return Optional.ofNullable(classeId);
    }

    public LongFilter classeId() {
        if (classeId == null) {
            setClasseId(new LongFilter());
        }
        return classeId;
    }

    public void setClasseId(LongFilter classeId) {
        this.classeId = classeId;
    }

    public LongFilter getEnseignantId() {
        return enseignantId;
    }

    public Optional<LongFilter> optionalEnseignantId() {
        return Optional.ofNullable(enseignantId);
    }

    public LongFilter enseignantId() {
        if (enseignantId == null) {
            setEnseignantId(new LongFilter());
        }
        return enseignantId;
    }

    public void setEnseignantId(LongFilter enseignantId) {
        this.enseignantId = enseignantId;
    }

    public LongFilter getEtudiantsId() {
        return etudiantsId;
    }

    public Optional<LongFilter> optionalEtudiantsId() {
        return Optional.ofNullable(etudiantsId);
    }

    public LongFilter etudiantsId() {
        if (etudiantsId == null) {
            setEtudiantsId(new LongFilter());
        }
        return etudiantsId;
    }

    public void setEtudiantsId(LongFilter etudiantsId) {
        this.etudiantsId = etudiantsId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CoursCriteria that = (CoursCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(description, that.description) &&
            Objects.equals(nombreHeures, that.nombreHeures) &&
            Objects.equals(classeId, that.classeId) &&
            Objects.equals(enseignantId, that.enseignantId) &&
            Objects.equals(etudiantsId, that.etudiantsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, description, nombreHeures, classeId, enseignantId, etudiantsId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CoursCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNom().map(f -> "nom=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalNombreHeures().map(f -> "nombreHeures=" + f + ", ").orElse("") +
            optionalClasseId().map(f -> "classeId=" + f + ", ").orElse("") +
            optionalEnseignantId().map(f -> "enseignantId=" + f + ", ").orElse("") +
            optionalEtudiantsId().map(f -> "etudiantsId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
