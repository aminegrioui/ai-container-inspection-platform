import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment.local';

@Injectable({
  providedIn: 'root'
})
export class PredictionService {

  private readonly API_URL = `${environment.apiUrl}/history`;


  constructor(private http: HttpClient) {}


}
