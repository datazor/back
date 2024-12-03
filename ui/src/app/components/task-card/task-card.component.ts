import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Task, TaskUpdate } from '../../models/task.model';
import { ProjectMember, MemberRole } from '../../models/member.model';
import { TaskDetailsPopupComponent } from '../task-details-popup/task-details-popup.component';
import { ClickOutsideDirective } from '../../directives/click-outside.directive';

@Component({
  selector: 'app-task-card',
  standalone: true,
  imports: [CommonModule, TaskDetailsPopupComponent, ClickOutsideDirective],
  templateUrl: './task-card.component.html',
  styleUrls: ['./task-card.component.css']
})
export class TaskCardComponent {
  @Input() task!: Task;
  @Input() members: ProjectMember[] = [];
  @Input() currentUserRole: MemberRole = 'Observer';
  @Output() assigneeChange = new EventEmitter<{ taskId: string, memberId: string }>();
  @Output() taskUpdate = new EventEmitter<{ taskId: string, updates: TaskUpdate }>();

  showAssigneeDropdown = false;
  showDetailsPopup = false;

  get filteredMembers() {
    return this.members.filter(member => member.role !== 'Observer');
  }


  canAssignTask(): boolean {
    return ['Administrator', 'Member'].includes(this.currentUserRole);
  }

  toggleAssigneeDropdown(event: Event): void {
    if (!this.canAssignTask()) return;
    event.stopPropagation();
    this.showAssigneeDropdown = !this.showAssigneeDropdown;
  }

  assignTask(event: Event, memberId: string): void {
    event.stopPropagation();
    this.task.assigneeId = memberId;
    this.assigneeChange.emit({ taskId: this.task.id || '', memberId });
    this.showAssigneeDropdown = false;
  }

  getMemberInitials(name: string): string {
    return name
      .split(' ')
      .map(word => word.charAt(0).toUpperCase())
      .join('');
  }

  getAssigneeName(): string {
    // console.log('task', this.task, 'projectMember', this.members);
    if (!this.task.assigneeId) return 'Unassigned';
    const member = this.members?.find(m => m.id === this.task.assigneeId);
    return member ? member.name : 'Unassigned';
  }

  closeDropdown(): void {
    this.showAssigneeDropdown = false;
  }

  openDetailsPopup(): void {
    this.showDetailsPopup = true;
  }

  closeDetailsPopup(): void {
    this.showDetailsPopup = false;
  }

  onTaskUpdate(updates: TaskUpdate): void {
    this.taskUpdate.emit({ taskId: this.task.id || '', updates });
  }
}