import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './enseignant.reducer';

export const EnseignantDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const enseignantEntity = useAppSelector(state => state.enseignant.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="enseignantDetailsHeading">
          <Translate contentKey="shoolManagementApp.enseignant.detail.title">Enseignant</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{enseignantEntity.id}</dd>
          <dt>
            <span id="prenom">
              <Translate contentKey="shoolManagementApp.enseignant.prenom">Prenom</Translate>
            </span>
          </dt>
          <dd>{enseignantEntity.prenom}</dd>
          <dt>
            <span id="nom">
              <Translate contentKey="shoolManagementApp.enseignant.nom">Nom</Translate>
            </span>
          </dt>
          <dd>{enseignantEntity.nom}</dd>
          <dt>
            <span id="dateEmbauche">
              <Translate contentKey="shoolManagementApp.enseignant.dateEmbauche">Date Embauche</Translate>
            </span>
          </dt>
          <dd>
            {enseignantEntity.dateEmbauche ? (
              <TextFormat value={enseignantEntity.dateEmbauche} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="specialite">
              <Translate contentKey="shoolManagementApp.enseignant.specialite">Specialite</Translate>
            </span>
          </dt>
          <dd>{enseignantEntity.specialite}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="shoolManagementApp.enseignant.email">Email</Translate>
            </span>
          </dt>
          <dd>{enseignantEntity.email}</dd>
          <dt>
            <span id="telephone">
              <Translate contentKey="shoolManagementApp.enseignant.telephone">Telephone</Translate>
            </span>
          </dt>
          <dd>{enseignantEntity.telephone}</dd>
        </dl>
        <Button tag={Link} to="/enseignant" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/enseignant/${enseignantEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EnseignantDetail;
