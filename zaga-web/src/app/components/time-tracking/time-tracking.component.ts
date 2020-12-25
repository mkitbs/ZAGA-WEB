import { Component, OnInit, ViewChild } from '@angular/core';
import { FormControl } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { CdTimerComponent } from 'angular-cd-timer';
import { TimeTracking } from 'src/app/models/TimeTracking';
import { WorkerTimeTracking } from 'src/app/models/WorkerTimeTracking';
import { WorkerTimeTrackingService } from 'src/app/service/worker-time-tracking.service';

@Component({
  selector: 'app-time-tracking',
  templateUrl: './time-tracking.component.html',
  styleUrls: ['./time-tracking.component.css']
})


export class TimeTrackingComponent implements OnInit {
  @ViewChild('basicTimer', null) basicTimer;
  startTime = 0;
  startFlag = false;
  pauseFlag = false;
  continueFlag = false;
  endFlag = false;
  status = "Nije započet"
  isTicking = false;
  id;
  pause = new FormControl('');
  workerTimeTracking: WorkerTimeTracking = new WorkerTimeTracking();
  constructor(private workerTimeTrackingService: WorkerTimeTrackingService,
    private activatedRoute: ActivatedRoute) { }

  ngOnInit() {
    this.id = this.activatedRoute.snapshot.params.id;
    this.workerTimeTrackingService.getWorkerTask(this.id).subscribe(data => {
      this.workerTimeTracking = data;
      if (this.workerTimeTracking.headerInfo.wowStatus == "NOT_STARTED") { //ako bude nezapoceto
        this.startFlag = true;
        this.isTicking = true;
        this.startTime = 300;
        this.basicTimer.start();
      } else if (this.workerTimeTracking.headerInfo.wowStatus == "STARTED") { //ako bude u toku
        this.pauseFlag = true;
        this.endFlag = true;
        this.isTicking = true;
      } else if (this.workerTimeTracking.headerInfo.wowStatus == "PAUSED") {

      } else if (this.workerTimeTracking.headerInfo.wowStatus == "FINISHED") {

      }
    })

  }

  startTimer() {
    var timeTracking: TimeTracking = new TimeTracking();
    timeTracking.startTime = new Date();
    timeTracking.wowId = this.id;
    timeTracking.type = "RN";
    timeTracking.id = "";
    this.workerTimeTrackingService.setTracking(timeTracking).subscribe(data => {
      this.isTicking = true;
      this.basicTimer.start();
      this.pauseFlag = true;
      this.status = "U radu"
      this.startFlag = false;
      this.endFlag = true;

    })

  }

  pauseTimer() {
    this.basicTimer.start();
    console.log(this.basicTimer.get())
    this.pauseFlag = false;
    this.continueFlag = true;
    this.endFlag = false;
    this.status = "Pauza";
    console.log(this.pause.value)
  }

  continueTimer() {
    this.endFlag = true;
    this.pauseFlag = true;
    this.continueFlag = false;
    this.status = "U radu"
  }

  stopTimer() {
    this.basicTimer.reset();
  }


}
