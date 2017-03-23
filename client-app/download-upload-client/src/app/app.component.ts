import {Component, OnInit, OnChanges} from "@angular/core";
import {HttpService} from "./http.service";
import {Body} from "@angular/http/src/body";
import {Headers, RequestOptions, Http} from "@angular/http";
import {Observable} from "rxjs";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
})
export class AppComponent implements OnInit, OnChanges {

  allFiles = [];
  apiEndPoint: string = 'http://localhost:8080/';
  fileList: FileList;

  constructor(private httpService: HttpService, private http: Http) {

  }


  ngOnInit() {
    this.httpService.getData()
      .subscribe(
        (data: Body) => this.allFiles = data.json()
      );
  }

  fileChange(event) {
    this.fileList = event.target.files;

  }

  onSubmit(){
    if (this.fileList.length > 0) {
      let file: File = this.fileList[0];
      let formData: FormData = new FormData();
      formData.append('file', file, file.name);
      let headers = new Headers();
      headers.append('Accept', 'application/json');
      let options = new RequestOptions({headers: headers});
      this.http.post(this.apiEndPoint, formData, options)
        .map(res => res.json())
        .catch(error => Observable.throw(error))
        .subscribe(
          data => console.log('success'),
          error => console.log(error)
        )
    }
  }

  ngOnChanges(){
    this.httpService.getData()
      .subscribe(
        (data: Body) => this.allFiles = data.json()
      );
  }

}
