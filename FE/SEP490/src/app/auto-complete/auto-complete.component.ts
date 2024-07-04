import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { ProductListService } from 'src/app/service/product/product-list.service';
import { Observable, debounceTime, distinctUntilChanged, map, startWith, switchMap } from 'rxjs';
import {AutocompleteLibModule} from 'angular-ng-autocomplete';
// interface Products {
//   productId: number;
//   productName: string;
//   description: string;
//   quantity: number;
//   price: number;
//   image: string;
//   completionTime: string;
//   enddateWarranty: string;
//   code: string;
//   type: number;
//   status: {
//     status_id: number;
//     status_name: string;
//     type: number;
//   };
//   categories: {
//     categoryId: number;
//     categoryName: string;
//   };
// }
@Component({
  selector: 'app-auto-complete',
  templateUrl: './auto-complete.component.html',
  styleUrls: ['./auto-complete.component.scss']
})
export class AutoCompleteComponent implements OnInit {
  keyword = 'productName';
  products: any[] = [];

  constructor(private productListService: ProductListService, ) { }

  ngOnInit(): void {
    this.productListService.getAllProductCustomer().subscribe(
      (data) => {
        if (data.code === 1000) {
          this.products = data.result;
          console.log('Danh sách sản phẩm:', this.products);
        } else {
          console.error('Failed to fetch products:', data);
         
        }
      },
      (error) => {
        console.error('Error fetching products:', error);
   
      }
    );

   
  } 
    selectEvent(item: any) {
      
  }

  onChangeSearch(search: string) {

  }

  onFocused(e: any) {
  }
}
