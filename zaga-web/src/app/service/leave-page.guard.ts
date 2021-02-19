import { Injectable } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material';
import { ActivatedRouteSnapshot, CanDeactivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Subject, TimeoutError } from 'rxjs';
import { Observable } from 'rxjs';
import { LeavePageConfirmationComponent } from '../components/leave-page-confirmation/leave-page-confirmation.component';

export interface CanComponentDeactivate {
  canDeactivate: () => Observable<boolean> | Promise<boolean> | boolean;
}

@Injectable({
  providedIn: 'root'
})
export class LeavePageGuard implements CanDeactivate<CanComponentDeactivate> {
  
  confirmDlg: MatDialogRef<LeavePageConfirmationComponent>;
  leave = false;

  constructor(
    private dialog: MatDialog,
    private router: Router
  ) {}

  canDeactivate(component: CanComponentDeactivate) {
    const subject = new Subject<boolean>();
    //return component.canDeactivate ? component.canDeactivate() : true;
    if(!this.leave){
      this.confirmDlg = this.dialog.open(LeavePageConfirmationComponent, { disableClose: true });
      this.confirmDlg.componentInstance.subject = subject;
      this.confirmDlg.afterClosed()
        .subscribe(async response => {
          console.log(response)
          console.log(subject.asObservable())
          if(this.leave){
            return true;
          }
          if (response) {
            return true;
          } else {
            return false;
          }
        });
        return subject.asObservable();
    } else {
      this.leave = false;
      return true;
    }
      
    }
  
}
