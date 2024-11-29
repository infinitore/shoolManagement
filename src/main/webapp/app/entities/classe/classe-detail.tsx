import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './classe.reducer';

export const ClasseDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const classeEntity = useAppSelector(state => state.classe.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="classeDetailsHeading">
          <Translate contentKey="shoolManagementApp.classe.detail.title">Classe</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{classeEntity.id}</dd>
          <dt>
            <span id="nom">
              <Translate contentKey="shoolManagementApp.classe.nom">Nom</Translate>
            </span>
          </dt>
          <dd>{classeEntity.nom}</dd>
          <dt>
            <span id="niveau">
              <Translate contentKey="shoolManagementApp.classe.niveau">Niveau</Translate>
            </span>
          </dt>
          <dd>{classeEntity.niveau}</dd>
          <dt>
            <span id="anneeScolaire">
              <Translate contentKey="shoolManagementApp.classe.anneeScolaire">Annee Scolaire</Translate>
            </span>
          </dt>
          <dd>{classeEntity.anneeScolaire}</dd>
        </dl>
        <Button tag={Link} to="/classe" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/classe/${classeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ClasseDetail;
