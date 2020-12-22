import { Component, OnInit } from "@angular/core";
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.css"],
})
export class AppComponent implements OnInit {
  title = "zaga-web";
  loginFlag: boolean = false;

  public constructor() {

  }
  ngOnInit(): void {
    if (window.sessionStorage.getItem('AuthToken') === null) {
      this.loginFlag = true;
    }
  }
}
