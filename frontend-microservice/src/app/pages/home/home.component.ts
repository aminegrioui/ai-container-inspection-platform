/*
import { Component } from '@angular/core';
import { PredictionUploadComponent } from '../../features/prediction/prediction-upload/prediction-upload.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [PredictionUploadComponent],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {}
*/

import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PredictionUploadComponent } from '../../features/prediction/prediction-upload/prediction-upload.component';
import { HistoryComponent } from '../../features/history/history.component';

type Tab = 'analyse' | 'history';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, PredictionUploadComponent, HistoryComponent],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  activeTab: Tab = 'analyse';

  setTab(tab: Tab): void {
    this.activeTab = tab;
  }
}
