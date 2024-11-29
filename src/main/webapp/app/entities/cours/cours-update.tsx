import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IClasse } from 'app/shared/model/classe.model';
import { getEntities as getClasses } from 'app/entities/classe/classe.reducer';
import { IEnseignant } from 'app/shared/model/enseignant.model';
import { getEntities as getEnseignants } from 'app/entities/enseignant/enseignant.reducer';
import { IEtudiant } from 'app/shared/model/etudiant.model';
import { getEntities as getEtudiants } from 'app/entities/etudiant/etudiant.reducer';
import { ICours } from 'app/shared/model/cours.model';
import { getEntity, updateEntity, createEntity, reset } from './cours.reducer';

export const CoursUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const classes = useAppSelector(state => state.classe.entities);
  const enseignants = useAppSelector(state => state.enseignant.entities);
  const etudiants = useAppSelector(state => state.etudiant.entities);
  const coursEntity = useAppSelector(state => state.cours.entity);
  const loading = useAppSelector(state => state.cours.loading);
  const updating = useAppSelector(state => state.cours.updating);
  const updateSuccess = useAppSelector(state => state.cours.updateSuccess);

  const handleClose = () => {
    navigate('/cours' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getClasses({}));
    dispatch(getEnseignants({}));
    dispatch(getEtudiants({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.nombreHeures !== undefined && typeof values.nombreHeures !== 'number') {
      values.nombreHeures = Number(values.nombreHeures);
    }

    const entity = {
      ...coursEntity,
      ...values,
      classe: classes.find(it => it.id.toString() === values.classe?.toString()),
      enseignant: enseignants.find(it => it.id.toString() === values.enseignant?.toString()),
      etudiants: mapIdList(values.etudiants),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...coursEntity,
          classe: coursEntity?.classe?.id,
          enseignant: coursEntity?.enseignant?.id,
          etudiants: coursEntity?.etudiants?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="shoolManagementApp.cours.home.createOrEditLabel" data-cy="CoursCreateUpdateHeading">
            <Translate contentKey="shoolManagementApp.cours.home.createOrEditLabel">Create or edit a Cours</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="cours-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('shoolManagementApp.cours.nom')}
                id="cours-nom"
                name="nom"
                data-cy="nom"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('shoolManagementApp.cours.description')}
                id="cours-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('shoolManagementApp.cours.nombreHeures')}
                id="cours-nombreHeures"
                name="nombreHeures"
                data-cy="nombreHeures"
                type="text"
              />
              <ValidatedField
                id="cours-classe"
                name="classe"
                data-cy="classe"
                label={translate('shoolManagementApp.cours.classe')}
                type="select"
              >
                <option value="" key="0" />
                {classes
                  ? classes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nom}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="cours-enseignant"
                name="enseignant"
                data-cy="enseignant"
                label={translate('shoolManagementApp.cours.enseignant')}
                type="select"
              >
                <option value="" key="0" />
                {enseignants
                  ? enseignants.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.prenom}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('shoolManagementApp.cours.etudiants')}
                id="cours-etudiants"
                data-cy="etudiants"
                type="select"
                multiple
                name="etudiants"
              >
                <option value="" key="0" />
                {etudiants
                  ? etudiants.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.prenom}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/cours" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default CoursUpdate;
