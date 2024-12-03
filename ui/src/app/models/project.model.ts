import { ProjectMember } from './member.model';

export interface Project {
  id: string;
  name: string;
  description?: string;
  startDate: Date;
  members: ProjectMember[];
  createdBy?: string;
}