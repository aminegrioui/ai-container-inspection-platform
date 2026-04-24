export interface DetectionBox {
  box: [number, number, number, number];
  box_padded: [number, number, number, number] | null;
  ergebnis: string;
  klasse: 'container' | 'text';
  ocr_conf: number | null;
  stufe_3_aktiv: boolean;
  yolo_conf: number;
}

export interface DetectionMetadata {
  datei: string;
  gesamtzeit_ms: number;
  s1_konfidenz: number;
}

export interface DetectionResult {
  metadata: DetectionMetadata;
  ergebnisse: DetectionBox[];
}

export interface PredictionResponse {
  id: number;
  imageName: string;
  status: 'PROCESSING' | 'COMPLETED' | 'ERROR';
  result: 'Positiv' | 'Negativ' | null;
  confidence: number | null;
  ocrText: string | null;
  detections: DetectionResult | null;
  durationMs: number | null;
  createdAt: string;
  respondedAt: string | null;
  newAnalyse: boolean;
}

export interface PredictionState {
  file: File | null;
  previewUrl: string | null;
  loading: boolean;
  elapsedMs: number;
  response: PredictionResponse | null;
  error: string | null;
}
