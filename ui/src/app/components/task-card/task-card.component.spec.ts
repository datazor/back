import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TaskCardComponent } from './task-card.component';
import { Task } from '../../models/task.model';
import { ProjectMember } from '../../models/member.model';

describe('TaskCardComponent', () => {
  let component: TaskCardComponent;
  let fixture: ComponentFixture<TaskCardComponent>;

  const mockTask: Task = {
    id: '1',
    title: 'Test Task',
    description: 'Test Description',
    status: 'TODO',
    priority: 'HIGH',
    assigneeId: '1',
    createdAt: new Date(),
    updatedAt: new Date()
  };

  const mockMembers: ProjectMember[] = [
    {
      id: '1',
      name: 'John Doe',
      email: 'john@example.com',
      role: 'Administrator',
      joinedAt: new Date()
    },
    {
      id: '2',
      name: 'Jane Smith',
      email: 'jane@example.com',
      role: 'Member',
      joinedAt: new Date()
    }
  ];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TaskCardComponent]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TaskCardComponent);
    component = fixture.componentInstance;
    component.task = mockTask;
    component.members = mockMembers;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should allow task assignment for administrators', () => {
    component.currentUserRole = 'Administrator';
    expect(component.canAssignTask()).toBeTruthy();
  });

  it('should allow task assignment for members', () => {
    component.currentUserRole = 'Member';
    expect(component.canAssignTask()).toBeTruthy();
  });

  it('should not allow task assignment for observers', () => {
    component.currentUserRole = 'Observer';
    expect(component.canAssignTask()).toBeFalsy();
  });

  it('should get correct member initials', () => {
    const initials = component.getMemberInitials('John Doe');
    expect(initials).toBe('JD');
  });

  it('should get correct assignee name', () => {
    expect(component.getAssigneeName()).toBe('John Doe');
  });

  it('should return "Unassigned" when no assignee', () => {
    component.task = { ...mockTask, assigneeId: undefined };
    expect(component.getAssigneeName()).toBe('Unassigned');
  });

  it('should emit assignee change event', () => {
    jest.spyOn(component.assigneeChange, 'emit');
    const memberId = '2';
    const event = new MouseEvent('click');
    
    component.assignTask(event, memberId);
    
    expect(component.assigneeChange.emit).toHaveBeenCalledWith({
      taskId: mockTask.id,
      memberId
    });
    expect(component.showAssigneeDropdown).toBeFalsy();
  });

  it('should toggle details popup', () => {
    expect(component.showDetailsPopup).toBeFalsy();
    
    component.openDetailsPopup();
    expect(component.showDetailsPopup).toBeTruthy();
    
    component.closeDetailsPopup();
    expect(component.showDetailsPopup).toBeFalsy();
  });

  it('should emit task update event', () => {
    jest.spyOn(component.taskUpdate, 'emit');
    const updates = { title: 'Updated Title' };
    
    component.onTaskUpdate(updates);
    
    expect(component.taskUpdate.emit).toHaveBeenCalledWith({
      taskId: mockTask.id,
      updates
    });
  });
});