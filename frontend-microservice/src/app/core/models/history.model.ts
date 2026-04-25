export interface HistoryItem {
  id: string;
  imageName: string;
  status: 'PROCESSING' | 'COMPLETED' | 'ERROR';
  result: 'Positiv' | 'Negativ' | null;
  confidence: number | null;
  ocrText: string | null;
  ocrConfidence: number | null;
  durationMs: number | null;
  createdAt: string;
  respondedAt: string | null;
}

export interface HistoryPage {
  content: HistoryItem[];
  page: number;
  size: number;
  totalItems: number;
  totalPages: number;
  last: boolean;
}

export type StatusFilter = 'ALL' | 'COMPLETED' | 'ERROR' | 'PROCESSING';
