import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PredictionResponse } from '../models/prediction.model';
import { environment } from '../../../environments/environment.local';

@Injectable({
  providedIn: 'root'
})
export class PredictionService {

  private readonly API_URL = `${environment.apiUrl}/upload`;


  constructor(private http: HttpClient) {}

  predict(file: File): Observable<PredictionResponse> {
    const form = new FormData();
    form.append('file', file);
    return this.http.post<PredictionResponse>(this.API_URL, form);
  }

}
