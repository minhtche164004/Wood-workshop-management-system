import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  sidebarVisible: boolean = true;
  dropdownVisible: boolean = false;

  constructor(private router: Router) {}

  ngOnInit(): void {}

  toggleSidebar() {
    this.sidebarVisible = !this.sidebarVisible;
  }

  toggleDropdown(event: Event): void {
    event.preventDefault();
    this.dropdownVisible = !this.dropdownVisible;
  }

  onLogout(): void {
    const token = localStorage.getItem('loginToken');
    localStorage.removeItem('loginToken');
    this.router.navigateByUrl('/login');
  }
}
