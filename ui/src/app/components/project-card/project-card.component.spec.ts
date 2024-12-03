import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { ProjectCardComponent } from './project-card.component';
import { ProjectService } from '../../services/project.service';
import { AuthService } from '../../services/auth.service';
import { Project } from '../../models/project.model';
import { ProjectMember } from '../../models/member.model';
import { of } from 'rxjs';

describe('ProjectCardComponent', () => {
  let component: ProjectCardComponent;
  let fixture: ComponentFixture<ProjectCardComponent>;
  let projectService: jest.Mocked<ProjectService>;
  let authService: jest.Mocked<AuthService>;

  const mockUser = { username: 'John Doe', email: 'john@example.com' };

  const mockMembers: ProjectMember[] = [{
    id: '1',
    name: 'John Doe',
    email: 'john@example.com',
    role: 'Administrator',
    joinedAt: new Date()
  }, {
    id: '2',
    name: 'Jane Smith',
    email: 'jane@example.com',
    role: 'Member',
    joinedAt: new Date()
  }];

  const mockProject: Project = {
    id: '1',
    name: 'Test Project',
    description: 'Test Description',
    startDate: new Date('2024-01-01'),
    members: mockMembers,
    createdBy: '1'
  };

  beforeEach(async () => {
    const projectServiceMock = {
      inviteUser: jest.fn().mockReturnValue(of(undefined))
    };

    const authServiceMock = {
      currentUser$: of(mockUser)
    };

    await TestBed.configureTestingModule({
      imports: [RouterTestingModule, ProjectCardComponent],
      providers: [
        { provide: ProjectService, useValue: projectServiceMock },
        { provide: AuthService, useValue: authServiceMock }
      ]
    }).compileComponents();

    projectService = TestBed.inject(ProjectService) as jest.Mocked<ProjectService>;
    authService = TestBed.inject(AuthService) as jest.Mocked<AuthService>;
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProjectCardComponent);
    component = fixture.componentInstance;
    component.project = mockProject;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should format date correctly', () => {
    const date = new Date('2024-01-01');
    const formatted = component.formatDate(date);
    expect(formatted).toBe('01/01/2024');
  });

  it('should get member initials', () => {
    const member = mockMembers[0];
    const initials = component.getMemberInitials(member);
    expect(initials).toBe('JD');
  });

  it('should show member tooltip', () => {
    component.showMemberTooltip(0);
    expect(component.hoveredMemberIndex).toBe(0);
  });

  it('should hide member tooltip', () => {
    component.hideMemberTooltip();
    expect(component.hoveredMemberIndex).toBeNull();
  });

  it('should get member email', () => {
    const member = mockMembers[0];
    const email = component.getMemberEmail(member);
    expect(email).toBe('john@example.com');
  });

  it('should handle context menu for admin users', () => {
    const mockEvent = {
      preventDefault: jest.fn(),
      clientX: 100,
      clientY: 100
    } as unknown as MouseEvent;

    // Component has admin user due to mockUser matching mockMembers[0]
    component.onContextMenu(mockEvent);

    expect(mockEvent.preventDefault).toHaveBeenCalled();
    expect(component.showContextMenu).toBe(true);
    expect(component.contextMenuPosition).toEqual({ x: 100, y: 100 });
  });
});