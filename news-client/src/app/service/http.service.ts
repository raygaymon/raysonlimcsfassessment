import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { News } from '../models';

@Injectable({
  providedIn: 'root'
})
export class HttpService {

  constructor(private http: HttpClient) { }
  

  postNews(news: News){
    this.http.post<any>('/post', news, {
      observe: 'response'
    }).subscribe(response => {
      console.log(news)
      return "News article posted successfullly";
    }) 
  }

  postPicture(formData: FormData){
    firstValueFrom(
      this.http.post<any>("/uploadPic", formData)).then(() => {
        console.log("upload sucessful")
      }).catch((error) => {
        console.log(error)
      })
  }

  getTagsByDuration(dur: number){
    return this.http.get<any>("/tags", {
      params: {"time": dur}
    })
  }
}
