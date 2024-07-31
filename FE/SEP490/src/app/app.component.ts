import { Component } from '@angular/core'
import { Chart, registerables } from 'chart.js';
import { AuthGuard } from './AuthGuard';
import { ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs';
import { LoadingService } from './LoadingService';

Chart.register(...registerables)
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'SEP490';
  isLoading$ = this.loadingService.loading$;

  constructor(private loadingService: LoadingService) { }
  ngOnInit() {

  }
}
