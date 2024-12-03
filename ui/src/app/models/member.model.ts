export type MemberRole = 'Administrator' | 'Member' | 'Observer';

export interface ProjectMember {
  id: string;
  name: string;
  email?: string;
  role: MemberRole;
  joinedAt?: Date;
}