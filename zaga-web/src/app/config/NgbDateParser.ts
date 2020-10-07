import { Injectable } from "@angular/core";
import { MatMonthView, yearsPerPage } from "@angular/material";
import {
  NgbDateParserFormatter,
  NgbDateStruct,
} from "@ng-bootstrap/ng-bootstrap";

function toInteger(value: any): number {
  return parseInt(`${value}`, 10);
}

@Injectable()
export class NgbDateParser extends NgbDateParserFormatter {
  parse(value: string): NgbDateStruct {
    console.log(value);
    const dateParts = value.split("/");
    return {
      year: toInteger(dateParts[2]),
      month: toInteger(dateParts[0]),
      day: toInteger(dateParts[1]),
    };
  }
  format(date: NgbDateStruct): string {
    return date.day + "." + date.month + "." + date.year + ".";
  }
}
