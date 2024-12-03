import { TestBed } from '@angular/core/testing';
import { ProjectService } from './project.service';
import api from '../core/api/axios';
import { Project } from '../models/project.model';

jest.mock('../core/api/axios');

describe('ProjectService', () => {
  let service: ProjectService;
  const mockedApi = api as jest.Mocked<typeof api>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ProjectService]
    });
    service = TestBed.inject(ProjectService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
  describe('createProject', () => {
    it('should create a new project', (done) => {
      const newProject: Project = {
        id:'1',
        name: 'Test Project',
        description: 'Test Description',
        startDate: new Date(),
        members: [],
        createdBy: '1'
      };

      mockedApi.post.mockResolvedValueOnce({data: newProject});

      service.createProject(newProject).subscribe(project => {
        expect(project).toBeTruthy();
        expect(project.name).toBe(newProject.name);
        expect(project.description).toBe(newProject.description);
        done();
      });
    });
  });

  describe('updateTask', () => {
    it('should update task', (done) => {
      const taskId = '1';
      const updates = {
        assigneeId: '2',
        updater: '1'
      };

      const updatedTask = {
        id: taskId,
        assigneeId: '2',
        title: 'Test Task',
        status: 'IN_PROGRESS'
      };

      mockedApi.put.mockResolvedValueOnce({data: updatedTask});

      service.updateTask(taskId, updates).subscribe(task => {
        expect(task).toBeTruthy();
        expect(task.assigneeId).toBe(updates.assigneeId);
        done();
      });
    });
  });

  describe('inviteUser', () => {
    it('should invite user to project', (done) => {
      const projectId = '1';
      const email = 'test@example.com';
      const role = 'Member';
      const invitedBy = '1';

      mockedApi.post.mockResolvedValueOnce({data: {}});

      service.inviteUser(projectId, email, role, invitedBy).subscribe(() => {
        expect(mockedApi.post).toHaveBeenCalledWith('/projects/invite', {
          projectId,
          email,
          role,
          invitedById: invitedBy
        });
        done();
      });
    });
  });
});