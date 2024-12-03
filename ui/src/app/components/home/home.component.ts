import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProjectService } from '../../services/project.service';
import { Project } from '../../models/project.model';
import { ProjectListComponent } from '../project-list/project-list.component';
import { CreateProjectDialogComponent } from '../create-project-dialog/create-project-dialog.component';
import { NavbarComponent } from '../navbar/navbar.component';
import { UserProfileComponent } from '../user-profile/user-profile.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule, 
    ProjectListComponent, 
    CreateProjectDialogComponent,
    NavbarComponent,
    UserProfileComponent
  ],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  projects: Project[] = [];
  showCreateDialog = false;

  constructor(private projectService: ProjectService) {}

  ngOnInit(): void {
    this.loadProjects();
  }

  loadProjects(): void {
    this.projectService.getProjects().subscribe({
      next: (projects) => {
        this.projects = projects;
        console.log(this.projects);
        console.log(this.projects.length);
      },
      error: (error) => {
        console.error('Error loading projects:', error);
      }
    });
  }

  openCreateDialog(): void {
    this.showCreateDialog = true;
  }

  closeCreateDialog(): void {
    this.showCreateDialog = false;
  }

  onProjectCreated(project: Project): void {
    console.log(this.projects);
    this.closeCreateDialog();
  }
}