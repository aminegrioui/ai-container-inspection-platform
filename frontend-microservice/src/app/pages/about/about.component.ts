import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-about',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './about.component.html',
  styleUrls: ['./about.component.css']
})
export class AboutComponent implements OnInit, OnDestroy {

  titleLines = ['Fullstack Developer', 'Spring Boot · Angular', 'KI Enthusiast'];
  displayedTitle = '';

  private charIndex  = 0;
  private lineIndex  = 0;
  private isDeleting = false;
  private timer: any;

  ngOnInit(): void { this.tick(); }

  ngOnDestroy(): void { clearTimeout(this.timer); }

  private tick(): void {
    const cur = this.titleLines[this.lineIndex];
    if (!this.isDeleting) {
      this.displayedTitle = cur.substring(0, ++this.charIndex);
      if (this.charIndex === cur.length) {
        this.timer = setTimeout(() => { this.isDeleting = true; this.tick(); }, 1800);
        return;
      }
    } else {
      this.displayedTitle = cur.substring(0, --this.charIndex);
      if (this.charIndex === 0) {
        this.isDeleting = false;
        this.lineIndex  = (this.lineIndex + 1) % this.titleLines.length;
      }
    }
    this.timer = setTimeout(() => this.tick(), this.isDeleting ? 45 : 85);
  }
}
