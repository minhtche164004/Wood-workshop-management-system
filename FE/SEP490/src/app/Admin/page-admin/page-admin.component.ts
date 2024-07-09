import { Component } from '@angular/core';
import 'jquery';
declare var $: any;
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
