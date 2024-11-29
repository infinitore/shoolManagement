import dayjs from 'dayjs';

export interface IEnseignant {
  id?: number;
  prenom?: string;
  nom?: string;
  dateEmbauche?: dayjs.Dayjs;
  specialite?: string;
  email?: string | null;
  telephone?: string | null;
}

export const defaultValue: Readonly<IEnseignant> = {};
