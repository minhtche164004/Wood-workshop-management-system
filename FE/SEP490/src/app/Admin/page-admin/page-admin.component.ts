import { Component } from '@angular/core';

@Component({
  selector: 'app-page-admin',
  templateUrl: './page-admin.component.html',
  styleUrls: ['./page-admin.component.scss']
})
export class PageAdminComponent {
  status = false;
  addToggle()
  {
    this.status = !this.status;       
  }
  
}
