import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import { AuthService } from './auth.service';
import { ProjectInvitation } from '../components/notification/notification.model';
import { User } from '../models/user.model';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import axios from "../core/api/axios";

@Injectable({
    providedIn: 'root',
})
export class NotificationService {
    private socketClient: any = null;
    private invitations = new BehaviorSubject<ProjectInvitation[]>([]);
    private currentUser: User | null = null;

    constructor(private authService: AuthService) {
        this.authService.currentUser$.subscribe((user) => {
            this.currentUser = user;
            if (this.currentUser) {
                // /user/userid/notifications
                this.initializeSocket(this.currentUser.username);
            }
        });
    }

    private initializeSocket(username: string): void {
        let ws =  new SockJS('https://kanban-back.lemrabott-abdelaziz.fr/ws');
        this.socketClient = Stomp.over(ws);

        this.socketClient.connect(
            {
                Authorization: `Bearer ${localStorage.getItem('token')}`,
            },
            () => {
                console.log('Connected to WebSocket');
                this.subscribeToNotifications(username);
            },
            (error: any) => {
                console.error('WebSocket connection error:', error);
            }
        );
    }

    private subscribeToNotifications(username: string): void {
        if (!this.socketClient) return;

        const destination = `/user/${username}/notification`;
        this.socketClient.subscribe(destination, (message: { body: string; }) => {
            const invitation: ProjectInvitation = JSON.parse(message.body);
            console.log('reveived', invitation)
            this.handleNewInvitation(invitation);
        });
    }

    private handleNewInvitation(invitation: ProjectInvitation): void {
        const current = this.invitations.value;
        this.invitations.next([invitation, ...current]);
        console.log('invitation', this.invitations.asObservable());
        this.showBrowserNotification(invitation);
    }

    private async showBrowserNotification(invitation: ProjectInvitation): Promise<void> {
        if ('Notification' in window && Notification.permission === 'granted') {
            new Notification('New Project Invitation', {
                body: `${invitation.invitedBy} has invited you to join project ${invitation.projectName}`,
                icon: '/assets/icons/notification.png',
            });
        }
    }

    getInvitations(): Observable<ProjectInvitation[]> {
        return this.invitations.asObservable();
    }

    async respondToInvitation(projectId: string, accept: boolean, username: string | undefined): Promise<void> {
        try {

            if (username) {
                console.log('call made')
                console.log({projectId: projectId, accept:accept, username: username})
                await axios.post('/projects/invite/respond', {projectId: projectId, accept:accept, username: username}).then(
                    res => res.data
                )
            }

            const current = this.invitations.value;
            const updated = current.filter(inv => inv.projectId !== projectId);
            this.invitations.next(updated);
        } catch (error) {
            console.error('Error responding to invitation:', error);
            throw error;
        }
    }

    disconnect(): void {
        if (this.socketClient) {
            this.socketClient.disconnect(() => {
                console.log('WebSocket disconnected');
            });
        }
    }
}
