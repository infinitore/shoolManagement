import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Etudiant from './etudiant';
import Enseignant from './enseignant';
import Classe from './classe';
import Cours from './cours';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="etudiant/*" element={<Etudiant />} />
        <Route path="enseignant/*" element={<Enseignant />} />
        <Route path="classe/*" element={<Classe />} />
        <Route path="cours/*" element={<Cours />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
