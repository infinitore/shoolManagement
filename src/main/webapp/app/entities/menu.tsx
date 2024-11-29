import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/etudiant">
        <Translate contentKey="global.menu.entities.etudiant" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/enseignant">
        <Translate contentKey="global.menu.entities.enseignant" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/classe">
        <Translate contentKey="global.menu.entities.classe" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/cours">
        <Translate contentKey="global.menu.entities.cours" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
