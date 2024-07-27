import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard-admin',
  templateUrl: './dashboard-admin.component.html',
  styleUrls: ['./dashboard-admin.component.scss']
})
export class DashboardAdminComponent {
  constructor( private router: Router) { }
  status = false;
  addToggle()
  {
    this.status = !this.status;       
  }
  onLogout(): void {
    const token = localStorage.getItem('loginToken');
    localStorage.removeItem('loginToken');
    this.router.navigateByUrl('/login');
  }
}
