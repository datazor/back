import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { NotificationService } from '../../services/notification.service';
import { ClickOutsideDirective } from '../../directives/click-outside.directive';
import {ProjectInvitation} from "./notification.model";
import {User} from "../../models/user.model";
import {AuthService} from "../../services/auth.service";

@Component({
    selector: 'app-notification',
    standalone: true,
    imports: [CommonModule, ClickOutsideDirective],
    templateUrl: './notification.component.html',
    styleUrls: ['./notification.component.css']
})
export class NotificationComponent implements OnInit, OnDestroy {
    invitations: ProjectInvitation[] = [];
    showDropdown = false;
    currentUser: User | null = null;

    constructor(
        private notificationService: NotificationService,
        private router: Router,
        private authService: AuthService,
    ) {}

    ngOnInit(): void {
        this.notificationService.getInvitations().subscribe(invitations => {
            this.invitations = invitations;
        });
        this.authService.currentUser$.subscribe(user => {
            this.currentUser = user;
        })
    }

    ngOnDestroy(): void {
        this.notificationService.disconnect();
    }

    toggleDropdown(): void {
        this.showDropdown = !this.showDropdown;
    }

    closeDropdown(): void {
        this.showDropdown = false;
    }

    async acceptInvitation(invitation: ProjectInvitation, event: Event): Promise<void> {
        event.stopPropagation();
        try {
            await this.notificationService.respondToInvitation(invitation.projectId, true, this.currentUser?.username);
            this.router.navigate(['/projects', invitation.projectId]);
        } catch (error) {
            console.error('Error accepting invitation:', error);
        }
    }

    async declineInvitation(invitation: ProjectInvitation, event: Event): Promise<void> {
        event.stopPropagation();
        try {
            await this.notificationService.respondToInvitation(invitation.projectId, false, this.currentUser?.username);
        } catch (error) {
            console.error('Error declining invitation:', error);
        }
    }
}