import { Component, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { UserProfileComponent } from '../user-profile/user-profile.component';
import {NotificationComponent} from "../notification/notification.component";

@Component({
  selector: 'app-navbar',
  standalone: true,
    imports: [CommonModule, RouterModule, UserProfileComponent, NotificationComponent],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  @Output() createProject = new EventEmitter<void>();

  onCreateProject(): void {
    this.createProject.emit();
  }
}