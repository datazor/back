import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { UserProfileComponent } from './user-profile.component';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { of } from 'rxjs';

describe('UserProfileComponent', () => {
  let component: UserProfileComponent;
  let fixture: ComponentFixture<UserProfileComponent>;
  let authService: jest.Mocked<AuthService>;
  let router: jest.Mocked<Router>;

  const mockUser = {
    id: '1',
    username: 'John Doe',
    email: 'john@example.com'
  };

  beforeEach(async () => {
    const authServiceMock = {
      currentUser$: of(mockUser),
      logout: jest.fn()
    };

    const routerMock = {
      navigate: jest.fn()
    };

    await TestBed.configureTestingModule({
      imports: [RouterTestingModule, UserProfileComponent],
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: Router, useValue: routerMock }
      ]
    }).compileComponents();

    authService = TestBed.inject(AuthService) as jest.Mocked<AuthService>;
    router = TestBed.inject(Router) as jest.Mocked<Router>;
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize with current user', () => {
    expect(component.currentUser).toEqual(mockUser);
  });

  it('should toggle dropdown', () => {
    expect(component.showDropdown).toBeFalsy();
    
    component.toggleDropdown();
    expect(component.showDropdown).toBeTruthy();
    
    component.toggleDropdown();
    expect(component.showDropdown).toBeFalsy();
  });

  it('should close dropdown', () => {
    component.showDropdown = true;
    component.closeDropdown();
    expect(component.showDropdown).toBeFalsy();
  });

  it('should handle logout', () => {
    component.logout();
    
    expect(authService.logout).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  });

  it('should get user initials', () => {
    expect(component.getUserInitials()).toBe('JD');
  });
});