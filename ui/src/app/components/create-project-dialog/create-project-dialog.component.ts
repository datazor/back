import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ProjectService } from '../../services/project.service';
import { Project } from '../../models/project.model';
import {AuthService} from "../../services/auth.service";
import {User} from "../../models/user.model";

@Component({
  selector: 'app-create-project-dialog',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './create-project-dialog.component.html',
  styleUrls: ['./create-project-dialog.component.css']
})
export class CreateProjectDialogComponent implements OnInit {
  @Output() close = new EventEmitter<void>();
  @Output() projectCreated = new EventEmitter<Project>();

  projectForm: FormGroup;
  isLoading = false;
  currentUser: User | null = {email: "", username: ""};

  constructor(
    private fb: FormBuilder,
    private projectService: ProjectService,
    private authService: AuthService
  ) {
    this.projectForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      description: ['', Validators.required],
      startDate: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.authService.currentUser$.subscribe(user => {
      this.currentUser = user;
    });
  }

  onSubmit(): void {
    if (this.projectForm.valid) {
      this.isLoading = true;
      const project: Project = {
        ...this.projectForm.value,
        members: [{...this.currentUser, role: "Administrator"}],
        createdBy: this.currentUser?.username
      };

      this.projectService.createProject(project).subscribe({
        next: (createdProject) => {
          this.projectCreated.emit(createdProject);
          window.location.reload();
        },
        error: (error) => {
          console.error('Error creating project:', error);
          this.isLoading = false;
        }
      });
    }
  }

  onClose(): void {
    this.close.emit();
  }
}