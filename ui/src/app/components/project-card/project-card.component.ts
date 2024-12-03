import {Component, Input, ElementRef, ViewChild, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Project } from '../../models/project.model';
import { ProjectMember, MemberRole } from '../../models/member.model';
import { ProjectService } from '../../services/project.service';
import {AuthService} from "../../services/auth.service";
import {User} from "../../models/user.model";

@Component({
  selector: 'app-project-card',
  standalone: true,
  imports: [CommonModule, RouterModule, ReactiveFormsModule],
  templateUrl: './project-card.component.html',
  styleUrls: ['./project-card.component.css']
})
export class ProjectCardComponent implements OnInit {
  @Input() project!: Project;
  @ViewChild('contextMenu') contextMenu!: ElementRef;
  private currentUser: User | null = {username: '' , email: ''};

  hoveredMemberIndex: number | null = null;
  showContextMenu = false;
  contextMenuPosition = { x: 0, y: 0 };
  showInviteDialog = false;
  inviteForm: FormGroup;

  constructor(
      private projectService: ProjectService,
      private fb: FormBuilder,
      private authService: AuthService,
  ) {
    this.inviteForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      role: ['MEMBER', Validators.required]
    });
  }

  ngOnInit(): void {
    this.authService.currentUser$.subscribe(user =>
        this.currentUser = user
    );
  }

  formatDate(date: Date): string {
    return new Date(date).toLocaleDateString('en-US', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric'
    });
  }

  showMemberTooltip(index: number): void {
    this.hoveredMemberIndex = index;
  }

  hideMemberTooltip(): void {
    this.hoveredMemberIndex = null;
  }

  getMemberEmail(member: ProjectMember): string {
    return member.email || ' ';
  }

  getMemberInitials(member: ProjectMember): string {
    return member.name.split(' ')
        .map(word => word.charAt(0).toUpperCase())
        .join('');
  }

  getRoleColor(role: MemberRole): string {
    switch (role) {
      case 'Administrator':
        return '#dc2626';
      case 'Member':
        return '#2563eb';
      case 'Observer':
        return '#059669';
      default:
        return '#6b7280';
    }
  }

  isAdmin(): boolean {
     return this.project.members.find(member => member.name == this.currentUser?.username)?.role === 'Administrator';
  }

  onContextMenu(event: MouseEvent): void {
    if (!this.isAdmin()) return;

    event.preventDefault();
    this.showContextMenu = true;
    this.contextMenuPosition = {
      x: event.clientX,
      y: event.clientY
    };

    document.addEventListener('click', this.closeContextMenu);
  }

  closeContextMenu = (): void => {
    this.showContextMenu = false;
    document.removeEventListener('click', this.closeContextMenu);
  };

  openInviteDialog(): void {
    if (!this.isAdmin()) return;
    this.showInviteDialog = true;
    this.closeContextMenu();
  }

  closeInviteDialog(): void {
    this.showInviteDialog = false;
    this.inviteForm.reset({ role: 'Member' });
  }

  onInviteSubmit(): void {
    if (this.inviteForm.valid && this.project.id) {
      const { email, role } = this.inviteForm.value;
      console.log('inviteBy:', this.currentUser)

      this.projectService.inviteUser(this.project.id, email, role, this.currentUser?.username).subscribe({
        next: () => {
          this.closeInviteDialog();
        },
        error: (error) => {
          console.error('Error inviting user:', error);
        }
      });
    }
  }
}