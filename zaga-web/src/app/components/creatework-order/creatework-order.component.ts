import { Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { Worker } from "src/app/models/Worker";
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
import { CultureService } from "src/app/service/culture.service";
import { Culture } from "src/app/models/Culture";
import { Machine } from "src/app/models/Machine";
import { MachineService } from "src/app/service/machine.service";
import { MaterialService } from "src/app/service/material.service";
import { FormControl } from "@angular/forms";
import { Observable } from "rxjs";
import { Field } from "src/app/models/Field";
import { FieldService } from "src/app/service/field.service";
import { DeviceDetectorService } from "ngx-device-detector";
import { CropService } from "src/app/service/crop.service";
import { MachineState } from "src/app/models/MachineState";
import { SpentMaterial } from "src/app/models/SpentMaterial";

@Component({
  selector: "app-creatework-order",
  templateUrl: "./creatework-order.component.html",
  styleUrls: ["./creatework-order.component.css"],
})
export class CreateworkOrderComponent implements OnInit {
  constructor(
    private route: ActivatedRoute,
    private toastr: ToastrService,
    private router: Router,
    private userService: UserService,
    private operationService: OperationService,
    private cultureService: CultureService,
    private machineService: MachineService,
    private materialService: MaterialService,
    private workOrderService: WorkOrderService,
    private fieldService: FieldService,
    private cropService: CropService,
    private deviceService: DeviceDetectorService
  ) {
    this.router.routeReuseStrategy.shouldReuseRoute = () => false;
  }

  modalHeader = "Dodavanje radnika";
  headerMachine = "Dodavanje mašine";
  headerMaterial = "Dodavanje materijala";
  workers = false;
  machines = false;
  materials = false;
  editing = false;
  editingMachine = false;
  editingMaterial = false;
  workId = this.route.snapshot.params.workId;

  query = "";
  material: SpentMaterial = new SpentMaterial();
  idOfEditingMaterial: any = 0;
  idOfEditingWorker: any = 0;
  idOfEditingMachine: any = 0;
  new = false;
  inputFill = true;
  workerMob: Worker = new Worker();
  allEmployees: Employee[] = [];
  operations: Operation[] = [];
  cultures: Culture[] = [];
  devices: Machine[] = [];
  substances: Material[] = [];
  fields: Field[] = [];
  crops: Crop[] = [];

  field: Field = new Field();
  operation: Operation = new Operation();
  crop: Crop = new Crop();
  culture: Culture = new Culture();
  employee: Employee = new Employee();
  workOrder: WorkOrder = new WorkOrder();
  worker: Worker = new Worker();
  employees: Employee[] = [];
  machine: MachineState = new MachineState();
  woMachines: MachineState[] = [];
  woMaterials: SpentMaterial[] = [];
  cultureId;
  selectedWorker = "-1";
  selectedMachine = "-1";
  selectedMaterial;
  selectedWorkerForMachine = "-1";
  workerss: Worker[] = [];
  selectedOperation;
  selectedTable;
  selectedYear = 2020;
  selectedCrop;
  clickAddMaterial = false;
  clickAddWorkOrder = false;

  nameFC: FormControl = new FormControl("");

  filteredOptions: Observable<string[]>;

  ngOnInit() {
    if (this.workId == "new") {
      //new
      this.new = true;
      this.workOrder = new WorkOrder();
      this.workOrder.machines = [];
      this.workOrder.workers = [];
      this.workOrder.materials = [];
      this.workOrder.status = "Novi";
    } else {
      this.new = false;

      this.workOrderService.getOne(this.workId).subscribe((data) => {
        this.workOrder = data;

        if (this.workOrder.status == "NEW") {
          this.workOrder.status = "Novi";
        } else if (this.workOrder.status == "IN_PROGRESS") {
          this.workOrder.status = "U radu";
        } else if (this.workOrder.status == "CLOSED") {
          this.workOrder.status = "Zatvoren";
        }

        this.workOrder.start = {
          day: +this.workOrder.start.substring(8, 10),
          month: +this.workOrder.start.substring(5, 7),
          year: +this.workOrder.start.substring(0, 4),
        };
        this.workOrder.end = {
          day: +this.workOrder.end.substring(8, 10),
          month: +this.workOrder.end.substring(5, 7),
          year: +this.workOrder.end.substring(0, 4),
        };

        this.workOrder.workers.forEach((data) => {
          this.worker = data;
          this.getWorker(this.worker.userId);
        });
      });
    }

    this.userService.getAll().subscribe((data) => {
      this.allEmployees = data.content;
    });

    this.operationService.getAll().subscribe((data) => {
      this.operations = data;
    });
    /*
    this.cropService.getAll().subscribe(data=>{
      this.crops = data;
    })
    */
    this.cultureService.getAll().subscribe((data) => {
      this.cultures = data;
    });
    this.machineService.getAll().subscribe((data) => {
      this.devices = data;
    });

    this.materialService.getAll().subscribe((data) => {
      this.substances = data;
    });

    this.fieldService.getAll().subscribe((data) => {
      this.fields = data;
    });
  }

  getArea() {
    this.cropService.getOne(this.workOrder.cropId).subscribe((data) => {
      this.crop = data;
      this.crop.area = data.Area;
    });
  }

  getOperation() {
    this.selectedOperation =
      this.workOrder.operationId.split("&")[1] +
      "&" +
      this.workOrder.operationId.split("&")[0];
  }

  getWorker(id) {
    this.userService.getOne(id).subscribe((data) => {
      this.employee = data;
      this.employees.push(this.employee);
    });
  }

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

  addWorker() {
    this.worker.userId = this.selectedWorker.split("&")[0];
    this.worker.name = this.selectedWorker.split("&")[1];
    this.worker.operationId = this.selectedOperation.split("&")[0];
    this.worker.operation = this.selectedOperation.split("&")[1];
    if (this.worker.userId != "-1") {
      this.workerss.push(this.worker);
      this.worker = new Worker();
      this.selectedWorker = "-1";
      this.selectedOperation =
        this.workOrder.operationId.split("&")[1] +
        "&" +
        this.workOrder.operationId.split("&")[0];
    } else {
      this.toastr.error("Izaberite radnika.");
    }
  }

  editWorker(worker) {
    this.worker.userId = worker.userId;
    this.worker.name = worker.name;
    this.editing = true;
    this.idOfEditingWorker = worker.userId;
    this.selectedWorker = worker.userId + "&" + worker.name;
    this.selectedOperation = worker.operationId + "&" + worker.operation;
  }

  editExistingWorker() {
    this.workerss.forEach((worker) => {
      if (worker.userId == this.idOfEditingWorker) {
        worker.userId = this.selectedWorker.split("&")[0];
        worker.name = this.selectedWorker.split("&")[1];
        worker.operationId = this.selectedOperation.split("&")[0];
        worker.operation = this.selectedOperation.split("&")[1];
      }
    });
    this.worker = new Worker();
    this.editing = false;
  }

  addMachine() {
    this.machine.machineId = this.selectedMachine.split("&")[0];
    this.machine.machineName = this.selectedMachine.split("&")[1];
    this.machine.userId = this.selectedWorkerForMachine.split("&")[0];
    this.machine.userName = this.selectedWorkerForMachine.split("&")[1];
    if (this.machine.machineId != "-1" && this.machine.userId != "-1") {
      this.woMachines.push(this.machine);
      this.machine = new MachineState();
      this.selectedMachine = "-1";
      this.selectedWorkerForMachine = "-1";
    } else if (this.machine.machineId == "-1") {
      if (this.machine.userId == "-1") {
        this.toastr.error("Izaberite mašinu i radnika.");
      } else {
        this.toastr.error("Izaberite mašinu.");
      }
    } else {
      this.toastr.error("Izaberite radnika.");
    }
  }

  editMachine(machine) {
    this.machine.machineId = machine.machineId;
    this.machine.machineName = machine.machineName;
    this.editingMachine = true;
    this.idOfEditingMachine = machine.machineId;
    this.selectedMachine = machine.machineId + "&" + machine.machineName;
    this.selectedWorkerForMachine = machine.userId + "&" + machine.userName;
  }

  editExistingMachine() {
    this.woMachines.forEach((machine) => {
      if (machine.machineId == this.idOfEditingMachine) {
        machine.machineName = this.selectedMachine.split("&")[1];
        machine.machineId = this.selectedMachine.split("&")[0];
        machine.userId = this.selectedWorkerForMachine.split("&")[0];
        machine.userName = this.selectedWorkerForMachine.split("&")[1];
      }
    });
    this.machine = new MachineState();
    this.editingMachine = false;
  }

  addMaterial(valid) {
    if (this.selectedMaterial != null) {
      this.material.materialId = this.selectedMaterial.split("&")[0];
      this.material.materialName = this.selectedMaterial.split("&")[1];
    }
    if (valid) {
      this.getArea();
      this.material.quantityPerHectar = this.material.quantity / this.crop.area;
      this.woMaterials.push(this.material);
      this.selectedMaterial = null;
      this.material = new SpentMaterial();
    }
    this.clickAddMaterial = true;
  }

  editMaterial(material) {
    this.material.materialId = material.materialId;
    this.material.materialName = material.materialName;
    this.editingMaterial = true;
    this.idOfEditingMaterial = material.materialId;
    this.selectedMaterial = material.materialId + "&" + material.materialName;
    this.material.quantity = material.quantity;
    this.material.unit = material.unit;
  }

  editExistingMaterial() {
    this.woMaterials.forEach((material) => {
      if (material.materialId == this.idOfEditingMaterial) {
        material.materialId = this.selectedMaterial.split("&")[0];
        material.materialName = this.selectedMaterial.split("&")[1];
        material.quantity = this.material.quantity;
        material.unit = this.material.unit;
        this.getArea();
        this.material.quantityPerHectar =
          this.material.quantity / this.crop.area;
      }
    });
    this.material = new SpentMaterial();
    this.editingMaterial = false;
  }

  addWorkOrder(valid) {
    var dateStartToAdd = "";
    var dateEndToAdd = "";
    if (this.workOrder.start != undefined) {
      if (this.workOrder.start.month < 10) {
        this.workOrder.start.month = "0" + this.workOrder.start.month;
      }
      if (this.workOrder.start.day < 10) {
        this.workOrder.start.day = "0" + this.workOrder.start.day;
      }
      dateStartToAdd =
        this.workOrder.start.year +
        "-" +
        this.workOrder.start.month +
        "-" +
        this.workOrder.start.day;
    }
    if (this.workOrder.end != undefined) {
      if (this.workOrder.end.month < 10) {
        this.workOrder.end.month = "0" + this.workOrder.end.month;
      }
      if (this.workOrder.end.day < 10) {
        this.workOrder.end.day = "0" + this.workOrder.end.day;
      }
      dateEndToAdd =
        this.workOrder.end.year +
        "-" +
        this.workOrder.end.month +
        "-" +
        this.workOrder.end.day;
    }

    this.workOrder.start = dateStartToAdd;
    this.workOrder.end = dateEndToAdd;
    //this.workOrder.operationId = this.workOrder.operationId.split("&")[1];

    this.workOrder.machines = this.woMachines;
    this.workOrder.workers = this.workerss;
    this.workOrder.materials = this.woMaterials;

    this.workOrder.responsibleId = this.nameFC.value.userId;
    this.clickAddWorkOrder = true;

    /*
    this.workOrderService.addWorkOrder(this.workOrder).subscribe(
      (data) => {
        this.toastr.success("Uspešno kreiran radni nalog.");
        this.router.navigate(["/workOrder"]);
      },
      (error) => {
        this.toastr.error("Radni nalog nije kreiran.");
      }
    );
      */
    console.log(valid);
  }

  updateWorkOrder() {
    var dateStartToAdd = "";
    var dateEndToAdd = "";
    if (this.workOrder.start != undefined) {
      if (this.workOrder.start.month < 10) {
        this.workOrder.start.month = "0" + this.workOrder.start.month;
      }
      if (this.workOrder.start.day < 10) {
        this.workOrder.start.day = "0" + this.workOrder.start.day;
      }
      dateStartToAdd =
        this.workOrder.start.year +
        "-" +
        this.workOrder.start.month +
        "-" +
        this.workOrder.start.day;
    }
    if (this.workOrder.end != undefined) {
      if (this.workOrder.end.month < 10) {
        this.workOrder.end.month = "0" + this.workOrder.end.month;
      }
      if (this.workOrder.end.day < 10) {
        this.workOrder.end.day = "0" + this.workOrder.end.day;
      }
      dateEndToAdd =
        this.workOrder.end.year +
        "-" +
        this.workOrder.end.month +
        "-" +
        this.workOrder.end.day;
    }

    this.workOrder.start = dateStartToAdd;
    this.workOrder.end = dateEndToAdd;
    this.workOrder.operationId = this.workOrder.operationId.split("&")[0];

    this.workOrderService.updateWorkOrder(this.workOrder).subscribe(
      (data) => {
        this.toastr.success("Uspešno sačuvan radni nalog.");
      },
      (error) => {
        this.toastr.error("Radni nalog nije sačuvan.");
      }
    );

    console.log(this.workOrder);
  }

  setForEdit(wor) {
    this.workerMob = wor;
  }

  displayFn(emp: Employee): string {
    return emp && emp.name ? emp.name : "";
  }

  addEmployee() {
    this.workerMob = new Worker();
  }

  getCulture() {
    this.cropService
      .getAllByFieldAndYear(this.selectedTable, this.selectedYear)
      .subscribe((data) => {
        this.crops = data;
      });
  }
}
