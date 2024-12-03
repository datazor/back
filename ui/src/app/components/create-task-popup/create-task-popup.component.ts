import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Task, TaskPriority } from '../../models/task.model';
import { ProjectMember } from '../../models/member.model';

@Component({
  selector: 'app-create-task-popup',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './create-task-popup.component.html',
  styleUrls: ['./create-task-popup.component.css']
})
export class CreateTaskPopupComponent {
  @Input() members: ProjectMember[] = [];
  @Output() close = new EventEmitter<void>();
  @Output() create = new EventEmitter<Partial<Task>>();

  taskForm: FormGroup;
  readonly priorities: TaskPriority[] = ['HIGH', 'MEDIUM', 'LOW'];
  isSubmitting = false;

  constructor(private fb: FormBuilder) {
    this.taskForm = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(3)]],
      description: ['', [Validators.required, Validators.minLength(10)]],
      priority: ['MEDIUM', Validators.required],
      assigneeId: ['']
    });
  }

  onSubmit(): void {
    if (this.taskForm.valid && !this.isSubmitting) {
      this.isSubmitting = true;
      console.log('you have form ', this.taskForm);
      this.create.emit({ ...this.taskForm.value, status: 'TODO' });
      this.isSubmitting = false;
    }
  }

  onClose(): void {
    this.close.emit();
  }

  stopPropagation(event: MouseEvent): void {
    event.stopPropagation();
  }
}