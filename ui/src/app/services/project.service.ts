import {Injectable} from '@angular/core';
import {Observable, from, throwError} from 'rxjs';
import { Project } from '../models/project.model';
import {Task, TaskHistory, TaskUpdate} from '../models/task.model';
import {MemberRole} from '../models/member.model';
import axios from "../core/api/axios";
import {catchError, map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class ProjectService {
  getProjects(): Observable<Project[]> {
    return from(axios.get(`/projects/${localStorage.getItem('username')}`)
        .then(response => {
          console.log('getProjects Response:', response)
          return response as unknown as Project[]
        }));
  }

  getProjectById(id: string): Observable<Project> {
    return from(axios.get(`/projects/project/${id}`).then(response => {
      return response as unknown as Project
    }));
  }

  getProjectTasks(projectId: string): Observable<Task[]> {
    return from(axios.get(`/tasks/${projectId}`).then(
        response => response as unknown as Task[]
    ));
  }

  updateTaskAssignee(taskId: string, memberId: string | undefined, userId: string | undefined): Observable<Task> {
    const updates: TaskUpdate = {
      assigneeId: memberId,
      updater: userId
    };

    return this.updateTask(taskId, updates);
  }



  getTaskHistory(taskId: string): Observable<TaskHistory[]> {
    return from(axios.get(`/tasks/${taskId}/history`).then(
        response => response as unknown as TaskHistory[]
    ));
  }

updateTask(taskId: string, updates: TaskUpdate): Observable<Task> {
    const endpoint = `/tasks/${taskId}`;

    return from(
        axios.put<Task>(endpoint, updates).then(response => response.data)
    ).pipe(
        map((updatedTask) => updatedTask),
        catchError((error) => {
          console.error('Error updating task:', error);

          let errorMessage = 'An error occurred while updating the task.';
          if (error.response) {
            errorMessage = `Error ${error.response.status}: ${error.response.data?.message || error.message}`;
          } else if (error.request) {
            errorMessage = 'No response received from the server.';
          } else {
            errorMessage = `Request error: ${error.message}`;
          }

          return throwError(() => new Error(errorMessage));
        })
    );
  }

  createProject(project: Project): Observable<Project> {
    return from(axios.post<Project>('/projects/create', project)
            .then(response => response.data));
  }

  updateMemberRole(projectId: string, memberId: string, newRole: MemberRole): Observable<void> {
    const endpoint = `/projects/${projectId}/members/${memberId}/role`;

    return from(
        axios.put<void>(endpoint, {newRole})
    ).pipe(
        map(() => undefined),
        catchError((error) => {
          console.error('Error updating member role:', error);

          let errorMessage = 'An error occurred while updating the member role.';
          if (error.response) {
            errorMessage = `Error ${error.response.status}: ${error.response.data?.message || error.message}`;
          } else if (error.request) {
            errorMessage = 'No response received from the server.';
          } else {
            errorMessage = `Request error: ${error.message}`;
          }

          return throwError(() => new Error(errorMessage));
        })
    );
  }

  createTask(projectId: string, task: Task): Observable<Task> {
    const taskRequest = {...task, projectId: projectId};
    console.log('taskRequest', taskRequest);
    return from(axios.post<Task>(`/tasks/create`, taskRequest).then(response => {
          console.log('call as response', response)
          return response as unknown as Task
        }))
  }

  inviteUser(projectId: string, email: string, role: MemberRole, invitedBy:string | undefined): Observable<void> {
    return from(axios.post('/projects/invite', {projectId: projectId, role:role, email:email, invitedById:invitedBy})
        .then(response => response.data));
  }
}