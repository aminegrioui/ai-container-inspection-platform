import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
//import { HistoryService } from '../../../core/services/history.service';
import { HistoryService } from '../../core/services/history.service';
import { HistoryItem, HistoryPage, StatusFilter } from '../../core/models/history.model';

@Component({
  selector: 'app-history',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.css']
})
export class HistoryComponent implements OnInit {

  items: HistoryItem[] = [];
  totalItems = 0;
  totalPages = 0;
  currentPage = 0;
  pageSize = 10;

  searchText = '';
  activeFilter: StatusFilter = 'ALL';
  filters: StatusFilter[] = ['ALL', 'COMPLETED', 'ERROR', 'PROCESSING'];

  loading = false;
  error: string | null = null;

  private searchTimeout: any;

  constructor(private historyService: HistoryService) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading = true;
    this.error = null;

    this.historyService.getHistory(
      this.currentPage,
      this.pageSize,
      this.activeFilter,
      this.searchText
    ).subscribe({
      next: (page: HistoryPage) => {
        this.items      = page.content;
        this.totalItems = page.totalItems;
        this.totalPages = page.totalPages;
        this.loading    = false;
      },
      error: () => {
        this.error   = 'History konnte nicht geladen werden.';
        this.loading = false;
      }
    });
  }

  onSearch(): void {
    clearTimeout(this.searchTimeout);
    this.searchTimeout = setTimeout(() => {
      this.currentPage = 0;
      this.load();
    }, 400); // debounce 400ms
  }

  onFilter(filter: StatusFilter): void {
    this.activeFilter = filter;
    this.currentPage  = 0;
    this.load();
  }

  goToPage(page: number): void {
    if (page < 0 || page >= this.totalPages) return;
    this.currentPage = page;
    this.load();
  }

  get pages(): number[] {
    const total = this.totalPages;
    const cur   = this.currentPage;
    // show max 5 page buttons centered around current
    const start = Math.max(0, Math.min(cur - 2, total - 5));
    const end   = Math.min(total, start + 5);
    return Array.from({ length: end - start }, (_, i) => start + i);
  }

  get rangeStart(): number { return this.currentPage * this.pageSize + 1; }
  get rangeEnd():   number { return Math.min((this.currentPage + 1) * this.pageSize, this.totalItems); }
}
