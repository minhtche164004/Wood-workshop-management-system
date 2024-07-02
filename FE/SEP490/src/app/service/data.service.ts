import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DataService {
  private searchKeySource = new BehaviorSubject<string | undefined>(undefined);
  currentSearchKey = this.searchKeySource.asObservable();

  constructor() { }

  changeSearchKey(searchKey: string | undefined) {
    this.searchKeySource.next(searchKey);
  }
}
