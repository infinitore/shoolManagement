import { IClasse } from 'app/shared/model/classe.model';
import { IEnseignant } from 'app/shared/model/enseignant.model';
import { IEtudiant } from 'app/shared/model/etudiant.model';

export interface ICours {
  id?: number;
  nom?: string;
  description?: string | null;
  nombreHeures?: number | null;
  classe?: IClasse | null;
  enseignant?: IEnseignant | null;
  etudiants?: IEtudiant[] | null;
}

export const defaultValue: Readonly<ICours> = {};
