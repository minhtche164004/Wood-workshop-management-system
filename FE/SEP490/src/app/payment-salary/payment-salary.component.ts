import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { interval } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { environment } from 'src/app/environments/environment'; // Đường dẫn đúng tới file môi trường
@Component({
  selector: 'app-payment-salary',
  templateUrl: './payment-salary.component.html',
  styleUrls: ['./payment-salary.component.scss']
})
export class PaymentSalaryComponent  {
  input1: string = ''; // Initialize input1
  input2: string = ''; // Initialize input1
  input3: string = ''; // Initialize input1
  input4: string = ''; // Initialize input1
  
  constructor() { }
}



