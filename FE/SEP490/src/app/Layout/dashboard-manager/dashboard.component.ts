import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import 'jquery';

declare var $: any; // khai bao jquery

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit{
  constructor( private router: Router) { }
  ngOnInit(): void {
    $('[data-dismiss="modal"]').click();        
  }
  status = false;
  addToggle()
  {
    this.status = !this.status;       
  }
  sidebarVisible: boolean = true;

  toggleSidebar() {
    this.sidebarVisible = !this.sidebarVisible;
  }
  onLogout(): void {
    const token = localStorage.getItem('loginToken');
    localStorage.removeItem('loginToken');
    this.router.navigateByUrl('/login');
  }
}
