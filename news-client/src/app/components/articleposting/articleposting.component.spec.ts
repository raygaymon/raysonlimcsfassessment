import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ArticlepostingComponent } from './articleposting.component';

describe('ArticlepostingComponent', () => {
  let component: ArticlepostingComponent;
  let fixture: ComponentFixture<ArticlepostingComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ArticlepostingComponent]
    });
    fixture = TestBed.createComponent(ArticlepostingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
