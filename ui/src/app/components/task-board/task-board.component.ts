import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Task, TaskStatus, TaskUpdate } from '../../models/task.model';
import { ProjectMember, MemberRole } from '../../models/member.model';
import { TaskCardComponent } from '../task-card/task-card.component';

@Component({
  selector: 'app-task-board',
  standalone: true,
  imports: [CommonModule, TaskCardComponent],
  templateUrl: './task-board.component.html',
  styleUrls: ['./task-board.component.css']
})
export class TaskBoardComponent {
  @Input() tasks: Task[] = [];
  @Input() members: ProjectMember[] = [];
  @Input() currentUserRole: MemberRole = 'Observer';
  @Output() statusChange = new EventEmitter<{task: Task, status: TaskStatus}>();
  @Output() assigneeChange = new EventEmitter<{taskId: string, memberId: string}>();
  @Output() taskUpdate = new EventEmitter<{taskId: string, updates: TaskUpdate}>();

  readonly columns: {title: string, status: TaskStatus}[] = [
    { title: 'To Do', status: 'TODO' },
    { title: 'In Progress', status: 'IN_PROGRESS' },
    { title: 'Done', status: 'DONE' }
  ];

  getTasksByStatus(status: TaskStatus): Task[] {
    return this.tasks.filter(task => task.status === status);
  }

  onDragStart(event: DragEvent, task: Task): void {
    if (event.dataTransfer) {
      event.dataTransfer.setData('taskId', task.id || '');
    }
  }

  onDragOver(event: DragEvent): void {
    event.preventDefault();
  }

  onDrop(event: DragEvent, newStatus: TaskStatus): void {
    event.preventDefault();
    const taskId = event.dataTransfer?.getData('taskId');
    const task = this.tasks.find(t => t.id === taskId);
    
    if (task && task.status !== newStatus) {
      this.statusChange.emit({ task, status: newStatus });
    }
  }

  onAssigneeChange(event: { taskId: string, memberId: string }): void {
    this.assigneeChange.emit(event);
  }

  onTaskUpdate(event: { taskId: string, updates: TaskUpdate }): void {
    this.taskUpdate.emit(event);
  }
}