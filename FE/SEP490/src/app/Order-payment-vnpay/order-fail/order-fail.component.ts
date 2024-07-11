import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-order-fail',
  templateUrl: './order-fail.component.html',
  styleUrls: ['./order-fail.component.scss']
})
export class OrderFailComponent {
  orderInfo: string = '';
  paymentTime: string = '';
  transactionId: string = '';
  totalPrice: number | null = null;

  constructor(private route: ActivatedRoute, private datePipe: DatePipe) {}

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.orderInfo = params['orderInfo'];
      this.paymentTime = params['paymentTime'];
      this.transactionId = params['transactionId'];
      this.totalPrice = +params['totalPrice']; // Dấu '+' chuyển đổi string sang number
    });
  }
}
