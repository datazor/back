import { Component, Input, Output, EventEmitter, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import {Task, TaskStatus, TaskPriority, TaskUpdate, TaskHistory} from '../../models/task.model';
import { ProjectMember, MemberRole } from '../../models/member.model';
import {ProjectService} from "../../services/project.service";
import {TaskHistoryComponent} from "../task-history/task-history.component";

@Component({
  selector: 'app-task-details-popup',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, TaskHistoryComponent],
  templateUrl: './task-details-popup.component.html',
  styleUrls: ['./task-details-popup.component.css']
})
export class TaskDetailsPopupComponent implements OnInit, OnDestroy {
  @Input() task!: Task;
  @Input() members: ProjectMember[] = [];
  @Input() currentUserRole: MemberRole = 'Observer';
  @Output() close = new EventEmitter<void>();
  @Output() update = new EventEmitter<TaskUpdate>();

  taskForm: FormGroup;
  isEditing = false;
  taskHistory: TaskHistory[] = [];
  isLoadingHistory = true;

  readonly statuses: TaskStatus[] = ['TODO', 'IN_PROGRESS', 'DONE'];
  readonly priorities: TaskPriority[] = ['HIGH', 'MEDIUM', 'LOW'];

  constructor(
      private fb: FormBuilder,
      private projectService: ProjectService,
  ) {
    this.taskForm = this.fb.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
      status: ['', Validators.required],
      priority: ['', Validators.required],
      assigneeId: ['']
    });
  }

  ngOnInit(): void {
    this.taskForm.patchValue({
      title: this.task.title,
      description: this.task.description,
      status: this.task.status,
      priority: this.task.priority,
      assigneeId: this.task.assigneeId
    });
    this.loadTaskHistory()
    document.addEventListener('keydown', this.handleEscapeKey);
  }

  ngOnDestroy(): void {
    document.removeEventListener('keydown', this.handleEscapeKey);
  }

  private handleEscapeKey = (event: KeyboardEvent) => {
    if (event.key === 'Escape') {
      this.onClose();
    }
  };


  private loadTaskHistory(): void {
    this.projectService.getTaskHistory(this.task.id || '').subscribe({
      next: (history) => {
        this.taskHistory = history;
        this.isLoadingHistory = false;
      },
      error: (error) => {
        console.error('Error loading task history:', error);
        this.isLoadingHistory = false;
      }
    });
  }

  canEdit(): boolean {
    return ['Administrator', 'Member'].includes(this.currentUserRole);
  }

  getAssigneeName(): string {
    if (!this.task.assigneeId) return 'Unassigned';
    const member = this.members.find(m => m.id === this.task.assigneeId);
    return member ? member.name : 'Unassigned';
  }

  onClose(): void {
    if (this.isEditing) {
      this.isEditing = false;
      this.taskForm.reset();
    }
    this.close.emit();
  }

  stopPropagation(event: MouseEvent): void {
    event.stopPropagation();
  }

  toggleEdit(): void {
    this.isEditing = !this.isEditing;
    if (!this.isEditing) {
      this.taskForm.patchValue({
        title: this.task.title,
        description: this.task.description,
        status: this.task.status,
        priority: this.task.priority,
        assigneeId: this.task.assigneeId
      });
    }
  }

  onSubmit(): void {
    if (this.taskForm.valid) {
      const updates: TaskUpdate = {};
      Object.keys(this.taskForm.controls).forEach(key => {
        const control = this.taskForm.get(key);
        if (control && control.dirty) {
          updates[key as keyof TaskUpdate] = control.value;
        }
      });

      if (Object.keys(updates).length > 0) {
        this.update.emit(updates);
      }
      this.isEditing = false;
      window.location.reload();
    }
  }
}