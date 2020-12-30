import { Component, OnInit, ViewChild } from '@angular/core';
import { FormControl } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CdTimerComponent } from 'angular-cd-timer';
import { Observable, interval } from 'rxjs';
import { ResponseTimeTracking } from 'src/app/models/ResponseTimeTracking';
import { TimeTracking } from 'src/app/models/TimeTracking';
import { WorkerTimeTracking } from 'src/app/models/WorkerTimeTracking';
import { WorkerTimeTrackingService } from 'src/app/service/worker-time-tracking.service';
import { resolve } from 'url';

@Component({
  selector: 'app-time-tracking',
  templateUrl: './time-tracking.component.html',
  styleUrls: ['./time-tracking.component.css']
})


export class TimeTrackingComponent implements OnInit {
  @ViewChild('basicTimer', null) basicTimer;

  constructor(private workerTimeTrackingService: WorkerTimeTrackingService,
    private activatedRoute: ActivatedRoute, private router: Router) {
    //    this.sub = interval(10000).subscribe((val) => {
    //      console.log('called'); 
    //    });
  }

  startTime;
  startFlag = false;
  pauseFlag = false;
  continueFlag = false;
  backFlag = false;
  endFlag = false;
  status = "Nije započet"
  isTicking = false;
  id;
  pause = new FormControl('');
  workerTimeTracking: WorkerTimeTracking = new WorkerTimeTracking();
  responseTimeTracking: ResponseTimeTracking = new ResponseTimeTracking();
  timeTrackingId;
  rnId;
  rnErpId;
  pauseId;
  pauseErpId;
  pauseType;
  sub;

  ngOnInit() {
    this.id = this.activatedRoute.snapshot.params.id;
    this.workerTimeTrackingService.getWorkerTask(this.id).subscribe(data => {
      this.workerTimeTracking = data;
      console.log(this.workerTimeTracking)
      if (this.workerTimeTracking.headerInfo.wowStatus == "NOT_STARTED") { //ako bude nezapoceto
        this.startFlag = true;
        this.isTicking = false;
        //this.startTime = 300;
        //this.basicTimer.start();
      } else if (this.workerTimeTracking.headerInfo.wowStatus == "STARTED") { //ako bude u toku
        var inProgress = this.workerTimeTracking.times.findIndex(x => x.type === "RN");
        var now: Date = new Date();
        var started: Date = new Date(this.workerTimeTracking.times[inProgress].startTime);

        var razlika = (now.getTime() - started.getTime()) / 1000;
        var saPaz = this.getSeconds(this.workerTimeTracking.times);
        console.log(razlika + "STARO")
        console.log(saPaz + "ODUZETE PAUZE");
        this.pauseFlag = true;
        this.endFlag = true;
        this.basicTimer.startTime = saPaz;
        this.status = "U radu"
        this.isTicking = true;
        this.basicTimer.start();
      } else if (this.workerTimeTracking.headerInfo.wowStatus == "PAUSED") {
        var paused = this.workerTimeTracking.times.findIndex(x => (x.type === "PAUSE_FUEL"
          || x.type === "PAUSE_WORK"
          || x.type === "PAUSE_SERVICE") && x.endTime === null);
        let pauseType = this.workerTimeTracking.times[paused].type;
        console.log(this.workerTimeTracking.times)
        console.log(pauseType)
        var pauseStarted = new Date(this.workerTimeTracking.times[paused].startTime);
        console.log(pauseStarted)
        var now: Date = new Date();
        var razlika = (now.getTime() - pauseStarted.getTime()) / 1000;
        this.continueFlag = true;
        this.isTicking = true;
        if (pauseType === "PAUSE_FUEL") {
          this.status = "Pauza za gorivo";
        } else if (pauseType === "PAUSE_SERVICE") {
          this.status = "Servisna pauza";
        } else if (pauseType === "PAUSE_WORK") {
          this.status = "Pauza - odmor";
        }

        this.basicTimer.startTime = razlika;
        this.basicTimer.start();
      } else if (this.workerTimeTracking.headerInfo.wowStatus == "FINISHED") {
        this.status = "Završen"
      }
    })
    this.seePositon();
    console.log(new Date())
  }



  startTimer() {
    var timeTracking: TimeTracking = new TimeTracking();
    timeTracking.startTime = new Date();
    timeTracking.wowId = this.id;
    timeTracking.type = "RN";
    timeTracking.id = "";
    this.workerTimeTrackingService.setTracking(timeTracking).subscribe(data => {
      this.workerTimeTracking.times = data;
      //this.rnId = this.responseTimeTracking.dbId;
      this.isTicking = true;
      this.backFlag = false;
      this.basicTimer.start();
      this.pauseFlag = true;
      this.status = "U radu"
      this.startFlag = false;
      this.endFlag = true;

    })

  }

