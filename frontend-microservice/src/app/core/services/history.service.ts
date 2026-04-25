import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { HistoryPage, StatusFilter } from '../models/history.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class HistoryService {

  private readonly API = `${environment.apiUrl}/history`;

  constructor(private http: HttpClient) {}

  getHistory(
    page: number = 0,
    size: number = 10,
    status?: StatusFilter,
    ocrText?: string
  ): Observable<HistoryPage> {
    let params = new HttpParams()
      .set('page', page)
      .set('size', size);

    if (status && status !== 'ALL') params = params.set('status', status);
    if (ocrText?.trim())            params = params.set('ocrText', ocrText.trim());

    return this.http.get<HistoryPage>(this.API, { params });
  }
}

