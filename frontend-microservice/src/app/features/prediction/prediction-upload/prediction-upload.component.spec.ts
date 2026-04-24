import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PredictionUploadComponent } from './prediction-upload.component';

describe('PredictionUploadComponent', () => {
  let component: PredictionUploadComponent;
  let fixture: ComponentFixture<PredictionUploadComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PredictionUploadComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PredictionUploadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