  pauseTimer() {
    var timeTracking: TimeTracking = new TimeTracking();
    timeTracking.startTime = new Date();
    timeTracking.wowId = this.id;
    timeTracking.type = this.pause.value;
    timeTracking.id = "";
    this.workerTimeTrackingService.setTracking(timeTracking).subscribe(data => {
      //this.timeTrackingId = data;
      this.workerTimeTracking.times = data;
      //this.responseTimeTracking = data;
      //this.pauseId = this.responseTimeTracking.dbId;
      //this.pauseErpId = this.responseTimeTracking.erpId;
      this.pauseFlag = false;
      this.continueFlag = true;
      this.endFlag = false;
      if (this.pause.value === "PAUSE_FUEL") {
        this.status = "Pauza za gorivo";
      } else if (this.pause.value === "PAUSE_SERVICE") {
        this.status = "Servisna pauza";
      } else if (this.pause.value === "PAUSE_WORK") {
        this.status = "Pauza - odmor";
      }
      this.basicTimer.reset();
      this.basicTimer.startTime = 0;
      this.basicTimer.start();
      console.log(this.pause.value)
    })
  }

  continueTimer() {
    //this.basicTimer.start();
    console.log(this.basicTimer.get())
    var timeTracking: TimeTracking = new TimeTracking();
    //timeTracking.startTime = new Date();
    timeTracking.wowId = this.id;
    let index = this.workerTimeTracking.times.findIndex(x => (x.type === "PAUSE_FUEL"
      || x.type === "PAUSE_WORK"
      || x.type === "PAUSE_SERVICE") && x.endTime === null);
    console.log(this.workerTimeTracking.times)
    console.log(index)
    timeTracking.type = this.workerTimeTracking.times[index].type;
    timeTracking.id = this.workerTimeTracking.times[index].id;
    timeTracking.endTime = new Date();
    this.workerTimeTrackingService.updateTracking(timeTracking).subscribe(data => {
      this.workerTimeTracking.times = data;
      var razlika = this.getSeconds(data);
      this.endFlag = true;
      this.pauseFlag = true;
      this.continueFlag = false;
      this.status = "U radu"
      this.basicTimer.reset();
      this.basicTimer.startTime = razlika;
      this.basicTimer.start();
    })
  }

  stopTimer() {
    this.basicTimer.reset();
  }

  endTimer() {
    this.basicTimer.stop();
    var timeTracking: TimeTracking = new TimeTracking();
    //timeTracking.startTime = new Date();
    timeTracking.wowId = this.id;
    timeTracking.type = "FINISHED";
    let index = this.workerTimeTracking.times.findIndex(x => x.type === "RN");
    timeTracking.id = this.workerTimeTracking.times[index].id;
    timeTracking.endTime = new Date();
    this.workerTimeTrackingService.updateTracking(timeTracking).subscribe(data => {
      this.endFlag = false;
      this.pauseFlag = false;
      this.continueFlag = false;
      this.startFlag = false;
      this.status = "Završeno"
      this.backFlag = true;
    })
  }

  getPause(pause) {
    this.pause.setValue(pause);
    if (this.pause.value == "PAUSE_WORK") {
      this.pauseType = "Odmor"
    } else if (this.pause.value == "PAUSE_SERVICE") {
      this.pauseType = "Servis";
    } else {
      this.pauseType = "Sipanje goriva"
    }
    console.log(this.pause.value)
  }

  setPuase() {
    this.pause = new FormControl("");
  }

  goBack() {
    this.router.navigateByUrl("/workOrderTractorDriver")
  }

  getPosition(): Promise<any> {
    return new Promise((resolve, reject) => {
      navigator.geolocation.getCurrentPosition(resp => {
        resolve({ lng: resp.coords.longitude, lat: resp.coords.latitude });
      }, error => {
        reject(error);
      })
    })
  }

  seePositon() {
    this.getPosition().then(pos => {
      console.log("Positon: " + pos.lat + "-" + pos.lng)
    })
  }

  getSeconds(times: TimeTracking[]) {
    var now: Date = new Date();
    console.log(times)
    var started = times.findIndex(x => x.type === "RN");
    var startedTime = new Date(times[started].startTime);
    var razlika = (now.getTime() - startedTime.getTime()) / 1000;
    times.forEach(el => {
      if (el.type !== "RN" && el.type !== "FINISHED") {
        let subt = (new Date(el.endTime).getTime() - new Date(el.startTime).getTime()) / 1000;
        razlika = razlika - subt;
        subt = 0;
      }
    })
    return razlika;
  }


}
