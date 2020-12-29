import { Component, OnInit, ViewChild } from '@angular/core';
import { FormControl } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CdTimerComponent } from 'angular-cd-timer';
import { Observable, interval } from 'rxjs';
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

  startTime = 0;
  startFlag = false;
  pauseFlag = false;
  continueFlag = false;
  backFlag = true;
  endFlag = false;
  status = "Nije započet"
  isTicking = false;
  id;
  pause = new FormControl('');
  workerTimeTracking: WorkerTimeTracking = new WorkerTimeTracking();
  timeTrackingId;
  rnId;
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
        this.pauseFlag = true;
        this.endFlag = true;
        this.isTicking = true;
      } else if (this.workerTimeTracking.headerInfo.wowStatus == "PAUSED") {

      } else if (this.workerTimeTracking.headerInfo.wowStatus == "FINISHED") {

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
      this.rnId = data;
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
    this.basicTimer.start();
    console.log(this.basicTimer.get())
    var timeTracking: TimeTracking = new TimeTracking();
    timeTracking.startTime = new Date();
    timeTracking.wowId = this.id;
    timeTracking.type = this.pause.value;
    timeTracking.id = "";
    this.workerTimeTrackingService.setTracking(timeTracking).subscribe(data => {
      this.timeTrackingId = data;
      this.pauseFlag = false;
      this.continueFlag = true;
      this.endFlag = false;
      this.status = "Pauza";
      console.log(this.pause.value)
    })
  }

  continueTimer() {
    this.basicTimer.start();
    console.log(this.basicTimer.get())
    var timeTracking: TimeTracking = new TimeTracking();
    //timeTracking.startTime = new Date();
    timeTracking.wowId = this.id;
    timeTracking.type = this.pause.value;
    timeTracking.id = this.timeTrackingId;
    timeTracking.endTime = new Date();
    this.workerTimeTrackingService.setTracking(timeTracking).subscribe(data => {
      this.endFlag = true;
      this.pauseFlag = true;
      this.continueFlag = false;
      this.status = "U radu"
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
    timeTracking.id = this.rnId;
    timeTracking.endTime = new Date();
    this.workerTimeTrackingService.setTracking(timeTracking).subscribe(data => {
      this.endFlag = false;
      this.pauseFlag = false;
      this.continueFlag = false;
      this.startFlag = false;
      this.status = "Završeno"
      this.backFlag = true;
    })
  }

  getPause(pause){
    this.pause.setValue(pause);
    if(this.pause.value == "PAUSE_WORK"){
      this.pauseType = "Odmor"
    } else if(this.pause.value == "PAUSE_SERVICE"){
      this.pauseType = "Servis";
    } else {
      this.pauseType = "Sipanje goriva"
    }
    console.log(this.pause.value)
  }

  setPuase(){
    this.pause = new FormControl("");
  }

  goBack(){
    this.router.navigateByUrl("/workOrderTractorDriver")
  }

  getPosition() : Promise<any> {
    return new Promise((resolve, reject) => {
      navigator.geolocation.getCurrentPosition(resp => {
        resolve({lng: resp.coords.longitude, lat: resp.coords.latitude});
      }, error => {
        reject(error);
      })
    })
  }

  seePositon(){
    this.getPosition().then(pos => {
      console.log("Positon: " + pos.lat + "-" + pos.lng)
    })
    
  }


}
