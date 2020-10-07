import { Component } from "@angular/core";
import {
  NgbDatepickerConfig,
  NgbDateParserFormatter,
} from "@ng-bootstrap/ng-bootstrap";
import { NgbDateParser } from "./config/NgbDateParser";

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.css"],
  providers: [{ provide: NgbDateParserFormatter, useClass: NgbDateParser }],
})
export class AppComponent {
  title = "zaga-web";
}
