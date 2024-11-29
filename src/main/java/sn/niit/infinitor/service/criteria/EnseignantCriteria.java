package sn.niit.infinitor.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link sn.niit.infinitor.domain.Enseignant} entity. This class is used
 * in {@link sn.niit.infinitor.web.rest.EnseignantResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /enseignants?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EnseignantCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter prenom;

    private StringFilter nom;

    private LocalDateFilter dateEmbauche;

    private StringFilter specialite;

    private StringFilter email;

    private StringFilter telephone;

    private LongFilter coursId;

    private Boolean distinct;

    public EnseignantCriteria() {}

    public EnseignantCriteria(EnseignantCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.prenom = other.optionalPrenom().map(StringFilter::copy).orElse(null);
        this.nom = other.optionalNom().map(StringFilter::copy).orElse(null);
        this.dateEmbauche = other.optionalDateEmbauche().map(LocalDateFilter::copy).orElse(null);
        this.specialite = other.optionalSpecialite().map(StringFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.telephone = other.optionalTelephone().map(StringFilter::copy).orElse(null);
        this.coursId = other.optionalCoursId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public EnseignantCriteria copy() {
        return new EnseignantCriteria(this);
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

    public StringFilter getPrenom() {
        return prenom;
    }

    public Optional<StringFilter> optionalPrenom() {
        return Optional.ofNullable(prenom);
    }

    public StringFilter prenom() {
        if (prenom == null) {
            setPrenom(new StringFilter());
        }
        return prenom;
    }

    public void setPrenom(StringFilter prenom) {
        this.prenom = prenom;
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

    public LocalDateFilter getDateEmbauche() {
        return dateEmbauche;
    }

    public Optional<LocalDateFilter> optionalDateEmbauche() {
        return Optional.ofNullable(dateEmbauche);
    }

    public LocalDateFilter dateEmbauche() {
        if (dateEmbauche == null) {
            setDateEmbauche(new LocalDateFilter());
        }
        return dateEmbauche;
    }

    public void setDateEmbauche(LocalDateFilter dateEmbauche) {
        this.dateEmbauche = dateEmbauche;
    }

    public StringFilter getSpecialite() {
        return specialite;
    }

    public Optional<StringFilter> optionalSpecialite() {
        return Optional.ofNullable(specialite);
    }

    public StringFilter specialite() {
        if (specialite == null) {
            setSpecialite(new StringFilter());
        }
        return specialite;
    }

    public void setSpecialite(StringFilter specialite) {
        this.specialite = specialite;
    }

    public StringFilter getEmail() {
        return email;
    }

    public Optional<StringFilter> optionalEmail() {
        return Optional.ofNullable(email);
    }

    public StringFilter email() {
        if (email == null) {
            setEmail(new StringFilter());
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getTelephone() {
        return telephone;
    }

    public Optional<StringFilter> optionalTelephone() {
        return Optional.ofNullable(telephone);
    }

    public StringFilter telephone() {
        if (telephone == null) {
            setTelephone(new StringFilter());
        }
        return telephone;
    }

    public void setTelephone(StringFilter telephone) {
        this.telephone = telephone;
    }

    public LongFilter getCoursId() {
        return coursId;
    }

    public Optional<LongFilter> optionalCoursId() {
        return Optional.ofNullable(coursId);
    }

    public LongFilter coursId() {
        if (coursId == null) {
            setCoursId(new LongFilter());
        }
        return coursId;
    }

    public void setCoursId(LongFilter coursId) {
        this.coursId = coursId;
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
        final EnseignantCriteria that = (EnseignantCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(prenom, that.prenom) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(dateEmbauche, that.dateEmbauche) &&
            Objects.equals(specialite, that.specialite) &&
            Objects.equals(email, that.email) &&
            Objects.equals(telephone, that.telephone) &&
            Objects.equals(coursId, that.coursId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, prenom, nom, dateEmbauche, specialite, email, telephone, coursId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EnseignantCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalPrenom().map(f -> "prenom=" + f + ", ").orElse("") +
            optionalNom().map(f -> "nom=" + f + ", ").orElse("") +
            optionalDateEmbauche().map(f -> "dateEmbauche=" + f + ", ").orElse("") +
            optionalSpecialite().map(f -> "specialite=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalTelephone().map(f -> "telephone=" + f + ", ").orElse("") +
            optionalCoursId().map(f -> "coursId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
