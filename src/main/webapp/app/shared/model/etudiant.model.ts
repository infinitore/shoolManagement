import dayjs from 'dayjs';
import { ICours } from 'app/shared/model/cours.model';
import { IClasse } from 'app/shared/model/classe.model';

export interface IEtudiant {
  id?: number;
  prenom?: string;
  nom?: string;
  dateNaissance?: dayjs.Dayjs;
  adresse?: string | null;
  email?: string | null;
  telephone?: string | null;
  cours?: ICours[] | null;
  classe?: IClasse | null;
}

export const defaultValue: Readonly<IEtudiant> = {};
