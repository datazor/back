import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { CreateTaskPopupComponent } from './create-task-popup.component';
import { ProjectMember } from '../../models/member.model';

describe('CreateTaskPopupComponent', () => {
  let component: CreateTaskPopupComponent;
  let fixture: ComponentFixture<CreateTaskPopupComponent>;

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
      imports: [ReactiveFormsModule, CreateTaskPopupComponent]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateTaskPopupComponent);
    component = fixture.componentInstance;
    component.members = mockMembers;
    fixture.detectChanges();
  });

  it('should initialize form with default values', () => {
    expect(component.taskForm.get('title')?.value).toBe('');
    expect(component.taskForm.get('description')?.value).toBe('');
    expect(component.taskForm.get('priority')?.value).toBe('MEDIUM');
    expect(component.taskForm.get('assigneeId')?.value).toBe('');
  });

  it('should validate required fields', () => {
    const form = component.taskForm;
    expect(form.valid).toBeFalsy();

    form.controls['title'].setValue('Test Task');
    form.controls['description'].setValue('Test Description');
    
    expect(form.valid).toBeTruthy();
  });

  it('should validate title minimum length', () => {
    const titleControl = component.taskForm.get('title');
    
    titleControl?.setValue('Te');
    expect(titleControl?.errors?.['minlength']).toBeTruthy();
    
    titleControl?.setValue('Test Task');
    expect(titleControl?.errors).toBeFalsy();
  });

  it('should validate description minimum length', () => {
    const descControl = component.taskForm.get('description');
    
    descControl?.setValue('Short');
    expect(descControl?.errors?.['minlength']).toBeTruthy();
    
    descControl?.setValue('This is a proper task description');
    expect(descControl?.errors).toBeFalsy();
  });

  it('should emit create event with valid form data', () => {
    jest.spyOn(component.create, 'emit');
    
    const taskData = {
      title: 'Test Task',
      description: 'Test Description',
      priority: 'HIGH',
      assigneeId: '1'
    };

    component.taskForm.patchValue(taskData);
    component.onSubmit();

    expect(component.create.emit).toHaveBeenCalledWith({
      ...taskData,
      status: 'TODO'
    });
  });

  it('should not emit create event with invalid form', () => {
    jest.spyOn(component.create, 'emit');
    
    component.onSubmit();
    
    expect(component.create.emit).not.toHaveBeenCalled();
  });

  it('should emit close event', () => {
    jest.spyOn(component.close, 'emit');
    
    component.onClose();
    
    expect(component.close.emit).toHaveBeenCalled();
  });

  it('should stop event propagation', () => {
    const mockEvent = {
      stopPropagation: jest.fn()
    } as unknown as MouseEvent;

    component.stopPropagation(mockEvent);
    
    expect(mockEvent.stopPropagation).toHaveBeenCalled();
  });
});