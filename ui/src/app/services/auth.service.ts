import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, from, throwError } from 'rxjs';
import { tap, catchError, map } from 'rxjs/operators';
import { User, AuthResponse } from '../models/user.model';
import axios from "../core/api/axios";
import {LoginRequest} from "../components/login/login.model";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  currentUser$ = this.currentUserSubject.asObservable();

  constructor(private router: Router) {
    const token = localStorage.getItem('token');
    if (token) {
      this.loadUser();
    }
  }

  login(email: string, password: string): Observable<AuthResponse> {
    const loginRequest: LoginRequest = {
    email: email,
    password: password
  };

  return from(axios.post('/user/login', loginRequest)).pipe(
      map(res => res as unknown as AuthResponse),
      tap(res => this.handleAuthentication(res)),
      catchError(error => {
        console.error('Login error:', error);
        return throwError(() => new Error('Invalid credentials'));
      })
  );
  }

  register(user: User): Observable<AuthResponse> {
        return from(axios.post('/user/register', user)).pipe(
          map(res => res as unknown as AuthResponse),
          tap(() => this.router.navigate(['/login'])),
          catchError(error => {
            console.error('Login error:', error);
            return throwError(() => new Error('Invalid credentials'));
          })
        );
    }

  private handleAuthentication(response: AuthResponse): void {
    console.log(response);
    localStorage.setItem('token', response.token);
    localStorage.setItem('email', response.user.email);
    localStorage.setItem('username', response.user.username);
    this.currentUserSubject.next(response.user);
  }

  private loadUser(): void {
    const user: User = {
      email: localStorage.getItem('email') as string,
      username: localStorage.getItem('username') as string,
    };
    this.currentUserSubject.next(user);
  }

  getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('email');
    localStorage.removeItem('username');
    this.currentUserSubject.next(null);
  }

  isAuthenticated(): boolean {
    return !!localStorage.getItem('token');
  }
}