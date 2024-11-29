import etudiant from 'app/entities/etudiant/etudiant.reducer';
import enseignant from 'app/entities/enseignant/enseignant.reducer';
import classe from 'app/entities/classe/classe.reducer';
import cours from 'app/entities/cours/cours.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  etudiant,
  enseignant,
  classe,
  cours,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
