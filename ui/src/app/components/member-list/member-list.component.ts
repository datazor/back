import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProjectMember, MemberRole } from '../../models/member.model';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-member-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './member-list.component.html',
  styleUrls: ['./member-list.component.css']
})
export class MemberListComponent {
  @Input() members: ProjectMember[] = [];
  @Input() isAdmin = false;
  @Output() roleChange = new EventEmitter<{memberId: string, newRole: MemberRole}>();

  readonly roles: MemberRole[] = ['Member', 'Observer', 'Administrator'];

  getMemberInitials(member: ProjectMember): string {
    return member.name
      .split(' ')
      .map(word => word.charAt(0).toUpperCase())
      .join('');
  }

  onRoleChange(member: ProjectMember, event: Event): void {
    console.log('members', this.members);
    const select = event.target as HTMLSelectElement;
    const newRole = select.value as MemberRole;
    this.roleChange.emit({ memberId: member.id, newRole });
    console.log('member', member, 'role', newRole);
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
}