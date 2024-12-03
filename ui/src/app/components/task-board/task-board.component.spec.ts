import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TaskBoardComponent } from './task-board.component';
import { Task } from '../../models/task.model';
import { ProjectMember } from '../../models/member.model';

describe('TaskBoardComponent', () => {
  let component: TaskBoardComponent;
  let fixture: ComponentFixture<TaskBoardComponent>;

  const mockTasks: Task[] = [
    {
      id: '1',
      title: 'Task 1',
      description: 'Description 1',
      status: 'TODO',
      priority: 'HIGH',
      assigneeId: '1',
      createdAt: new Date(),
      updatedAt: new Date()
    },
    {
      id: '2',
      title: 'Task 2',
      description: 'Description 2',
      status: 'IN_PROGRESS',
      priority: 'MEDIUM',
      assigneeId: '2',
      createdAt: new Date(),
      updatedAt: new Date()
    }
  ];

  const mockMembers: ProjectMember[] = [
    {
      id: '1',
      name: 'John Doe',
      email: 'john@example.com',
      role: 'Administrator',
      joinedAt: new Date()
    }
  ];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TaskBoardComponent]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TaskBoardComponent);
    component = fixture.componentInstance;
    component.tasks = mockTasks;
    component.members = mockMembers;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should filter tasks by status', () => {
    const todoTasks = component.getTasksByStatus('TODO');
    const inProgressTasks = component.getTasksByStatus('IN_PROGRESS');
    const doneTasks = component.getTasksByStatus('DONE');

    expect(todoTasks.length).toBe(1);
    expect(inProgressTasks.length).toBe(1);
    expect(doneTasks.length).toBe(0);
  });

  it('should emit status change on drop', () => {
    jest.spyOn(component.statusChange, 'emit');
    const task = mockTasks[0];
    const newStatus = 'IN_PROGRESS';

    const mockDragEvent = {
      preventDefault: jest.fn(),
      dataTransfer: {
        getData: jest.fn().mockReturnValue(task.id)
      }
    } as unknown as DragEvent;

    component.onDrop(mockDragEvent, newStatus);

    expect(component.statusChange.emit).toHaveBeenCalledWith({
      task,
      status: newStatus
    });
  });

  it('should emit assignee change event', () => {
    jest.spyOn(component.assigneeChange, 'emit');
    const event = { taskId: '1', memberId: '2' };
    
    component.onAssigneeChange(event);
    
    expect(component.assigneeChange.emit).toHaveBeenCalledWith(event);
  });

  it('should emit task update event', () => {
    jest.spyOn(component.taskUpdate, 'emit');
    const event = { taskId: '1', updates: { title: 'Updated Title' } };
    
    component.onTaskUpdate(event);
    
    expect(component.taskUpdate.emit).toHaveBeenCalledWith(event);
  });
});