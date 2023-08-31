import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LandingpageComponent } from './components/landingpage/landingpage.component';
import { ArticlepostingComponent } from './components/articleposting/articleposting.component';

const routes: Routes = [
  {path: '', component: LandingpageComponent},
  {path: 'post', component: ArticlepostingComponent},
  {path:'**', redirectTo:'/', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
