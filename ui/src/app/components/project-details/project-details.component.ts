import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { ProjectService } from '../../services/project.service';
import { Task, TaskUpdate } from '../../models/task.model';
import { Project } from '../../models/project.model';
import { MemberRole } from '../../models/member.model';
import { TaskBoardComponent } from '../task-board/task-board.component';
import { MemberListComponent } from '../member-list/member-list.component';
import { NavbarComponent } from '../navbar/navbar.component';
import { CreateTaskPopupComponent } from '../create-task-popup/create-task-popup.component';
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'app-project-details',
  standalone: true,
  imports: [
    CommonModule, 
    TaskBoardComponent, 
    MemberListComponent,
    NavbarComponent,
    CreateTaskPopupComponent
  ],
  templateUrl: './project-details.component.html',
  styleUrls: ['./project-details.component.css']
})
export class ProjectDetailsComponent implements OnInit {
  project?: Project;
  tasks: Task[] = [];
  isLoading = true;
  currentUserRole: MemberRole = 'Observer';
  currentUserId: string | undefined = '';
  showCreateTaskPopup = false;

  constructor(
    private route: ActivatedRoute,
    private projectService: ProjectService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const projectId = this.route.snapshot.paramMap.get('id');
    console.log(this.route.snapshot.paramMap);
    if (projectId) {
      this.loadProject(projectId);
    }
    this.authService.currentUser$.subscribe(currentUser => {
      this.currentUserId = currentUser?.username || '';
    })
  }

  private loadProject(id: string): void {
    this.projectService.getProjectById(id).subscribe({
      next: (project) => {
        this.project = project;
        console.log(this.project);
        const currentMember = project.members?.find(m => m.name === localStorage.getItem('username'));
        this.currentUserRole = currentMember?.role || 'Observer';
        this.loadTasks(id);
      },
      error: (error) => {
        console.error('Error loading project:', error);
        this.isLoading = false;
      }
    });
  }

  private loadTasks(projectId: string): void {
    this.projectService.getProjectTasks(projectId).subscribe({
      next: (tasks) => {
        this.tasks = tasks;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading tasks:', error);
        this.isLoading = false;
      }
    });
  }

  onTaskAssigneeChange(event: { taskId: string; memberId: string }): void {

    this.projectService.updateTaskAssignee(event.taskId, event.memberId, this.currentUserId).subscribe({
      next: (updatedTask) => {
        this.tasks = this.tasks.map(t => 
          t.id == updatedTask.id ? updatedTask : t
        );
      },
      error: (error) => {
        console.error('Error updating task assignee:', error);
      }
    });
  }

  onTaskUpdate(event: { taskId: string; updates: TaskUpdate }): void {
    console.log('current user id', this.currentUserId)
    this.projectService.updateTask(event.taskId, {...event.updates, updater:this.currentUserId}).subscribe({
      next: (updatedTask) => {
        this.tasks = this.tasks.map(t => 
          t.id === updatedTask.id ? updatedTask : t
        );
      },
      error: (error) => {
        console.error('Error updating task:', error);
      }
    });
  }

  onMemberRoleChange(event: { memberId: string; newRole: MemberRole }): void {
    if (this.project?.id) {
      this.projectService.updateMemberRole(
        this.project.id,
        event.memberId,
        event.newRole
      ).subscribe({
        next: () => {
        },
        error: (error) => {
          console.error('Error updating member role:', error);
        }
      });
    }
  }

  openCreateTaskPopup(): void {
    this.showCreateTaskPopup = true;

  }

  closeCreateTaskPopup(): void {
    this.showCreateTaskPopup = false;
  }

  onTaskCreate(taskData: Partial<Task>): void {
    console.log('Task received',taskData);
    if (this.project?.id) {
      const newTask: Task = {
        ...taskData,
        createdAt: new Date(),
        status: taskData.status || 'TODO'
      };

      console.log('i am aboutto call create')


    this.projectService.createTask(this.project.id, newTask).subscribe({
      next: () => {
        this.closeCreateTaskPopup();
        window.location.reload();
      },
      error: (error) => {
        console.error('Error creating task:', error.message);
      }
    });
    }
  }

}