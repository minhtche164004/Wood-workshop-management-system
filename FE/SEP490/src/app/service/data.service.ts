import { Injectable } from '@angular/core';
import { BehaviorSubject, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DataService {
  private searchKeySource = new BehaviorSubject<string | undefined>(undefined);

  private searchProductSubject = new Subject<any>();
  currentSearchKey = this.searchKeySource.asObservable();

  constructor() { }

  changeSearchKey(searchKey: string | undefined) {
    this.searchKeySource.next(searchKey);
  }

 
  clearSearchKey(): void {
    // this.currentSearchKey.next?(null); // Assuming currentSearchKey is a BehaviorSubject
  }

  triggerSearchProduct(searchParams: any) {
    this.searchProductSubject.next(searchParams);
  }

  getSearchProductTrigger() {
    return this.searchProductSubject.asObservable();
  }
}
