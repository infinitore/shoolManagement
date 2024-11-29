import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './cours.reducer';

export const CoursDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const coursEntity = useAppSelector(state => state.cours.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="coursDetailsHeading">
          <Translate contentKey="shoolManagementApp.cours.detail.title">Cours</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{coursEntity.id}</dd>
          <dt>
            <span id="nom">
              <Translate contentKey="shoolManagementApp.cours.nom">Nom</Translate>
            </span>
          </dt>
          <dd>{coursEntity.nom}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="shoolManagementApp.cours.description">Description</Translate>
            </span>
          </dt>
          <dd>{coursEntity.description}</dd>
          <dt>
            <span id="nombreHeures">
              <Translate contentKey="shoolManagementApp.cours.nombreHeures">Nombre Heures</Translate>
            </span>
          </dt>
          <dd>{coursEntity.nombreHeures}</dd>
          <dt>
            <Translate contentKey="shoolManagementApp.cours.classe">Classe</Translate>
          </dt>
          <dd>{coursEntity.classe ? coursEntity.classe.nom : ''}</dd>
          <dt>
            <Translate contentKey="shoolManagementApp.cours.enseignant">Enseignant</Translate>
          </dt>
          <dd>{coursEntity.enseignant ? coursEntity.enseignant.prenom : ''}</dd>
          <dt>
            <Translate contentKey="shoolManagementApp.cours.etudiants">Etudiants</Translate>
          </dt>
          <dd>
            {coursEntity.etudiants
              ? coursEntity.etudiants.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.prenom}</a>
                    {coursEntity.etudiants && i === coursEntity.etudiants.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/cours" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/cours/${coursEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CoursDetail;
