import { Component, OnChanges, OnInit, inject } from '@angular/core';
import { Router } from '@angular/router';
import { TagCount } from 'src/app/models';
import { HttpService } from 'src/app/service/http.service';

@Component({
  selector: 'app-landingpage',
  templateUrl: './landingpage.component.html',
  styleUrls: ['./landingpage.component.css']
})
export class LandingpageComponent implements OnInit {

  private router = inject(Router)
  tags = []
  private service = inject(HttpService)
  duration: number;

  postNews(){
    this.router.navigate(['/post'])
  }

  ngOnInit (): void {
    let dur = new Date().getTime()
    this.service.getTagsByDuration(dur - (5*300000)).subscribe((response)=> {
      for (let t of response.tags){
        let tc : TagCount
        tc = t
        console.log(tc)
        this.tags.push(tc)
      }
    })
  }

  changeDuration(){
    this.tags = []
    this.service.getTagsByDuration(this.duration - (5 * 300000)).subscribe((response) => {
      for (let t of response.tags) {
        let tc: TagCount
        tc = t
        console.log(tc)
        this.tags.push(tc)
      }
    })
  }

}
