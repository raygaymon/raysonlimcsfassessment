import { HttpClient } from '@angular/common/http';
import { Component, ElementRef, ViewChild, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { firstValueFrom, windowWhen } from 'rxjs';
import { News } from 'src/app/models';
import { HttpService } from 'src/app/service/http.service';

@Component({
  selector: 'app-articleposting',
  templateUrl: './articleposting.component.html',
  styleUrls: ['./articleposting.component.css']
})
export class ArticlepostingComponent {

  @ViewChild('file') imageFile: ElementRef;
  form1: FormGroup;
  constructor(private http: HttpClient, private fb: FormBuilder) { }
  private service = inject(HttpService)
  private router = inject(Router)

  images: [] = []
  hashTags: string[] = []
  n: News

  ngOnInit(): void {

    this.form1 = this.fb.group({
      'title': this.fb.control<string>('', [Validators.required, Validators.minLength(5)]),
      'description': this.fb.control<string>('', [Validators.required, Validators.minLength(5)]),
      'tags': this.fb.control<string>('')
    })
  }

  addHashtag(){
    let tags = this.form1.get('tags').value
    let inputTags = tags.split(' ')
    for (let w of inputTags){
      this.hashTags.push(w)
      console.log(w + " added")
    }
    console.log(this.hashTags)
  }

  deleteHashTag(tag: string){
    let i = this.hashTags.indexOf(tag, 0)
    console.log(this.hashTags[i] + ' removed')
    this.hashTags.splice(i , 1)
    for (let w of this.hashTags){
      console.log(w)
    }
    
  }

  setFormValueToNews(title:string, desc: string, tags: string[], url:string){
    this.n = {
      title: title,
      postDate: new Date().getTime(),
      description: desc,
      tags: tags,
      imageUrl: url
    }
  }

  upload() {
    //this creates a multipart form
    const formData = new FormData();
    formData.set('name', this.form1.get('title').value);
    formData.set('file', this.imageFile.nativeElement.files[0]);
    this.service.postPicture(formData)
    console.log(formData.get('file'))
  }

  submit(){
    this.setFormValueToNews(this.form1.get('title').value, this.form1.get('description').value, this.hashTags, this.imageFile.nativeElement.files[0].name)
    console.log(this.n)
    this.service.postNews(this.n)
    this.upload()
  }

  goBack(){
    this.router.navigate([''])
  }
}
