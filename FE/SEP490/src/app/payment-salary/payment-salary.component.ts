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
export class PaymentSalaryComponent implements OnInit {
  transactionData: any; // Add this line

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    interval(5000) // Update every 5 seconds
      .pipe(
        switchMap(() => this.http.get(`${environment.apiUrl}api/auth/getTransaction`))
      )
      .subscribe((data: any) => {
        this.transactionData = data['transactionInfos'];
      });
      
  }

  getTransactions(): void {
    this.http.get(`${environment.apiUrl}api/auth/getTransaction`).subscribe((data: any) => {
      this.transactionData = data['transactionInfos'];
    });
  }

}



