import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-leave-page-confirmation',
  templateUrl: './leave-page-confirmation.component.html',
  styleUrls: ['./leave-page-confirmation.component.css']
})
export class LeavePageConfirmationComponent implements OnInit {

  constructor(
    private dialogRef: MatDialogRef<LeavePageConfirmationComponent>
  ) { }

  subject: Subject<boolean>;

  ngOnInit() {
  }

  onYesResponse() {
    if (this.subject) {
      this.subject.next(true);
      this.subject.complete();
    }
    this.dialogRef.close(true);
  }

  onNoResponse() {
    if (this.subject) {
      this.subject.next(false);
      this.subject.complete();
    }
    this.dialogRef.close(false);
  }

}
