import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './etudiant.reducer';

export const EtudiantDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const etudiantEntity = useAppSelector(state => state.etudiant.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="etudiantDetailsHeading">
          <Translate contentKey="shoolManagementApp.etudiant.detail.title">Etudiant</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{etudiantEntity.id}</dd>
          <dt>
            <span id="prenom">
              <Translate contentKey="shoolManagementApp.etudiant.prenom">Prenom</Translate>
            </span>
          </dt>
          <dd>{etudiantEntity.prenom}</dd>
          <dt>
            <span id="nom">
              <Translate contentKey="shoolManagementApp.etudiant.nom">Nom</Translate>
            </span>
          </dt>
          <dd>{etudiantEntity.nom}</dd>
          <dt>
            <span id="dateNaissance">
              <Translate contentKey="shoolManagementApp.etudiant.dateNaissance">Date Naissance</Translate>
            </span>
          </dt>
          <dd>
            {etudiantEntity.dateNaissance ? (
              <TextFormat value={etudiantEntity.dateNaissance} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="adresse">
              <Translate contentKey="shoolManagementApp.etudiant.adresse">Adresse</Translate>
            </span>
          </dt>
          <dd>{etudiantEntity.adresse}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="shoolManagementApp.etudiant.email">Email</Translate>
            </span>
          </dt>
          <dd>{etudiantEntity.email}</dd>
          <dt>
            <span id="telephone">
              <Translate contentKey="shoolManagementApp.etudiant.telephone">Telephone</Translate>
            </span>
          </dt>
          <dd>{etudiantEntity.telephone}</dd>
          <dt>
            <Translate contentKey="shoolManagementApp.etudiant.cours">Cours</Translate>
          </dt>
          <dd>
            {etudiantEntity.cours
              ? etudiantEntity.cours.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.nom}</a>
                    {etudiantEntity.cours && i === etudiantEntity.cours.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="shoolManagementApp.etudiant.classe">Classe</Translate>
          </dt>
          <dd>{etudiantEntity.classe ? etudiantEntity.classe.nom : ''}</dd>
        </dl>
        <Button tag={Link} to="/etudiant" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/etudiant/${etudiantEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EtudiantDetail;
