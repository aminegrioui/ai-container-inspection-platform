import { Component, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PredictionService } from '../../../core/services/prediction.service';
import { PredictionResponse, PredictionState } from '../../../core/models/prediction.model';

@Component({
  selector: 'app-prediction-upload',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './prediction-upload.component.html',
  styleUrls: ['./prediction-upload.component.css']
})
export class PredictionUploadComponent implements OnDestroy {

  state: PredictionState = {
    file: null,
    previewUrl: null,
    loading: false,
    elapsedMs: 0,
    response: null,
    error: null
  };

  private timerInterval: any;
  private startTime = 0;

  constructor(private predictionService: PredictionService) {}

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    if (!file) return;

    if (this.state.previewUrl) URL.revokeObjectURL(this.state.previewUrl);

    this.state = {
      ...this.state,
      file,
      previewUrl: URL.createObjectURL(file),
      response: null,
      error: null,
      elapsedMs: 0
    };
  }

  onSend(): void {
    if (!this.state.file) return;

    this.state = { ...this.state, loading: true, response: null, error: null, elapsedMs: 0 };
    this.startTimer();

    if (!this.state.file) {
      this.state = {
        ...this.state,
        error: 'Bitte zuerst eine Datei auswählen'
      };
      return;
    }
    this.predictionService.predict(this.state.file).subscribe({
      next: (response: PredictionResponse) => {
        this.stopTimer();
        this.state = { ...this.state, loading: false, response };
      },
      error: (err) => {
        this.stopTimer();
        this.state = {
          ...this.state,
          loading: false,
          error: err?.error?.message ?? 'Verbindungsfehler — Backend nicht erreichbar'
        };
      }
    });
  }

  onReset(): void {
    this.stopTimer();
    if (this.state.previewUrl) URL.revokeObjectURL(this.state.previewUrl);
    this.state = { file: null, previewUrl: null, loading: false, elapsedMs: 0, response: null, error: null };
  }

  getOcrText(): string {
    return this.state.response?.ocrText ?? '—';
  }

  getOcrConfidence(): number {
    const ocr = this.state.response?.detections?.ergebnisse?.find(e => e.stufe_3_aktiv && e.ocr_conf != null);
    return ocr?.ocr_conf ?? 0;
  }

  getYoloCount(): number {
    return this.state.response?.detections?.ergebnisse?.length ?? 0;
  }

  getElapsedSeconds(): string {
    return (this.state.elapsedMs / 1000).toFixed(1);
  }

  getConfidencePercent(): number {
    return Math.round((this.state.response?.confidence ?? 0) * 100);
  }

  private startTimer(): void {
    this.startTime = Date.now();
    this.timerInterval = setInterval(() => {
      this.state = { ...this.state, elapsedMs: Date.now() - this.startTime };
    }, 100);
  }

  private stopTimer(): void {
    if (this.timerInterval) { clearInterval(this.timerInterval); this.timerInterval = null; }
  }

  ngOnDestroy(): void {
    this.stopTimer();
    if (this.state.previewUrl) URL.revokeObjectURL(this.state.previewUrl);
  }
}
