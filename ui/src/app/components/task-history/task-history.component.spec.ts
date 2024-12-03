import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TaskHistoryComponent } from './task-history.component';
import { TaskHistory } from '../../models/task.model';
import { ProjectMember } from '../../models/member.model';

describe('TaskHistoryComponent', () => {
    let component: TaskHistoryComponent;
    let fixture: ComponentFixture<TaskHistoryComponent>;

    const mockMembers: ProjectMember[] = [
        {
            id: '1',
            name: 'John Doe',
            email: 'john@example.com',
            role: 'Administrator',
            joinedAt: new Date()
        }
    ];

    const mockHistory: TaskHistory[] = [
        {
            id: '1',
            taskId: '1',
            changes: [
                {
                    field: 'status',
                    oldValue: 'TODO',
                    newValue: 'IN_PROGRESS'
                }
            ],
            updatedBy: '1',
            updatedAt: new Date()
        }
    ];

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [TaskHistoryComponent]
        }).compileComponents();
    });

    beforeEach(() => {
        fixture = TestBed.createComponent(TaskHistoryComponent);
        component = fixture.componentInstance;
        component.history = mockHistory;
        component.members = mockMembers;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it('should get updater name', () => {
        expect(component.getUpdaterName('1')).toBe('John Doe');
        expect(component.getUpdaterName('999')).toBe('Unknown User');
    });

    it('should format field name', () => {
        expect(component.formatFieldName('status')).toBe('Status');
        expect(component.formatFieldName('assigneeId')).toBe('Assignee Id');
    });

    it('should format value', () => {
        expect(component.formatValue(null)).toBe('None');
        expect(component.formatValue(undefined)).toBe('None');
        expect(component.formatValue(true)).toBe('Yes');
        expect(component.formatValue(false)).toBe('No');
        expect(component.formatValue('TEST')).toBe('TEST');
    });
});