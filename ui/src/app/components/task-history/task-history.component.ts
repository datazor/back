import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TaskHistory } from '../../models/task.model';
import { ProjectMember } from '../../models/member.model';

@Component({
    selector: 'app-task-history',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './task-history.component.html',
    styleUrls: ['./task-history.component.css']
})
export class TaskHistoryComponent {
    @Input() history: TaskHistory[] = [];
    @Input() members: ProjectMember[] = [];

    getUpdaterName(userId: string): string {
        console.log('user', userId, 'users', this.members)
        const member = this.members.find(m => m.id == userId);
        console.log('member', member);
        return member ? member.name : 'Unknown User';
    }

    formatFieldName(field: string): string {
        return field.charAt(0).toUpperCase() + field.slice(1).replace(/([A-Z])/g, ' $1');
    }

    formatValue(value: any): string {
        if (value === null || value === undefined) return 'None';
        if (value instanceof Date) return new Date(value).toLocaleString();
        if (typeof value === 'boolean') return value ? 'Yes' : 'No';
        return String(value);
    }
}