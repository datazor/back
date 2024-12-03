import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-invite-user-dialog',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './invite-user-dialog.component.html',
  styleUrls: ['./invite-user-dialog.component.css']
})
export class InviteUserDialogComponent {
  @Input() projectName = '';
  @Output() close = new EventEmitter<void>();
  @Output() invite = new EventEmitter<string>();

  inviteForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.inviteForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });
  }

  onSubmit(): void {
    if (this.inviteForm.valid) {
      this.invite.emit(this.inviteForm.value.email);
    }
  }

  onClose(): void {
    this.close.emit();
  }
}