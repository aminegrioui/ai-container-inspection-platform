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
