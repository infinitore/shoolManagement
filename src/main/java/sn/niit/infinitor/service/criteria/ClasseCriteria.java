package sn.niit.infinitor.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link sn.niit.infinitor.domain.Classe} entity. This class is used
 * in {@link sn.niit.infinitor.web.rest.ClasseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /classes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClasseCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nom;

    private StringFilter niveau;

    private StringFilter anneeScolaire;

    private LongFilter etudiantsId;

    private Boolean distinct;

    public ClasseCriteria() {}

    public ClasseCriteria(ClasseCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nom = other.optionalNom().map(StringFilter::copy).orElse(null);
        this.niveau = other.optionalNiveau().map(StringFilter::copy).orElse(null);
        this.anneeScolaire = other.optionalAnneeScolaire().map(StringFilter::copy).orElse(null);
        this.etudiantsId = other.optionalEtudiantsId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ClasseCriteria copy() {
        return new ClasseCriteria(this);
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

    public StringFilter getNiveau() {
        return niveau;
    }

    public Optional<StringFilter> optionalNiveau() {
        return Optional.ofNullable(niveau);
    }

    public StringFilter niveau() {
        if (niveau == null) {
            setNiveau(new StringFilter());
        }
        return niveau;
    }

    public void setNiveau(StringFilter niveau) {
        this.niveau = niveau;
    }

    public StringFilter getAnneeScolaire() {
        return anneeScolaire;
    }

    public Optional<StringFilter> optionalAnneeScolaire() {
        return Optional.ofNullable(anneeScolaire);
    }

    public StringFilter anneeScolaire() {
        if (anneeScolaire == null) {
            setAnneeScolaire(new StringFilter());
        }
        return anneeScolaire;
    }

    public void setAnneeScolaire(StringFilter anneeScolaire) {
        this.anneeScolaire = anneeScolaire;
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
        final ClasseCriteria that = (ClasseCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(niveau, that.niveau) &&
            Objects.equals(anneeScolaire, that.anneeScolaire) &&
            Objects.equals(etudiantsId, that.etudiantsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, niveau, anneeScolaire, etudiantsId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClasseCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNom().map(f -> "nom=" + f + ", ").orElse("") +
            optionalNiveau().map(f -> "niveau=" + f + ", ").orElse("") +
            optionalAnneeScolaire().map(f -> "anneeScolaire=" + f + ", ").orElse("") +
            optionalEtudiantsId().map(f -> "etudiantsId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
