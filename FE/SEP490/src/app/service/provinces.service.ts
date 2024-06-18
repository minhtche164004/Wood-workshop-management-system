import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ProvincesService {
  private provincesUrl = 'assets/data.json'; // Path to your JSON file

  constructor(private http: HttpClient) { }

  getProvinces(): Observable<any[]> {
    return this.http.get(this.provincesUrl, { responseType: 'text' }).pipe(
      map(response => {
        // Remove the wrapping 'data=' and parse the JSON
        const jsonString = response.replace(/^data='(.*)';$/, '$1');
        return JSON.parse(jsonString);
      })
    );
  }
  
}
