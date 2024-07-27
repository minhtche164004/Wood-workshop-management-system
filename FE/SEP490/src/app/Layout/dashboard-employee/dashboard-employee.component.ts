import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard-employee',
  templateUrl: './dashboard-employee.component.html',
  styleUrls: ['./dashboard-employee.component.scss']
})
export class DashboardEmployeeComponent {
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
