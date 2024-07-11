import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-order-success',
  templateUrl: './order-success.component.html',
  styleUrls: ['./order-success.component.scss']
})

export class OrderSuccessComponent implements OnInit {
  orderInfo: string = '';
  paymentTime: string = '';
  transactionId: string = '';
  totalPrice: number | null = null;

  constructor(private route: ActivatedRoute) {}

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.orderInfo = params['orderInfo'];
      this.paymentTime = params['paymentTime'];
      this.transactionId = params['transactionId'];
      this.totalPrice = +params['totalPrice']; // Dấu '+' chuyển đổi string sang number
    });
  }
}
