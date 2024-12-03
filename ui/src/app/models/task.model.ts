export type TaskStatus = 'TODO' | 'IN_PROGRESS' | 'DONE';
export type TaskPriority = 'HIGH' | 'MEDIUM' | 'LOW';

export interface Task {
  id?: string;
  title?: string;
  description?: string;
  status?: TaskStatus;
  priority?: TaskPriority;
  assigneeId?: string;
  createdAt: Date;
  updatedAt?: Date;
  projectId?: number;
}

export interface TaskUpdate {
  updater?: string;
  title?: string;
  description?: string;
  status?: TaskStatus;
  priority?: TaskPriority;
  assigneeId?: string;
}

export interface TaskHistory {
  id: string;
  taskId: string;
  changes: {
    field: keyof Task;
    oldValue: any;
    newValue: any;
  }[];
  updatedBy: string;
  updatedAt: Date;
}