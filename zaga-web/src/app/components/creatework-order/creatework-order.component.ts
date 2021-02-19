import { Component, OnDestroy, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { Material } from "src/app/models/Material";
import { ToastrService } from "ngx-toastr";
import { Router } from "@angular/router";

import { WorkOrderService } from "src/app/service/work-order.service";
import { Crop } from "src/app/models/Crop";
import { WorkOrder } from "src/app/models/WorkOrder";
import { UserService } from "src/app/service/user.service";
import { Employee } from "src/app/models/Employee";
import { Operation } from "src/app/models/Operation";
import { OperationService } from "src/app/service/operation.service";
import { Culture } from "src/app/models/Culture";
import { Machine } from "src/app/models/Machine";
import { MachineService } from "src/app/service/machine.service";
import { MaterialService } from "src/app/service/material.service";
import { Form, FormControl } from "@angular/forms";
import { Observable, Subject } from "rxjs";
import { Field } from "src/app/models/Field";
import { FieldService } from "src/app/service/field.service";
import { DeviceDetectorService } from "ngx-device-detector";
import { CropService } from "src/app/service/crop.service";
import { SpentMaterial } from "src/app/models/SpentMaterial";
import { WorkOrderWorker } from "src/app/models/WorkOrderWorker";
import { ViewChild } from "@angular/core";
import { WorkOrderWorkerService } from "src/app/service/work-order-worker.service";
import { WorkOrderMachine } from "src/app/models/WorkOrderMachine";
import { SpentMaterialService } from "src/app/service/spent-material.service";
import { Renderer2 } from "@angular/core";
import { MatSnackBar, throwMatDialogContentAlreadyAttachedError } from '@angular/material';
import { NgxSpinnerService } from 'ngx-spinner';
import { AuthService } from 'src/app/service/auth/auth.service';
import { User } from 'src/app/models/User';
import { Setting } from 'src/app/models/Setting';
import { LeavePageGuard } from "src/app/service/leave-page.guard";

@Component({
  selector: "app-creatework-order",
  templateUrl: "./creatework-order.component.html",
  styleUrls: ["./creatework-order.component.css"],
})
export class CreateworkOrderComponent implements OnInit {
  @ViewChild("closeButtonMachineModal", null) closeButtonMachineModal;
  @ViewChild("closeButtonMaterialModal", null) closeButtonMaterialModal;
  @ViewChild("closeButtonWorkerModal", null) closeButtonWorkerModal;
  @ViewChild("closeButtonWowModal", null) closeButtonWowModal;
  @ViewChild("closeButtonCloseWOModal", null) closeButtonCloseWOModal;
  @ViewChild("closeButtonMaterialModalMob", null) closeButtonMaterialModalMob;
  @ViewChild("saveStartedCreation", null) saveStartedCreation;
  @ViewChild("closeSaveStartedModal", null) closeSaveStartedModal;
  @ViewChild("openErrorsModal", null) openErrorsModal;
  @ViewChild("leavePageConfirm", null) leavePageConfirm;

  constructor(
    private route: ActivatedRoute,
    private renderer: Renderer2,
    private toastr: ToastrService,
    private router: Router,
    private userService: UserService,
    private operationService: OperationService,
    private machineService: MachineService,
    private materialService: MaterialService,
    private workOrderService: WorkOrderService,
    private fieldService: FieldService,
    private cropService: CropService,
    private deviceService: DeviceDetectorService,
    private wowService: WorkOrderWorkerService,
    private spentMaterialService: SpentMaterialService,
    private spinner: NgxSpinnerService,
    private authService: AuthService,
    private snackBar: MatSnackBar,
    private canDeactivate: LeavePageGuard
  ) {
    this.router.routeReuseStrategy.shouldReuseRoute = () => false;
  }

  modalHeader = "Dodavanje radnika";
  headerMachine = "Dodavanje mašine";
  headerMaterial = "Dodavanje materijala";

  workers = false;
  machines = false;
  materials = false;
  new = false;
  error = false;
  workId = this.route.snapshot.params.workId;

  idOfEditingMaterial: any = 0;
  idOfEditingWorkerMachine: any = 0;

  errors: string[] = [];
  allEmployees: Employee[] = [];
  operations: Operation[] = [];
  devicesPropulsion: Machine[] = [];
  devicesCoupling: Machine[] = [];
  substances: Material[] = [];
  substances$: Observable<Material[]>;
  fields: Field[] = [];
  crops: Crop[] = [];
  woMaterials: SpentMaterial[] = [];
  wows: WorkOrderWorker[] = [];

  wow: WorkOrderWorker = new WorkOrderWorker();
  wowMob: WorkOrderWorker = new WorkOrderWorker();
  spentMaterial: SpentMaterial = new SpentMaterial();
  materialMob: SpentMaterial = new SpentMaterial();
  setting: Setting = new Setting();

  crop: Crop = new Crop();
  workOrder: WorkOrder = new WorkOrder();

  selectedWorkerForMachine;
  selectedTable;
  selectedYear;
  selectedCrop;
  selectedWorker;
  selectedOperation;
  selectedMachine;
  selectedCouplingMachine = -1;
  selectedMaterial;
  quantityEntered;
  unit;
  treatedEntered;
  quantityPerHectar;
  spentPerHectar;
  dateOfCreateWO;

  clickAddMaterial = false;
  clickAddWorkOrder = false;
  clickAddWorkerMachine = false;
  clickAddMachine = false;
  addNewWow = false;
  addNewSpentMaterial = false;
  clickCloseWorkOrder = false;
  exists = false;
  clickAddWowDetail = false;
  clickAddNewWow = false;
  clickUpdateWo = false;
  clickAddNewMaterial = false;
  clickUpdateMaterial = false;
  clickSaveNewModalMaterial = false;
  isGetCulture;
  today;
  withoutMachine;

  validWow;
  validWom;
  validWows: any [] = [];
  validWoms: any [] = [];
  validWoInfo;
  validDayPeriod;
  validNightPeriod;
  validWorkPeriod;
  validFinalState;
  validTreated;
  validInitialState;
  validFuel;
  validMaterialQuantity;
  validSpentQuantity;
  validNewSpentQuantity;
  validQuantity;
  idForValidQuantity;

  nameFC: FormControl = new FormControl("");
  operationFC: FormControl = new FormControl("");
  fieldFC: FormControl = new FormControl("");
  cultureFC: FormControl = new FormControl("");
  workerFC: FormControl = new FormControl("");
  workerOperationFC: FormControl = new FormControl("");
  workerMachineFC: FormControl = new FormControl("");
  workerCoMachineFC: FormControl = new FormControl("");

  existingWorkerFC: FormControl = new FormControl("");
  existingWorkerOperationFC: FormControl = new FormControl("");
  workerExMachineFC: FormControl = new FormControl("");
  materialFC: FormControl = new FormControl("");
  woMaterialFC: FormControl = new FormControl("");

  numOfRefOrderFC: FormControl = new FormControl("");
  noteFC: FormControl = new FormControl("");

  filteredOptions: Observable<string[]>;

  currentYear;
  nextYear;
  previousYear;

  sapId;
  creator;
  loggedUser;
  user: User = new User();
  userCreator: User = new User();

  workOrderWorkerId;
  materialId;

  emptyMaterial;
  emptyWow;
  loading;

  editWowMobFlag;
  editMaterialMobFlag;
  mob = false;

  noOperationOutput = false;

  subject = new Subject<Material>();

  startedCreationWorker = false;
  startedCreationMaterial = false;
  addWow = false;
  addMat = false;
  validAnswerWow = false;
  validAnswerMat = false;
  msgInvalidAnswerWow;
  msgInvalidAnswerMat;
  errWow = false;
  errMat = false;

  leavePageFlag;

  ngOnInit() {
    this.authService.getUserSettings().subscribe(data => {
      this.setting = data;
      this.sapId = this.setting.masterDataFormat;
      console.log(this.sapId)
    })

    this.authService.getLogged().subscribe(data => {
      this.user = data;
      this.loggedUser = this.user.sapUserId;
    })
    if (this.workId == "new") {
      //new
      this.new = true;
      this.workOrder = new WorkOrder();
      this.workOrder.workers = [];
      this.workOrder.materials = [];
      this.workOrder.status = "Novi";
      var today = new Date();
      this.workOrder.date = {
        year: today.getFullYear(),
        month: today.getMonth() + 1,
        day: today.getDate(),
      };
      this.userService.getAll().subscribe((data) => {
        this.allEmployees = data.content;
        this.allEmployees = this.allEmployees.sort((a, b) => a.perNumber - b.perNumber);
        console.log(this.allEmployees)
      });
      //this.substances$ = this.materialService.getAll();
      //this.spinner.show();
      //this.materialService.getAll().subscribe((data) => {
      //data = this.convertKeysToLowerCase(data);
      //this.spinner.hide();
      // this.substances = data;
      //console.log(this.substances)
      //}, error => {
      // this.spinner.hide();
      //});
    } else {
      this.new = false;
      this.today = new Date();
      this.loading = true;
      this.spinner.show();
      this.workOrderService.getOne(this.workId).subscribe((data) => {

        // this.materialService.getAll().subscribe((data) => {
        //   //data = this.convertKeysToLowerCase(data);
        //   this.spinner.hide();
        //   this.substances = data;
        //   console.log(this.substances)
        // }, error => {
        //   this.spinner.hide();
        // });
        this.loading = false;
        this.spinner.hide();
        this.workOrder = data;
        this.noteFC.setValue(this.workOrder.note)
        this.numOfRefOrderFC.setValue(this.workOrder.numOfRefOrder)
        console.log(this.workOrder.treated)
        if (this.workOrder.treated != undefined) {
          this.workOrder.materials.forEach(mat => {
            this.quantityPerHectar = mat.quantity / this.workOrder.treated
            console.log(this.quantityPerHectar)
          })
        }
        console.log(this.workOrder)
        this.treatedEntered = this.workOrder.treated;
        if (this.workOrder.status == "NEW") {
          this.workOrder.status = "Novi";
        } else if (this.workOrder.status == "IN_PROGRESS") {
          this.workOrder.status = "U radu";
        } else if (this.workOrder.status == "CLOSED") {
          this.workOrder.status = "Zatvoren";
        } else if (this.workOrder.status == "CANCELLATION") {
          this.workOrder.status = "Storniran";
        }

        if (this.workOrder.materials.length != 0) {
          this.workOrder.materials.forEach(material => {
            if (material.quantity < 0) {
              material.quantity = null;
            }
            if (material.quantityPerHectar < -1) {
              material.quantityPerHectar = null;
            }
            if (material.deleted == true) {
              this.emptyMaterial = true;
            } else {
              this.emptyMaterial = false;
            }
          })
        }

        if (this.workOrder.workers.length != 0) {
          this.workOrder.workers.forEach(wow => {
            if (wow.deleted == true) {
              this.emptyWow = true;
            } else {
              this.emptyWow = false;
            }
          })
        }

        this.workOrder.date = {
          day: +this.workOrder.date.day,
          month: +this.workOrder.date.month,
          year: +this.workOrder.date.year,
        };
        
        let day;
        let month;
        if(this.workOrder.date.day >= 0 && this.workOrder.date.day <= 9){
          day = "0" + this.workOrder.date.day;
        } else {
          day = this.workOrder.date.day;
        }
        if(this.workOrder.date.month >= 0 && this.workOrder.date.month <= 9){
          month = "0" + this.workOrder.date.month;
        } else {
          month = this.workOrder.date.month;
        }

        this.dateOfCreateWO = day + "." + month + "." + this.workOrder.date.year + "."
        this.creator = this.workOrder.userCreatedId;

        if(this.workOrder.noOperationOutput){
          this.noOperationOutput = true;
        }

        this.userService.getUserBySapId(this.workOrder.userCreatedId).subscribe(data => {
          this.userCreator = data;
        })

        this.cropService
          .getAllByFieldAndYear(data.tableId, data.year)
          .subscribe((res) => {
            this.crops = res;
          });

        if (this.treatedEntered == 0) {
          this.treatedEntered = null;
        }
        this.userService.getAll().subscribe((data) => {
          this.allEmployees = data.content;
          console.log(this.allEmployees)
          var comparableId = this.workOrder.responsibleId;
          var filterById = function (element) {
            if (element.userId == comparableId) {
              console.log(element)
              return element;
            }
          };
          this.nameFC.setValue(this.allEmployees.filter(filterById)[0]);
        });

        this.operationService.getAll().subscribe((data) => {
          this.operations = data;
          var comparableId = this.workOrder.operationId;
          var filterById = function (element) {
            if (element.dbid == comparableId) {
              console.log(element)
              return element;
            }
          };
          this.operationFC.setValue(this.operations.filter(filterById)[0])
        })

        this.fieldService.getAll().subscribe(data => {
          this.fields = data;
          console.log(this.fields)
          var comparableId = this.workOrder.tableId;
          var filterById = function (element) {
            if (element.dbId == comparableId) {
              return element;
            }
          }
          this.fieldFC.setValue(this.fields.filter(filterById)[0])
        })

        this.cropService.getAll().subscribe(data => {
          this.crops = data;
          console.log(this.crops)
          var comparableId = this.workOrder.cropId;
          var filterById = function (element) {
            if (element.id == comparableId) {
              return element;
            }
          }
          this.cultureFC.setValue(this.crops.filter(filterById)[0])
        })
      }, error => {
        this.loading = false;
        this.spinner.hide();
      });
    }

    this.operationService.getAll().subscribe((data) => {
      //data = this.convertKeysToLowerCase(data);
      this.operations = data;
      console.log(this.operations);
    });

    this.machineService.getAllPropulsion().subscribe((data) => {
      // data = this.convertKeysToLowerCase(data);
      this.devicesPropulsion = data;
      console.log(this.devicesPropulsion)
    });

    this.machineService.getAllCoupling().subscribe((data) => {
      // data = this.convertKeysToLowerCase(data);
      this.devicesCoupling = data;
    });

    this.spinner.show();
    this.substances$ = this.materialService.getAll();
    //this.spinner.hide();
    this.materialService.getAll().subscribe((data) => {
      //data = this.convertKeysToLowerCase(data);
      this.spinner.hide();
      this.substances = data;
      console.log(this.substances)
    }, error => {
      this.spinner.hide();
    });

    this.fieldService.getAll().subscribe((data) => {
      this.fields = data;
      console.log(this.fields)
    });

    var now = new Date();
    this.currentYear = now.getFullYear();
    this.selectedYear = this.currentYear;
    this.nextYear = now.getFullYear() + 1;
    this.previousYear = now.getFullYear() - 1;
  }

  //methods for on change listeners

  getArea(id) {
    console.log(id)
    this.workOrder.area = null;
    this.cropService.getOne(id).subscribe((data) => {
      this.crop = data;
      this.crop.Area = data.Area;
      this.workOrder.area = data.Area;
    });
  }

  getOperation(op) {
    this.workerOperationFC.setValue(op);
  }


  getUnitOfMaterial(id) {
    console.log(id)
    
    let item = this.substances$.subscribe(items => {
      items.find(item => item.dbid == id);
    })
    console.log(item)
    this.unit = this.substances.find((x) => x.dbid == id).Unit;
    console.log(this.unit)
    /*
    this.woMaterials.forEach((material) => {
      if (material.material.dbid == id) {
        material.material.Unit = this.unit;
      }
    });
    */
  }

  getCulture() {
    this.workOrder.area = null;
    this.crops = [];
    this.cultureFC = new FormControl("");
    console.log(this.selectedYear);
    if (this.new) {
      console.log(this.fieldFC.value)
      this.cropService
        .getAllByFieldAndYear(this.fieldFC.value.dbId, this.selectedYear)
        .subscribe((data) => {
          this.isGetCulture = true;
          console.log(data);
          this.crops = data;
          if (this.crops.length == 1) {
            console.log(this.crops[0])
            this.cultureFC.setValue(this.crops[0])
            this.workOrder.area = this.crops[0].Area;
          }
        });
    } else {
      this.cropService
        .getAllByFieldAndYear(this.fieldFC.value.dbId, this.workOrder.year)
        .subscribe((data) => {
          console.log(data);
          this.crops = data;
        });
    }

  }

  calculateQuantityPerHectar() {
    this.workOrder.materials.forEach(mat => {
      this.quantityPerHectar = mat.quantity / this.treatedEntered;
    })
  }

  calculateSpentPerHectar(spent) {
    console.log(spent)
    console.log(this.workOrder.treated)
    if (this.workOrder.treated != undefined) {
      this.workOrder.materials.forEach(mat => {
        this.spentPerHectar = mat.spent / this.workOrder.treated
        console.log(this.spentPerHectar)
      })
    } else {
      console.log(this.spentMaterial.spent)
      console.log(this.treatedEntered)
      this.spentPerHectar = this.spentMaterial.spent / this.workOrder.treated;
    }

  }

  //methods for autoscroll

  expandWorkers() {
    this.workers = !this.workers;
    let el = document.getElementById("rad");
    setTimeout(() => {
      el.scrollIntoView({
        behavior: "smooth",
      });
    }, 500);
  }
  expandMachines() {
    this.machines = !this.machines;
    if (!this.deviceService.isMobile) {
      let el = document.getElementById("mas");
      setTimeout(() => {
        el.scrollIntoView({
          behavior: "smooth",
        });
      }, 500);
    }
  }
  expandMaterials() {
    this.materials = !this.materials;
    if (!this.deviceService.isMobile) {
      let el = document.getElementById("mat");
      setTimeout(() => {
        el.scrollIntoView({
          behavior: "smooth",
        });
      }, 500);
    }
  }

  //autocomplete
  displayFn(emp: Employee): string {
    if (emp != null) {
      if (emp.perNumber == undefined && emp.name == undefined) {
        return emp && emp.Id + " - " + emp.Name;
      } else {
        return emp && emp.perNumber + " - " + emp.name;
      }
    }
  }

  displayFnOperation(op: Operation): string {
    return op && op.Id + " - " + op.Name
  }

  displayFnField(field: Field): string {
    return field && field.Id + " - " + field.Name
  }

  displayFnCulture(culture: Crop): string {
    return culture && culture.Id + " - " + culture.Name + " (" + culture.CultureId + "-" + culture.cultureName +")"
  }

  displayFnMachine(machine: Machine): string {
    if (machine != null) {
      if (machine.Id == undefined) {
        return machine && "BEZ PRIKLJUČNE MAŠINE"
      } else {
        return machine && machine.Id + " - " + machine.Name
      }
    }

  }

  displayFnMaterial(substance: Material): string {
    return substance && substance.Id + " - " + substance.Name;
  }

  displayFnWithoutId(element: any): string {
    return element && element.Name;
  }

  displayFnResponsible(emp: Employee): string {
    if (emp.perNumber == undefined && emp.name == undefined) {
      return emp && emp.Name;
    } else {
      return emp && emp.name;
    }
  }

  displayFnMachineWithoutId(machine: Machine): string {
    if (machine.Id == undefined) {
      return machine && "BEZ PRIKLJUČNE MAŠINE"
    } else {
      return machine && machine.Name
    }
  }

  displayFnCultureWithoutId(culture: Crop): string {
    return culture && culture.Name
  }

  //methods for work order workers, operations and machines

  addWorkerAndMachine(valid) {
    this.clickAddWorkerMachine = true;

    if (
      this.workerFC.valid == true &&
      this.workerOperationFC.valid == true &&
      this.workerMachineFC.valid == true &&
      this.workerCoMachineFC.valid == true
    ) {
      this.wow.wowObjectId = Math.floor(Math.random() * 100 + 1);
      this.wow.user = this.workerFC.value;

      //this.wow.user.userd = this.selectedWorker;
      this.wow.operation = this.workerOperationFC.value;
      //this.wow.operation.dbid = this.workerOperationFC.value.dbid;
      this.wow.machine = this.workerMachineFC.value;
      if (this.workerCoMachineFC.value == -1) {
        this.wow.connectingMachine.dbid = -1;
      } else {
        this.wow.connectingMachine = this.workerCoMachineFC.value;
      }

      this.wows.forEach((wow) => {
        console.log(wow)
        console.log(this.wow)
        if (
          wow.user.id == this.wow.user.id &&
          wow.operation.dbid == this.wow.operation.dbid &&
          wow.machine.dbid == this.wow.machine.dbid &&
          wow.connectingMachine.dbid == this.wow.connectingMachine.dbid &&
          wow.wowObjectId != this.wow.wowObjectId
        ) {
          this.toastr.error("Radnik, operacija i mašina su već dodati", "Greška!", {
            positionClass: 'toast-center-center', closeButton: true,  disableTimeOut: true
          });
          this.exists = true;
          this.wow = new WorkOrderWorker();
        }
      });
      if (!this.exists) {
        this.wows.push(this.wow);
        console.log("===============")
        console.log(this.wows)
        console.log("===============")
        this.workerFC = new FormControl("");
        this.workerOperationFC.setValue(this.operationFC.value);
        this.workerMachineFC = new FormControl("");
        this.workerCoMachineFC = new FormControl("");
        console.log(this.wows)
        this.wow = new WorkOrderWorker();
        this.exists = false;
        this.closeButtonWorkerModal.nativeElement.click();
      }
      this.exists = false;
      this.clickAddWorkerMachine = false;

    }
  }

  editWorkerAndMachine(id) {
    console.log(id);
    this.wowService.getOne(id).subscribe(data => {
      console.log(data)
      this.wow = data;
      if (this.wow.machine.Id == "BEZ-MASINE") {
        this.withoutMachine = true;
      } else {
        this.withoutMachine = false;
      }
      if (this.wow.dayPeriod == -1) {
        this.wow.dayPeriod = null;
      }
      if (this.wow.nightPeriod == -1) {
        this.wow.nightPeriod = null;
      }
      if (this.wow.initialState == -1) {
        this.wow.initialState = null;
      }
      if (this.wow.finalState == -1) {
        this.wow.finalState = null;
      }
      console.log(this.wow.fuel)
      if (this.wow.fuel == -1) {
        console.log("USAOOOOOO")
        this.wow.fuel = null;
      }
    })
    
    /*
    console.log(this.wow);
    console.log(this.allEmployees.find(
      (x) => x.userId == this.wow.user.userId
    ))
    this.wow.user.Name = this.allEmployees.find(
      (x) => x.userId == this.wow.user.userId
    ).name;
    console.log(this.wow.machine);
    
    console.log(this.withoutMachine)
    this.wow.machine.Name = this.devicesPropulsion.find(
      (x) => x.dbid == this.wow.machine.dbid
    ).Name;
    */
   
    
  }

  editExistingWorkerAndMachine(existing) {
    console.log(existing)
    this.idOfEditingWorkerMachine = existing.wowObjectId;
    this.wows.forEach((wow) => {
      if (wow.wowObjectId == this.idOfEditingWorkerMachine) {
        console.log(wow)
        console.log(this.workerFC.value)
        if(this.mob){
          wow.user = this.workerFC.value;
          wow.machine = this.workerMachineFC.value;
          if(this.workerCoMachineFC.value == -1){
            wow.connectingMachine.dbid = this.workerCoMachineFC.value;
          } else {
            wow.connectingMachine = this.workerCoMachineFC.value;
          }
          
          wow.operation = this.workerOperationFC.value;
        } else {
          wow.user.userId = this.allEmployees.find(
            (x) => x.userId == wow.user.userId
          ).userId;
         
          wow.machine.dbid = this.devicesPropulsion.find(
            (x) => x.dbid == wow.machine.dbid
          ).dbid;
          console.log(this.devicesCoupling);
          
          if(wow.connectingMachine.dbid != -1){
            wow.connectingMachine.dbid = this.devicesCoupling.find(
              (x) => x.dbid == wow.connectingMachine.dbid
            ).dbid;
          } else {
            wow.connectingMachine.dbid = -1;
          }
            
          
         
          wow.operation.dbid = this.operations.find(
            (x) => x.dbid == wow.operation.dbid
          ).dbid;
          wow.dayPeriod = this.wow.dayPeriod;
          wow.nightPeriod = this.wow.nightPeriod;
          wow.initialState = this.wow.initialState;
          wow.finalState = this.wow.finalState;
          wow.fuel = this.wow.fuel;
        }
   
        this.closeButtonWorkerModal.nativeElement.click();
        this.toastr.success("Uspešno izvršena promena.", "Potvrda!", {
          positionClass: 'toast-center-center', closeButton: true,  disableTimeOut: true
        });
        console.log(this.wows)
      }
    });
  }

  addDetailWow() {
    if (this.new) {
      this.addNewWow = false;
    } else {
      this.addNewWow = true;
      console.log(this.workerMachineFC.value)
      if (this.workerMachineFC.value.Id == "BEZ-MASINE") {
        this.withoutMachine = true;
      } else {
        this.withoutMachine = false;
      }
    }
    this.wow.dayPeriod = null;
    this.wow.nightPeriod = null;
    this.wow.initialState = null;
    this.wow.finalState = null;
    this.wow.user.Name = null;
    this.wow.machine.Name = null;
  }

  validDetailOfNewWow() {
    this.clickAddNewWow = true;
    if (this.wow.dayPeriod > 24 || this.wow.dayPeriod < 0) {
      this.validDayPeriod = false;
    } else {
      this.validDayPeriod = true;
    }
    if (this.wow.nightPeriod > 24 || this.wow.nightPeriod < 0) {
      this.validNightPeriod = false;
    } else {
      this.validNightPeriod = true;
    }
    if (this.wow.dayPeriod + this.wow.nightPeriod > 24) {
      this.validWorkPeriod = false;
    } else {
      this.validWorkPeriod = true;
    }
    if (this.wow.initialState < 0) {
      this.validInitialState = false;
    } else {
      this.validInitialState = true;
    }
    if (this.wow.finalState < this.wow.initialState || this.wow.finalState < 0) {
      this.validFinalState = false;
    } else {
      this.validFinalState = true;
    }
    if (this.wow.fuel < 0) {
      this.validFuel = false;
    } else {
      this.validFuel = true;
    }
    if (this.validWorkPeriod && this.validNightPeriod && this.validWorkPeriod && this.validFinalState && this.validInitialState && this.validFuel) {
      this.closeButtonWowModal.nativeElement.click();
    }
  }

  addNewWorkerAndMachine() {
    this.wow.user.userId = this.workerFC.value.userId;
    this.wow.operation.dbid = this.workerOperationFC.value.dbid;
    this.wow.machine.dbid = this.workerMachineFC.value.dbid;
    this.spinner.show();
    if(this.workerCoMachineFC.value == -1){
      this.wow.connectingMachine.dbid = this.workerCoMachineFC.value;
    } else {
      this.wow.connectingMachine.dbid = this.workerCoMachineFC.value.dbid;
    }
    console.log(this.wow)
    this.wowService.addWorker(this.wow, this.workId).subscribe((res) => {
      console.log(res);
      this.toastr.success("Izvršioc " + this.workerFC.value.name + " je uspešno dodat.", "Potvrda!", {
        positionClass: 'toast-center-center', closeButton: true,  disableTimeOut: false
      })
      this.closeButtonWorkerModal.nativeElement.click();
      this.wow = new WorkOrderWorker();
      this.spinner.hide();
      this.workerFC = new FormControl("");
      this.workerOperationFC.setValue(this.operationFC.value);
      this.workerMachineFC = new FormControl("");
      this.workerCoMachineFC = new FormControl("");
      this.workOrderService.getOne(this.workId).subscribe((data) => {
        this.workOrder = data;
        if (this.workOrder.status == "NEW") {
          this.workOrder.status = "Novi";
        } else if (this.workOrder.status == "IN_PROGRESS") {
          this.workOrder.status = "U radu";
        } else if (this.workOrder.status == "CLOSED") {
          this.workOrder.status = "Zatvoren";
        } else if (this.workOrder.status == "CANCELLATION") {
          this.workOrder.status = "Storniran";
        }

        this.noteFC.setValue(this.workOrder.note)
        this.numOfRefOrderFC.setValue(this.workOrder.numOfRefOrder)

        this.workOrder.date = {
          day: +this.workOrder.date.day,
          month: +this.workOrder.date.month,
          year: +this.workOrder.date.year,
        };

        this.cropService
          .getAllByFieldAndYear(data.tableId, data.year)
          .subscribe((res) => {
            this.crops = res;
          });

        if (this.workOrder.treated == 0) {
          this.workOrder.treated = null;
        }
        if (this.workOrder.materials.length != 0) {
          this.workOrder.materials.forEach(material => {
            if (material.quantity < 0) {
              material.quantity = null;
            }
            if (material.quantityPerHectar < -1) {
              material.quantityPerHectar = null;
            }
          })
        }
        if(this.workOrder.noOperationOutput){
          this.noOperationOutput = true;
        }

      });
    }, err => {
      this.spinner.hide();
      this.toastr.error("Izvršioc " + this.workerFC.value.name + " nije dodat.", "Greška!", {
        positionClass: 'toast-center-center', closeButton: true,  disableTimeOut: true
      })
    });
  }


  updateWow(workOrderWorker) {
    console.log(this.wow + "VBBBBBBBBBB");
    this.spinner.show();
    this.clickAddWowDetail = true;
    if (workOrderWorker.dayPeriod > 24 || workOrderWorker.dayPeriod < 0 || workOrderWorker.dayPeriod == null) {
      this.validDayPeriod = false;
    } else {
      this.validDayPeriod = true;
    }
    if (workOrderWorker.nightPeriod > 24 || workOrderWorker.nightPeriod < 0 || workOrderWorker.nightPeriod == null) {
      this.validNightPeriod = false;
    } else {
      this.validNightPeriod = true;
    }
    if (workOrderWorker.dayPeriod + this.wow.nightPeriod > 24) {
      this.validWorkPeriod = false;
    } else {
      this.validWorkPeriod = true;
    }
    if ((workOrderWorker.initialState < 0 || workOrderWorker.initialState == null) && !this.withoutMachine) {
      this.validInitialState = false;
    } else {
      this.validInitialState = true;
    }
    if ((workOrderWorker.finalState < this.wow.initialState || workOrderWorker.finalState < 0 || workOrderWorker.finalState == null) && !this.withoutMachine) {
      this.validFinalState = false;
    } else {
      this.validFinalState = true;
    }
    if ((workOrderWorker.fuel < 0 || workOrderWorker.fuel == null) && !this.withoutMachine) {
      this.validFuel = false;
    } else {
      this.validFuel = true;
    }
    if(this.noOperationOutput){
      workOrderWorker.noOperationOutput = true;
    } else {
      workOrderWorker.noOperationOutput = false;
    }
    console.log(workOrderWorker);
    if (this.validWorkPeriod && this.validNightPeriod && this.validWorkPeriod && this.validFinalState && this.validInitialState && this.validFuel) {
      this.wowService.updateWorkOrderWorker(workOrderWorker.id, workOrderWorker).subscribe((res) => {
        console.log(res);
        this.wow = new WorkOrderWorker();
        if(this.withoutMachine){
          this.toastr.success("Uspešno uneti učinci za izvršioca " + workOrderWorker.user.Name, "Potvrda!", {
            positionClass: 'toast-center-center', closeButton: true,  disableTimeOut: false
          });
        } else {
          this.toastr.success("Uspešno uneti učinci za izvršioca " + workOrderWorker.user.Name + " i mašinu " + workOrderWorker.machine.Name, "Potvrda!", {
            positionClass: 'toast-center-center', closeButton: true,  disableTimeOut: false
          });
        }
        
        this.spinner.hide();
        this.getOneWorkOrder();
      }, error => {
        this.spinner.hide();
        this.getOneWorkOrder();
        
        if (error.status == 400) {
          this.toastr.error("Došlo je do greške.Za detalje kliknite na uzvičnik.")
          this.error = true;
          this.errors = error.error.message;
        } else {
          if(this.withoutMachine){
            this.toastr.error("Neuspešno uneti učinci za izvršioca " + workOrderWorker.user.Name, "Greška!", {
              positionClass: 'toast-center-center', closeButton: true,  disableTimeOut: true
            });
          } else {
            this.toastr.error("Neuspešno uneti učinci za izvršioca " + workOrderWorker.user.Name + " i mašinu " + workOrderWorker.machine.Name, "Greška!", {
              positionClass: 'toast-center-center', closeButton: true,  disableTimeOut: true
            });
          }
        }
        this.wow = new WorkOrderWorker();
      });
      this.closeButtonWowModal.nativeElement.click();
    } else {
      this.spinner.hide();
    }

  }

  getOneWorkOrder(){
    this.workOrderService.getOne(this.workId).subscribe((data) => {
      this.workOrder = data;
      this.noteFC.setValue(this.workOrder.note)
      this.numOfRefOrderFC.setValue(this.workOrder.numOfRefOrder)

      this.workOrder.treated = this.treatedEntered;
      if (this.workOrder.status == "NEW") {
        this.workOrder.status = "Novi";
      } else if (this.workOrder.status == "IN_PROGRESS") {
        this.workOrder.status = "U radu";
      } else if (this.workOrder.status == "CLOSED") {
        this.workOrder.status = "Zatvoren";
      } else if (this.workOrder.status == "CANCELLATION") {
        this.workOrder.status = "Storniran";
      }

      if (this.workOrder.materials.length != 0) {
        this.workOrder.materials.forEach(material => {
          if (material.quantity < 0) {
            material.quantity = null;
          }
          if (material.quantityPerHectar < 0) {
            material.quantityPerHectar = null;
          }
        })
      }

      this.workOrder.date = {
        day: +this.workOrder.date.day,
        month: +this.workOrder.date.month,
        year: +this.workOrder.date.year,
      };

      if(this.workOrder.noOperationOutput){
        this.noOperationOutput = true;
      }

      this.cropService
        .getAllByFieldAndYear(data.tableId, data.year)
        .subscribe((res) => {
          this.crops = res;
        });

      if (this.workOrder.treated == 0) {
        this.workOrder.treated = null;
      }
    });
  }

  updateWOWBasicInfo(workOrderWorker) {
    console.log(workOrderWorker)
    this.wowService.updateWorkOrderWorkerBasicInfo(workOrderWorker.id, workOrderWorker).subscribe((res) => {
      console.log(res);
      this.toastr.success("Uspešno izvršena promena.", "Potvrda!", {
        positionClass: 'toast-center-center', closeButton: true,  disableTimeOut: false
      });
      this.spinner.hide();
      this.workOrderService.getOne(this.workId).subscribe((data) => {
        this.workOrder = data;
        this.noteFC.setValue(this.workOrder.note)
        this.numOfRefOrderFC.setValue(this.workOrder.numOfRefOrder)

        this.workOrder.treated = this.treatedEntered;
        if (this.workOrder.status == "NEW") {
          this.workOrder.status = "Novi";
        } else if (this.workOrder.status == "IN_PROGRESS") {
          this.workOrder.status = "U radu";
        } else if (this.workOrder.status == "CLOSED") {
          this.workOrder.status = "Zatvoren";
        } else if (this.workOrder.status == "CANCELLATION") {
          this.workOrder.status = "Storniran";
        }

        if (this.workOrder.materials.length != 0) {
          this.workOrder.materials.forEach(material => {
            if (material.quantity == -1) {
              material.quantity = null;
            }
            if (material.quantityPerHectar < 0) {
              material.quantityPerHectar = null;
            }
          })
        }

        this.workOrder.date = {
          day: +this.workOrder.date.day,
          month: +this.workOrder.date.month,
          year: +this.workOrder.date.year,
        };

        if(this.workOrder.noOperationOutput){
          this.noOperationOutput = true;
        }

        this.cropService
          .getAllByFieldAndYear(data.tableId, data.year)
          .subscribe((res) => {
            this.crops = res;
          });

        if (this.workOrder.treated == 0) {
          this.workOrder.treated = null;
        }
      });
    }, error => {
      this.spinner.hide();
      this.toastr.error("Neuspešno izvršena promena.", "Greška!", {
        positionClass: 'toast-center-center', closeButton: true,  disableTimeOut: true
      });
    });
  }

  //methods for work order materials

  addMaterial(valid) {
    this.clickAddMaterial = true;
    console.log(this.quantityEntered);
    console.log(valid)
    if (this.quantityEntered < 0) {
      this.validMaterialQuantity = false;
      console.log(this.validMaterialQuantity)
    } else {
      this.validMaterialQuantity = true;
    }
    if (valid && this.validMaterialQuantity == true) {
      this.spentMaterial.smObjectId = Math.floor(Math.random() * 100 + 1);
      this.spentMaterial.material.dbid = this.selectedMaterial;
      this.spentMaterial.quantity = this.quantityEntered;
      this.spentMaterial.material.Unit = this.unit;
      this.spentMaterial.material = this.materialFC.value;
      this.woMaterials.forEach((material) => {
        if (
          material.material.dbid == this.spentMaterial.material.dbid &&
          material.quantity == this.spentMaterial.quantity
        ) {
          this.toastr.error("Materijal sa izabranom količinom je već unet. Izaberite drugi materijal ili promenite količinu.", "Greška!", {
            positionClass: 'toast-center-center', closeButton: true,  disableTimeOut: true
          });
          this.exists = true;
        }
      });
      if (!this.exists) {
        this.woMaterials.push(this.spentMaterial);
        console.log(this.spentMaterial.material)
        this.materialFC = new FormControl("");
        this.spentMaterial = new SpentMaterial();
        this.exists = false;
        this.closeButtonMaterialModalMob.nativeElement.click();
      }
      this.clickAddMaterial = false;
      this.exists = false;
      this.selectedMaterial = null;
      this.quantityEntered = null;
      this.unit = null;
      this.closeButtonMaterialModal.nativeElement.click();
    }
  }

  editMaterial(material) {
    console.log(material)
    if (this.new) {
      this.spentMaterial.spent = material.spent;
      this.spentMaterial.material.Unit = material.material.unit;
      this.unit = this.spentMaterial.material.Unit;
      this.spentMaterial.material.Name = this.substances.find(
        (x) => x.dbid == material.material.id
      ).Name;
      this.spentMaterial.quantity = material.quantity;
      this.idOfEditingMaterial = material.smObjectId;
      console.log(this.spentMaterial);
    } else {
      this.spentMaterial.id = material.id;
      this.spentMaterial = material;
      this.spentMaterial.material.Unit = this.substances.find(
        (x) => x.dbid == this.spentMaterial.material.dbid
      ).Unit;
      this.spentMaterial.material.Name = this.substances.find(
        (x) => x.dbid == this.spentMaterial.material.dbid
      ).Name;
      if (material.spent == -1) {
        this.spentMaterial.spent = null;
        this.spentMaterial.spentPerHectar = null;
        this.spentPerHectar = null;
      } else {
        this.spentMaterial.spent = material.spent;
        this.spentMaterial.spentPerHectar = material.spentPerHectar;
        console.log("TREATED: " + this.workOrder.treated)
        if (this.workOrder.treated != 0) {
          console.log("IF")
          this.spentPerHectar = this.spentMaterial.spent / this.workOrder.treated;
        }
        else {
          console.log("ELSE")
          this.spentPerHectar = this.spentMaterial.spent / this.treatedEntered;
          console.log(this.spentPerHectar)
        }

      }
    }
  }

  editExistingMaterial(existing) {
    console.log(existing)
    this.idOfEditingMaterial = existing.smObjectId;
    this.woMaterials.forEach((material) => {
      console.log(material)
      if (material.smObjectId == this.idOfEditingMaterial) {
        if(!this.mob){
          material.material.dbid = this.substances.find(
            (x) => x.dbid == material.material.dbid
          ).dbid;
          material.quantity = existing.quantity;
        } else {
          material.material = this.materialFC.value;
          material.quantity = this.quantityEntered;
          this.closeButtonMaterialModalMob.nativeElement.click();
        }
      }
    });
    this.toastr.success("Materijal " + existing.material.Name + " je uspešno promenjen.", "Potvrda!", {
      positionClass: 'toast-center-center', closeButton: true,  disableTimeOut: false
    });
    console.log(this.woMaterials);
  }

  addDetailMaterial() {
    if (this.new) {
      this.addNewSpentMaterial = false;
    } else {
      this.addNewSpentMaterial = true;
      this.spentMaterial = new SpentMaterial();
      this.spentMaterial.material.Unit = this.unit;
    }
    this.spentMaterial.spent = null;
  }

  validSpentMaterial() {
    this.clickSaveNewModalMaterial = true;
    if (this.spentMaterial.spent < 0) {
      this.validNewSpentQuantity = false;
    } else {
      this.validNewSpentQuantity = true;
      this.closeButtonMaterialModal.nativeElement.click();
    }
  }

  addNewMaterial() {
    this.clickAddNewMaterial = true;
    this.spinner.show();
    if (this.quantityEntered < 0) {
      this.validMaterialQuantity = false;
      this.spinner.hide();
    } else {
      this.validMaterialQuantity = true;
      this.spentMaterial.quantity = this.quantityEntered;
    }
    if (this.validMaterialQuantity == true) {
      this.clickAddNewMaterial = false;
      this.spentMaterial.quantity = this.quantityEntered;
      this.spentMaterial.material.dbid = this.materialFC.value.dbid;
      this.spentMaterialService
        .addSpentMaterial(this.workId, this.spentMaterial)
        .subscribe((res) => {
          this.closeButtonMaterialModalMob.nativeElement.click();
          this.spinner.hide();
          console.log(this.materialFC.value.Name)
          this.toastr.success("Materijal " + this.materialFC.value.Name + " je uspešno dodat.", "Potvrda!", {
            positionClass: 'toast-center-center', closeButton: true,  disableTimeOut: false
          })
          console.log(res);
          this.spentMaterial = new SpentMaterial();
          this.materialFC = new FormControl("");
          this.selectedMaterial = null;
          this.quantityEntered = null;
          this.workOrderService.getOne(this.workId).subscribe((data) => {
            this.workOrder = data;
            this.noteFC.setValue(this.workOrder.note)
            this.numOfRefOrderFC.setValue(this.workOrder.numOfRefOrder)
            if (this.workOrder.status == "NEW") {
              this.workOrder.status = "Novi";
            } else if (this.workOrder.status == "IN_PROGRESS") {
              this.workOrder.status = "U radu";
            } else if (this.workOrder.status == "CLOSED") {
              this.workOrder.status = "Zatvoren";
            } else if (this.workOrder.status == "CANCELLATION") {
              this.workOrder.status = "Storniran";
            }

            if(this.workOrder.noOperationOutput){
              this.noOperationOutput = true;
            }

            this.workOrder.date = {
              day: +this.workOrder.date.day,
              month: +this.workOrder.date.month,
              year: +this.workOrder.date.year,
            };

            this.cropService
              .getAllByFieldAndYear(data.tableId, data.year)
              .subscribe((res) => {
                this.crops = res;
              });

            if (this.workOrder.treated == 0) {
              this.workOrder.treated = null;
            }
            if (this.workOrder.materials.length != 0) {
              this.workOrder.materials.forEach(material => {
                if (material.quantity < 0) {
                  material.quantity = null;
                }
                if (material.quantityPerHectar < -1) {
                  material.quantityPerHectar = null;
                }
                if (material.deleted == true) {
                  this.emptyMaterial = true;
                } else {
                  this.emptyMaterial = false;
                }
              })
            }
          });
        }, error => {
          this.spinner.hide();
          this.toastr.error("Materijal " + this.materialFC.value.Name + " nije dodat.", "Greška!", {
            positionClass: 'toast-center-center', closeButton: true,  disableTimeOut: true
          })
        });
    }

  }

  updateMaterial(spentMaterial) {
    console.log(spentMaterial.spent)
    console.log(spentMaterial)
    this.clickUpdateMaterial = true;
    this.error = false;
    this.errors = [];
    this.spinner.show();
    if (spentMaterial.spent < 0 || spentMaterial.spent == null) {
      this.validSpentQuantity = false;
      this.spinner.hide();
    } else {
      this.validSpentQuantity = true;
    }
    if(spentMaterial.quantity != null && spentMaterial.quantity >=0){
      this.validQuantity = true;
    }
    else {
      this.validQuantity = false;
      this.toastr.error("Unesite planiranu količinu za materijal " + spentMaterial.material.Name, "Greška!", {
        positionClass: 'toast-center-center', closeButton: true,  disableTimeOut: true
      })
      this.spinner.hide();
    }
    if (this.validSpentQuantity == true && this.validQuantity == true) {
      this.clickUpdateMaterial = false;
      this.spentMaterialService
        .updateSpentMaterial(spentMaterial.id, spentMaterial)
        .subscribe((res) => {
          console.log(res);
          this.toastr.success("Uspešno uneti učinci za materijal " + spentMaterial.material.Name, "Potvrda!", {
            positionClass: 'toast-center-center', closeButton: true,  disableTimeOut: false
          });
          this.spinner.hide();
          this.workOrderService.getOne(this.workId).subscribe((data) => {
            this.workOrder = data;
            this.noteFC.setValue(this.workOrder.note)
            this.numOfRefOrderFC.setValue(this.workOrder.numOfRefOrder)
            if (this.workOrder.status == "NEW") {
              this.workOrder.status = "Novi";
            } else if (this.workOrder.status == "IN_PROGRESS") {
              this.workOrder.status = "U radu";
            } else if (this.workOrder.status == "CLOSED") {
              this.workOrder.status = "Zatvoren";
            } else if (this.workOrder.status == "CANCELLATION") {
              this.workOrder.status = "Storniran";
            }

            if(this.workOrder.noOperationOutput){
              this.noOperationOutput = true;
            }

            this.workOrder.date = {
              day: +this.workOrder.date.day,
              month: +this.workOrder.date.month,
              year: +this.workOrder.date.year,
            };

            this.cropService
              .getAllByFieldAndYear(data.tableId, data.year)
              .subscribe((res) => {
                this.crops = res;
              });

            if (this.workOrder.materials.length != 0) {
              this.workOrder.materials.forEach(material => {
                if (material.quantity < 0) {
                  material.quantity = null;
                }
                if (material.quantityPerHectar < -1) {
                  material.quantityPerHectar = null;
                }
                if (material.deleted == true) {
                  this.emptyMaterial = true;
                } else {
                  this.emptyMaterial = false;
                }
              })
            }

            if (this.workOrder.treated == 0) {
              this.workOrder.treated = null;
            }
          });
        }, err => {
          
          spentMaterial.spent = null;
          this.spinner.hide();
          console.log(err);
          if(err.status == 400){
            this.error = true;
            this.errors = err.error.errors;
            this.toastr.error("Greška. Za detalje kliknite na uzvičnik");
          } else {
            this.toastr.error("Neuspešno uneti učinci za materijal " + spentMaterial.material.Name, "Greška!", {
              positionClass: 'toast-center-center', closeButton: true,  disableTimeOut: true
            });
          }
         
        });
      this.closeButtonMaterialModal.nativeElement.click();
    }

  }

  updateMaterialBasicInfo(spentMaterial) {
    this.idForValidQuantity = spentMaterial.id;
    this.clickUpdateMaterial = true;
    console.log(spentMaterial)
    if(this.mob){
      if(this.quantityEntered < 0 || this.quantityEntered == null){
        this.validQuantity = false;
      } else {
        spentMaterial.material = this.materialFC.value;
        spentMaterial.quantity = this.quantityEntered;
        this.validQuantity = true;
      }
    } else {
      if (spentMaterial.quantity < 0 || spentMaterial.quantity == null) {
        this.validQuantity = false;
      } else {
        this.validQuantity = true;
      }
    }
   
    if (this.validQuantity == true) {
      this.spentMaterialService
        .updateSpentMaterialBasicInfo(spentMaterial.id, spentMaterial)
        .subscribe((res) => {
          console.log(res);
          this.toastr.success("Uspešno sačuvane promene.", "Potvrda!", {
            positionClass: 'toast-center-center', closeButton: true,  disableTimeOut: false
          });
          this.clickUpdateMaterial = false;
          this.spinner.hide();
          this.workOrderService.getOne(this.workId).subscribe((data) => {
            this.workOrder = data;
            this.noteFC.setValue(this.workOrder.note)
            this.numOfRefOrderFC.setValue(this.workOrder.numOfRefOrder)
            if (this.workOrder.status == "NEW") {
              this.workOrder.status = "Novi";
            } else if (this.workOrder.status == "IN_PROGRESS") {
              this.workOrder.status = "U radu";
            } else if (this.workOrder.status == "CLOSED") {
              this.workOrder.status = "Zatvoren";
            } else if (this.workOrder.status == "CANCELLATION") {
              this.workOrder.status = "Storniran";
            }

            if(this.workOrder.noOperationOutput){
              this.noOperationOutput = true;
            }

            this.workOrder.date = {
              day: +this.workOrder.date.day,
              month: +this.workOrder.date.month,
              year: +this.workOrder.date.year,
            };

            this.cropService
              .getAllByFieldAndYear(data.tableId, data.year)
              .subscribe((res) => {
                this.crops = res;
              });

            if (this.workOrder.treated == 0) {
              this.workOrder.treated = null;
            }
            if (this.workOrder.materials.length != 0) {
              this.workOrder.materials.forEach(material => {
                if (material.quantity < 0) {
                  material.quantity = null;
                }
                if (material.quantityPerHectar < -1) {
                  material.quantityPerHectar = null;
                }
                if (material.deleted == true) {
                  this.emptyMaterial = true;
                } else {
                  this.emptyMaterial = false;
                }
              })
            }
          });
        }, err => {
          if(err.status == 400){
            this.toastr.error("Greška. Za detalje kliknite na uzvičnik");
            this.error = true;
            this.errors = err.error.errors;
          } else {
            this.toastr.error("Neuspešno sačuvane promene.", "Greška!", {
              positionClass: 'toast-center-center', closeButton: true,  disableTimeOut: true
            });
          }
          spentMaterial.spent = null;
          this.spinner.hide();
          console.log(err);
         
        });
    }

  }

  //methods for work order

  addWorkOrder(valid) {
    this.error = false;
    this.errors = [];
    this.clickAddWorkOrder = true;
    console.log(valid.status);
    console.log(this.fieldFC.value.dbId)
    if (
      valid.status == "VALID" && 
      this.nameFC.valid &&
      this.nameFC.value.userId != undefined &&
      this.operationFC.value.dbid != undefined &&
      this.fieldFC.value.dbId != undefined &&
      this.cultureFC.value.id != undefined
    ) {
      this.spinner.show();
      if (this.workOrder.date != undefined) {
        if (this.workOrder.date.month < 10) {
          this.workOrder.date.month = "0" + this.workOrder.date.month;
        }
        if (this.workOrder.date.day < 10) {
          this.workOrder.date.day = "0" + this.workOrder.date.day;
        }
      }

      this.workOrder.workers = this.wows;
      this.workOrder.materials = this.woMaterials;

      this.workOrder.responsibleId = this.nameFC.value.userId
      this.workOrder.operationId = this.operationFC.value.dbid
      this.workOrder.tableId = this.fieldFC.value.dbId;
      this.workOrder.cropId = this.cultureFC.value.id
      this.workOrder.numOfRefOrder = this.numOfRefOrderFC.value;
      this.workOrder.note = this.noteFC.value;
      console.log("===========")
      console.log(this.workOrder)
      console.log("===========")

      this.workOrderService.addWorkOrder(this.workOrder).subscribe(
        (data) => {
          this.spinner.hide();
          this.canDeactivate.leave = true;
          this.toastr.success(
            "Uspešno kreiran radni nalog. SAP id je " + data.erpId, "Potvrda!", {
              positionClass: 'toast-center-center', closeButton: true,  disableTimeOut: false
            }
          );
          this.router.navigate(["/workOrder"]);
        },
        (error) => {
          this.spinner.hide();
          if (error.status === 400) {
            //let errorMessage = error.error.message;
            //this.toastr.error(errorMessage);
            this.error = true;
            this.errors = error.error.message;
            this.openErrorsModal.nativeElement.click();
          } else {
            this.toastr.error("Radni nalog nije kreiran.", "Greška!", {
              positionClass: 'toast-center-center', closeButton: true,  disableTimeOut: true
            });
          }
        }
      );

    } else {
      this.toastr.error("Radni nalog nije kreiran. Popunite sva polja pravilno.", "Greška!", {
        positionClass: 'toast-center-center', closeButton: true,  disableTimeOut: true
      });
    }
  }

  updateWorkOrder() {
    this.clickUpdateWo = true;
    if (this.workOrder.date != undefined) {
      if (this.workOrder.date.month < 10) {
        this.workOrder.date.month = "0" + this.workOrder.date.month;
      }
      if (this.workOrder.date.day < 10) {
        this.workOrder.date.day = "0" + this.workOrder.date.day;
      }
    }
    
    this.spinner.show();
    this.workOrder.responsibleId = this.nameFC.value.userId;
    this.workOrder.operationId = this.operationFC.value.dbid
    this.workOrder.tableId = this.fieldFC.value.dbId;
    this.workOrder.cropId = this.cultureFC.value.id
    this.workOrder.numOfRefOrder = this.numOfRefOrderFC.value
    this.workOrder.note = this.noteFC.value;
    if(this.noOperationOutput){
      this.workOrder.noOperationOutput = true;
      this.validTreated = true;
    } else {
      this.workOrder.noOperationOutput = false;
    }
    if (this.treatedEntered >= 0) {
      this.workOrder.treated = this.treatedEntered;
    } else {
      this.validTreated = false;
      this.spinner.hide();
      return;
    }

    console.log(this.workOrder)
    this.workOrderService.updateWorkOrder(this.workOrder).subscribe(
      (data) => {
        this.clickUpdateWo = false;
        this.spinner.hide();
        this.toastr.success("Radni nalog " + this.workOrder.sapId + " je uspešno sačuvan.", "Potvrda!", {
          positionClass: 'toast-center-center', closeButton: true,  disableTimeOut: false
        });
        this.workOrderService.getOne(this.workId).subscribe((data) => {
          this.workOrder = data;
          this.noteFC.setValue(this.workOrder.note)
          this.numOfRefOrderFC.setValue(this.workOrder.numOfRefOrder)
          console.log(this.workOrder);
          if (this.workOrder.status == "NEW") {
            this.workOrder.status = "Novi";
          } else if (this.workOrder.status == "IN_PROGRESS") {
            this.workOrder.status = "U radu";
          } else if (this.workOrder.status == "CLOSED") {
            this.workOrder.status = "Zatvoren";
          } else if (this.workOrder.status == "CANCELLATION") {
            this.workOrder.status = "Storniran";
          }

          if(this.workOrder.noOperationOutput){
            this.noOperationOutput = true;
          }

          this.workOrder.date = {
            day: +this.workOrder.date.day,
            month: +this.workOrder.date.month,
            year: +this.workOrder.date.year,
          };

          this.cropService
            .getAllByFieldAndYear(data.tableId, data.year)
            .subscribe((res) => {
              this.crops = res;
            });

          if (this.workOrder.materials.length != 0) {
            this.workOrder.materials.forEach(material => {
              if (material.quantity < 0) {
                material.quantity = null;
              }
              if (material.quantityPerHectar < -1) {
                material.quantityPerHectar = null;
              }
              if (material.deleted == true) {
                this.emptyMaterial = true;
              } else {
                this.emptyMaterial = false;
              }
            })
          }

          if (this.workOrder.treated == 0) {
            this.workOrder.treated = null;
          }
        })
      },
      (error) => {
        this.spinner.hide();
        //this.toastr.error("Došlo je do greške prilikom čuvanja.")
        if (error.status == 400) {
          this.error = true;
          this.errors = error.error.message;
          this.openErrorsModal.nativeElement.click();
        } else {
          this.toastr.error("Radni nalog " + this.workOrder.sapId + " nije sačuvan.", "Greška!", {
            positionClass: 'toast-center-center', closeButton: true,  disableTimeOut: true
          });
        }
      }
    );
  }

  closeWorkOrder() {
    this.clickCloseWorkOrder = true;
    if (this.treatedEntered != null) {
      this.validWoInfo = true;
    } else {
      this.validWoInfo = false;
      this.toastr.error("Unesite obrađenu površinu pre zatvaranja radnog naloga.", "Greška!", {
        positionClass: 'toast-center-center', closeButton: true,  disableTimeOut: true
      })
    }
    if(this.noOperationOutput){
      this.validWoInfo = true;
    }
    this.workOrder.workers.forEach((wow) => {
      console.log(wow)
      
      if(wow.machine.Id == "BEZ-MASINE"){
        this.withoutMachine = true;
      }else {
        this.withoutMachine = false;
      }
      console.log(this.withoutMachine)
      if(this.withoutMachine){
        if(
          wow.dayPeriod == -1 ||
          wow.dayPeriod == null ||
          wow.nightPeriod == -1 ||
          wow.nightPeriod == null
          ) {
            this.validWow = false;
            this.validWows.push("false");
            const element: HTMLElement = document.getElementById(wow.id);
            this.renderer.setStyle(element, "background-color", "#BD362F");
          } else {
            this.validWow = true;
            this.validWows.push("true");
          }
      } else {
        if(
          wow.dayPeriod == -1 ||
          wow.dayPeriod == null ||
          wow.nightPeriod == -1 ||
          wow.nightPeriod == null ||
          wow.initialState == -1 ||
          wow.initialState == null ||
          wow.finalState == -1 ||
          wow.finalState == null ||
          wow.fuel == -1 ||
          wow.fuel == null
          ){
            this.validWow = false;
            this.validWows.push("false");
            const element: HTMLElement = document.getElementById(wow.id);
            this.renderer.setStyle(element, "background-color", "#BD362F");
          } else {
            this.validWow = true;
            this.validWows.push("true");
          }
        }
     
      /*
      if (
        (wow.dayPeriod == -1 ||
          wow.nightPeriod == -1 ||
          wow.initialState == -1 ||
          wow.finalState == -1 ||
          wow.fuel == -1) &&
        this.withoutMachine == false
      ) {
        this.validWow = false;
        const element: HTMLElement = document.getElementById(wow.id);
        this.renderer.setStyle(element, "background-color", "#BD362F");
      } else if (
        (wow.dayPeriod == null ||
          wow.nightPeriod == null ||
          wow.initialState == null ||
          wow.finalState == null ||
          wow.fuel == null) &&
        this.withoutMachine == false
      ) {
        this.validWow = false;
        const element: HTMLElement = document.getElementById(wow.id);
        this.renderer.setStyle(element, "background-color", "#BD362F");
      } else if (
        (wow.initialState == -1 ||
          wow.finalState == -1 ||
          wow.sumState == -1) &&
        this.withoutMachine == false
      ) {
        this.validWow = false;
        const element: HTMLElement = document.getElementById(wow.id);
        this.renderer.setStyle(element, "background-color", "#BD362F");
      } else if (
        wow.connectingMachine.dbid == -1
      ) {
        this.validWow = true;
      } else if (
        wow.machine.Name === "BEZ MAŠINE"
      ) {
        this.validWow = true;
      } else {
        this.validWow = true;
      }
      */
    });
    console.log(this.validWows)
    console.log(this.validWows.indexOf("false"))
    if(this.validWows.indexOf("false") != -1){
      this.validWow = false;
    } else {
      this.validWow = true;
    }
    this.validWows = [];
    if (this.workOrder.materials.length == 0) {
      this.validWom = true;
    } else {
      this.workOrder.materials.forEach((material) => {
        if (material.spent == -1 && material.deleted != true) {
          this.validWom = false;
          this.validWoms.push("false");
          const element: HTMLElement = document.getElementById(material.id);
          this.renderer.setStyle(element, "background-color", "#BD362F");
        } else {
          this.validWom = true;
          this.validWoms.push("true");
        }
      });
    }
    if(this.validWoms.indexOf("false") != -1){
      this.validWom = false;
    } else {
      this.validWom = true;
    }
    this.validWoms = [];
    if (this.validWow == false && this.validWom == false) {
      this.toastr.error("Unesite učinke izvršioca/mašina i materijala.", "Greška!", {
        positionClass: 'toast-center-center', closeButton: true,  disableTimeOut: true
      });
    } else if (this.validWom == false) {
      this.toastr.error("Unesite učinke materijala.", "Greška!", {
        positionClass: 'toast-center-center', closeButton: true,  disableTimeOut: true
      });
    } else if (this.validWow == false) {
      this.toastr.error("Unesite učinke izvršioca/mašina.", "Greška!", {
        positionClass: 'toast-center-center', closeButton: true,  disableTimeOut: true
      });
    } else if (
      this.validWom == true &&
      this.validWow == true &&
      this.validWoInfo == true
    ) {
      console.log(this.validWow)
      this.spinner.show();
      this.workOrder.treated = this.treatedEntered;
      this.workOrder.numOfRefOrder = this.numOfRefOrderFC.value
      this.workOrder.note = this.noteFC.value;
      this.workOrder.cancellation = false;
      if(this.noOperationOutput){
        this.workOrder.noOperationOutput = true;
      } else {
        this.workOrder.noOperationOutput = false;
      }
      this.workOrderService.closeWorkOrder(this.workOrder).subscribe((res) => {
        console.log(res);
        this.spinner.hide();
        //this.error = true;
        this.canDeactivate.leave = true;
        this.toastr.success("Radni nalog " + this.workOrder.sapId + " je uspešno zatvoren.", "Potrvrda!",{
          positionClass: 'toast-center-center', closeButton: true,  disableTimeOut: false
        });
        this.router.navigate(["/workOrder"]);
      },
        err => {
          console.log(err);
          this.spinner.hide();
          if (err.status == 400) {
            //this.toastr.error("Došlo je do greške prilikom zatvaranja.");
            this.error = true;
            this.errors = err.error;
            this.openErrorsModal.nativeElement.click();
          } else {
            this.toastr.error("Radni nalog " + this.workOrder.sapId + " nije zatvoren.", "Greška!", {
              positionClass: 'toast-center-center', closeButton: true,  disableTimeOut: true
            });
          }
        });
    }
    this.closeButtonCloseWOModal.nativeElement.click();
  }

  /*metode za ponistavanje selectovanih radnika i operacija jer ukoliko se 
  dodan radnik i operacija, a zatim izmene, prilikom dodavanja novog 
  radnika i operacije bice mapirani prethodni, pa se ovom metodom to izbegava
  addEmployee() {
    this.wow = new WorkOrderWorker();
  }

  addDevice() {
    this.machine = new WorkOrderMachine();
  }

  addSubstance() {
    this.spentMaterial = new SpentMaterial();
  }
  */

  getWorkOrder() {
    this.workOrderService.getOne(this.workId).subscribe((data) => {
      this.workOrder = data;
      this.noteFC.setValue(this.workOrder.note)
      this.numOfRefOrderFC.setValue(this.workOrder.numOfRefOrder)
      if (this.workOrder.status == "NEW") {
        this.workOrder.status = "Novi";
      } else if (this.workOrder.status == "IN_PROGRESS") {
        this.workOrder.status = "U radu";
      } else if (this.workOrder.status == "CLOSED") {
        this.workOrder.status = "Zatvoren";
      } else if (this.workOrder.status == "CANCELLATION") {
        this.workOrder.status = "Storniran";
      }

      if(this.workOrder.noOperationOutput){
        this.noOperationOutput = true;
      }

      this.workOrder.date = {
        day: +this.workOrder.date.day,
        month: +this.workOrder.date.month,
        year: +this.workOrder.date.year,
      };

      this.cropService
        .getAllByFieldAndYear(data.tableId, data.year)
        .subscribe((res) => {
          this.crops = res;
        });

      if (this.workOrder.treated == 0) {
        this.workOrder.treated = null;
      }
    });
  }

  //delete methods

  deleteExistingWorkOrderWorker(id) {
    this.spinner.show();
    let dataa;
    this.wowService.deleteWorkOrderWorker(id).subscribe((res) => {
      dataa = this.workOrder.workers.filter(item => item.id == id);
      console.log(dataa)
      dataa[0].deleted = true;
      this.getWorkOrder();
      this.getOneWorkOrder();
      this.spinner.hide();
      this.toastr.success("Uspešno ste izbrisali radnika " + dataa[0].user.Name, "Potvrda!", {
        positionClass: 'toast-center-center', closeButton: true,  disableTimeOut: false
      })

    },
      err => {
        console.log(err);
        this.getOneWorkOrder();
        this.toastr.error("Neuspešno brisanje radnika " + dataa[0].user.Name, "Greška!", {
          positionClass: 'toast-center-center', closeButton: true,  disableTimeOut: true
        })
        this.spinner.hide();
      });
  }

  deleteExistingSpentMaterial(id) {
    this.spinner.show();
    let dataa;
    this.spentMaterialService.deleteSpentMaterial(id).subscribe((res) => {
      dataa = this.workOrder.materials.filter(item => item.id == id);
      dataa[0].deleted = true;
      this.spinner.hide();
      this.toastr.success("Uspešno ste izbrisali materijal " + dataa[0].material.Name, "Potvrda!", {
        positionClass: 'toast-center-center', closeButton: true,  disableTimeOut: false
      })
    },
      err => {
        console.log(err);
        this.toastr.error("Neuspešno brisanje materijala " + dataa[0].material.Name, "Greška!", {
          positionClass: 'toast-center-center', closeButton: true,  disableTimeOut: true
        })
        this.spinner.hide();
      });
  }

  deleteWorkOrderWorker(workOrderWorker) {
    this.wows.forEach((wow) => {
      if (wow.wowObjectId == workOrderWorker.wowObjectId) {
        this.wows.splice(this.wows.indexOf(wow), 1);
      }
    });
  }

  deleteSpentMaterial(spentMaterila) {
    this.woMaterials.forEach((wom) => {
      if (wom.smObjectId == spentMaterila.smObjectId) {
        this.woMaterials.splice(this.woMaterials.indexOf(wom), 1);
      }
    });
  }

  //method for convert json property names to lower case
  convertKeysToLowerCase(obj) {
    var output = [];
    for (let i in obj) {
      if (Object.prototype.toString.apply(obj[i]) === "[object Object]") {
        output[i.toLowerCase()] = this.convertKeysToLowerCase(obj[i]);
      } else if (Object.prototype.toString.apply(obj[i]) === "[object Array]") {
        output[i.toLowerCase()] = [];
        output[i.toLowerCase()].push(this.convertKeysToLowerCase(obj[i][0]));
      } else {
        output[i.toLowerCase()] = obj[i];
      }
    }
    return output;
  }

  setWowId(id) {
    this.workOrderWorkerId = id;
    console.log(this.workOrderWorkerId)
  }

  setMaterialId(id) {
    this.materialId = id;
    console.log(this.materialId);
  }

  cancellationWorkOrder() {
    this.workOrder.cancellation = true;
    console.log(this.workOrder)
    this.spinner.show();
    this.workOrderService.closeWorkOrder(this.workOrder).subscribe((res) => {
      console.log(res);
      this.spinner.hide();
      //this.error = true;
      this.canDeactivate.leave = true;
      this.toastr.success("Uspešno storniran radni nalog " + this.workOrder.sapId, "Potvrda!", {
        positionClass: 'toast-center-center', closeButton: true,  disableTimeOut: false
      });
      this.router.navigate(["/workOrder"]);
    },
      err => {
        console.log(err);
        this.spinner.hide();
        if (err.status == 400) {
          //this.toastr.error("Došlo je do greške prilikom storniranja. Za detalje kliknite na uzvičnik.");
          this.error = true;
          this.errors = err.error;
          this.openErrorsModal.nativeElement.click();
        } else {
          this.toastr.error("Radni nalog " + this.workOrder.sapId + " nije storniran.", "Greška!", {
            positionClass: 'toast-center-center', closeButton: true,  disableTimeOut: true
          });
        }
      });
  }

  editWow(wow){
    console.log(wow)
    this.wowMob = wow;
    this.mob = true;
    console.log(this.wowMob)
    this.editWowMobFlag = true;
    this.workerFC.setValue(wow.user);
    this.workerOperationFC.setValue(wow.operation);
    this.workerMachineFC.setValue(wow.machine);
    this.workerCoMachineFC.setValue(wow.connectingMachine)
  }

  addNewWowMob(){
    this.editWowMobFlag = false;
    this.workerFC = new FormControl("");
    this.workerOperationFC.setValue(this.operationFC.value);
    this.workerMachineFC = new FormControl("");
    this.workerCoMachineFC = new FormControl("");
  }

  addNewMaterialMob(){
    this.mob = true;
    this.editMaterialMobFlag = false;
    this.materialFC = new FormControl("");
    this.quantityEntered = null;
  }

  editMaterialMob(material){
    this.materialMob = material;
    this.mob = true;
    this.editMaterialMobFlag = true;
    this.idOfEditingMaterial = material.smObjectId;
    this.materialFC.setValue(material.material);
    this.quantityEntered = material.quantity;
  }

  disableTreated(){
    this.noOperationOutput = !this.noOperationOutput;
    console.log(this.noOperationOutput)
  }

  checkIfStartedCreation(){
    if(
      this.workerFC.value != "" &&
      this.workerOperationFC.value != "" &&
      this.workerMachineFC.value != "" &&
      this.workerCoMachineFC.value != ""
    ) {
      this.startedCreationWorker = true;
    } else {
      console.log("ELSE")
      this.startedCreationWorker = false;
    }
    if(this.materialFC.value != "" && this.quantityEntered != null){
      this.startedCreationMaterial = true;
    } else {
      this.startedCreationMaterial = false;
    }
    if(this.startedCreationWorker || this.startedCreationMaterial){
      this.saveStartedCreation.nativeElement.click();
    }
    if(!this.startedCreationWorker && !this.startedCreationMaterial){
      this.updateWorkOrder();
    }
  }

  addWorkerAndUpdateWo(){
      if(this.startedCreationWorker && !this.startedCreationMaterial){
        if(this.addWow && this.validAnswerWow){
          this.errWow = false;
          this.addNewWorkerAndMachine();
          this.updateWorkOrder();
          this.workerFC = new FormControl("");
          this.workerOperationFC = new FormControl("");
          this.workerMachineFC = new FormControl("");
          this.workerCoMachineFC = new FormControl("");  
          this.closeSaveStartedModal.nativeElement.click();
        } else if(!this.addWow && this.validAnswerWow){
          this.errWow = false;
          this.updateWorkOrder();
          this.workerFC = new FormControl("");
          this.workerOperationFC = new FormControl("");
          this.workerMachineFC = new FormControl("");
          this.workerCoMachineFC = new FormControl("");  
          this.closeSaveStartedModal.nativeElement.click();
        } else if(!this.validAnswerWow) {
          this.errWow = true;
          console.log("ELSE MSG TRUE")
          this.msgInvalidAnswerWow = "Morate izabrati da li želite da dodate radnika, klikom na dugme Da ili Ne!"
        }
      }
      else if(this.startedCreationMaterial && !this.startedCreationWorker){
        if(this.addMat && this.validAnswerMat){
          this.errMat = false;
          this.addNewMaterial();
          this.updateWorkOrder();
          this.materialFC = new FormControl("");
          this.quantityEntered = null;
          this.closeSaveStartedModal.nativeElement.click();
        } else if(!this.addMat && this.validAnswerMat){
          this.errMat = false;
          this.updateWorkOrder();
          this.materialFC = new FormControl("");
          this.quantityEntered = null;
          this.closeSaveStartedModal.nativeElement.click();
        } else if(!this.validAnswerMat) {
          this.errMat = true;
          this.msgInvalidAnswerMat = "Morate izabrati da li želite da dodate materijale, klikom na dugme Da ili Ne!"
        }
      } 
      else if(this.startedCreationWorker && this.startedCreationMaterial){
        if(this.addMat && this.addWow && this.validAnswerWow && this.validAnswerMat){
          this.errMat = false;
          this.errWow = false;
          this.addNewWorkerAndMachine();
          this.addNewMaterial();
          this.updateWorkOrder();
          this.workerFC = new FormControl("");
          this.workerOperationFC = new FormControl("");
          this.workerMachineFC = new FormControl("");
          this.workerCoMachineFC = new FormControl("");  
          this.materialFC = new FormControl("");
          this.quantityEntered = null;
          this.closeSaveStartedModal.nativeElement.click();
        } else if(this.addMat && !this.addWow && this.validAnswerWow && this.validAnswerMat){
          this.errMat = false;
          this.errWow = false;
          this.addNewMaterial();
          this.updateWorkOrder();
          this.workerFC = new FormControl("");
          this.workerOperationFC = new FormControl("");
          this.workerMachineFC = new FormControl("");
          this.workerCoMachineFC = new FormControl("");  
          this.materialFC = new FormControl("");
          this.quantityEntered = null;
          this.closeSaveStartedModal.nativeElement.click();
        } else if(!this.addMat && this.addWow && this.validAnswerWow && this.validAnswerMat){
          this.errMat = false;
          this.errWow = false;
          this.addNewWorkerAndMachine();
          this.updateWorkOrder();
          this.workerFC = new FormControl("");
          this.workerOperationFC = new FormControl("");
          this.workerMachineFC = new FormControl("");
          this.workerCoMachineFC = new FormControl("");  
          this.materialFC = new FormControl("");
          this.quantityEntered = null;
          this.closeSaveStartedModal.nativeElement.click();
        } else if (!this.addMat && !this.addWow && this.validAnswerWow && this.validAnswerMat){
          this.errMat = false;
          this.errWow = false;
          this.workerFC = new FormControl("");
          this.workerOperationFC = new FormControl("");
          this.workerMachineFC = new FormControl("");
          this.workerCoMachineFC = new FormControl("");  
          this.materialFC = new FormControl("");
          this.quantityEntered = null;
          this.updateWorkOrder();
          this.closeSaveStartedModal.nativeElement.click();
        } else if(!this.validAnswerWow && !this.validAnswerMat){
          this.errMat = true;
          this.errWow = true;
          console.log("TWO ERR")
          this.msgInvalidAnswerWow = "Morate izabrati da li želite da dodate radnika, klikom na dugme Da ili Ne!"
          this.msgInvalidAnswerMat = "Morate izabrati da li želite da dodate materijale, klikom na dugme Da ili Ne!"
        } else if(!this.validAnswerWow){
          this.errWow = true;
          this.msgInvalidAnswerWow = "Morate izabrati da li želite da dodate radnika, klikom na dugme Da ili Ne!"
        } else if(!this.validAnswerMat){
          this.errMat = true;
          this.msgInvalidAnswerMat = "Morate izabrati da li želite da dodate materijale, klikom na dugme Da ili Ne!"
        }
      }
      
    
  }

  getAnswerWorker(value){
    if(value == "workerYes"){
      this.addWow = true;
      this.validAnswerWow = true;
    } else if(value == "workerNo"){
      this.addWow = false;
      this.validAnswerWow = true;
    } else {
      this.validAnswerWow = false;
    }
    console.log("Worker = " + value)
  }

  getAnswerMaterial(value){
    if(value == "materialYes"){
      this.addMat = true;
      this.validAnswerMat = true;
    } else if(value == "materialNo"){
      this.addMat = false;
      this.validAnswerMat = true;
    } else {
      this.validAnswerMat = false;
    }
    console.log("Mat = " + value)
  }

 
}
