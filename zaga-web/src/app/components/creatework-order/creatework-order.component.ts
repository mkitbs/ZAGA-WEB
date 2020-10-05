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
import { WorkOrderWorker } from "src/app/models/WorkOrderWorker";
import { ViewChild } from "@angular/core";
import { WorkOrderWorkerService } from "src/app/service/work-order-worker.service";
import { WorkOrderMachine } from "src/app/models/WorkOrderMachine";
import { WorkOrderMachineService } from "src/app/service/work-order-machine.service";
import { SpentMaterialService } from "src/app/service/spent-material.service";
import { throwMatDialogContentAlreadyAttachedError } from "@angular/material";

@Component({
  selector: "app-creatework-order",
  templateUrl: "./creatework-order.component.html",
  styleUrls: ["./creatework-order.component.css"],
})
export class CreateworkOrderComponent implements OnInit {
  @ViewChild("closeButtonMachineModal", null) closeButtonMachineModal;
  @ViewChild("closeButtonMaterialModal", null) closebuttonMaterialModal;
  @ViewChild("closeButtonWorkerModal", null) closeButtonWorkerModal;

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
    private deviceService: DeviceDetectorService,
    private wowService: WorkOrderWorkerService,
    private womService: WorkOrderMachineService,
    private spentMaterialService: SpentMaterialService
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
  editingMachineState = false;
  editingWorker = false;
  editingSpentMaterial = false;
  workId = this.route.snapshot.params.workId;

  query = "";
  material: SpentMaterial = new SpentMaterial();
  idOfEditingMaterial: any = 0;
  idOfEditingWorkerMachine: any = 0;
  idOfEditingMachine: any = 0;
  new = false;
  inputFill = true;
  workerMob: Worker = new Worker();
  allEmployees: Employee[] = [];
  operations: Operation[] = [];
  cultures: Culture[] = [];
  devicesPropulsion: Machine[] = [];
  devicesCoupling: Machine[] = [];
  substances: Material[] = [];
  fields: Field[] = [];
  crops: Crop[] = [];
  wow: WorkOrderWorker = new WorkOrderWorker();
  wom: WorkOrderMachine = new WorkOrderMachine();
  spentMaterial: SpentMaterial = new SpentMaterial();

  field: Field = new Field();
  operation: Operation = new Operation();
  crop: Crop = new Crop();
  culture: Culture = new Culture();
  employee: Employee = new Employee();
  workOrder: WorkOrder = new WorkOrder();
  worker: WorkOrderWorker = new WorkOrderWorker();
  employees: Employee[] = [];
  machine: WorkOrderMachine = new WorkOrderMachine();
  woMachines: WorkOrderMachine[] = [];
  woMaterials: SpentMaterial[] = [];
  wows: WorkOrderWorker[] = [];
  cultureId;
  selectedWorkerForMachine;
  workerss: WorkOrderWorker[] = [];
  selectedTable;
  selectedYear = 2020;
  selectedCrop;
  selectedCouplingMachine;
  clickAddMaterial = false;
  clickAddWorkOrder = false;
  clickAddWorkerMachine = false;
  clickAddMachine = false;
  addNewWow = false;
  addNewSpentMaterial = false;

  unit;
  exists = false;

  nameFC: FormControl = new FormControl("");

  filteredOptions: Observable<string[]>;

  ngOnInit() {
    if (this.workId == "new") {
      //new
      this.new = true;
      this.workOrder = new WorkOrder();
      this.workOrder.workers = [];
      this.workOrder.materials = [];
      this.workOrder.status = "Novi";
    } else {
      this.new = false;

      this.workOrderService.getOne(this.workId).subscribe((data) => {
        this.workOrder = data;

        console.log(this.workOrder);
        if (this.workOrder.status == "NEW") {
          this.workOrder.status = "Novi";
        } else if (this.workOrder.status == "IN_PROGRESS") {
          this.workOrder.status = "U radu";
        } else if (this.workOrder.status == "CLOSED") {
          this.workOrder.status = "Zatvoren";
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
      });
    }

    this.userService.getAll().subscribe((data) => {
      this.allEmployees = data.content;
      var comparableId = this.workOrder.responsibleId;
      var filterById = function (element) {
        return element.userId == comparableId;
      };

      this.nameFC.setValue(this.allEmployees.filter(filterById)[0]);
    });

    this.operationService.getAll().subscribe((data) => {
      data = this.convertKeysToLowerCase(data);
      this.operations = data;
    });

    this.cultureService.getAll().subscribe((data) => {
      this.cultures = data;
    });
    this.machineService.getAllPropulsion().subscribe((data) => {
      data = this.convertKeysToLowerCase(data);
      this.devicesPropulsion = data;
    });

    this.machineService.getAllCoupling().subscribe((data) => {
      data = this.convertKeysToLowerCase(data);
      this.devicesCoupling = data;
    });

    this.materialService.getAll().subscribe((data) => {
      data = this.convertKeysToLowerCase(data);
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
      this.workOrder.area = data.Area;
    });
  }

  getOperation() {
    this.wow.operation.id = this.workOrder.operationId;
  }

  getWorker(id) {
    this.userService.getOne(id).subscribe((data) => {
      this.employee = data;
      this.employees.push(this.employee);
    });
  }

  getUnitOfMaterial(id) {
    this.unit = this.substances.find((x) => x.id == id).unit;
    this.woMaterials.forEach((material) => {
      if (material.material.id == id) {
        material.material.unit = this.unit;
      }
    });
    console.log(this.unit);
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

  addWorkerAndMachine(valid) {
    this.clickAddWorkerMachine = true;
    this.wow.wowObjectId = Math.floor(Math.random() * 100 + 1);
    if (valid) {
      this.wows.forEach((wow) => {
        if (
          wow.user.id == this.wow.user.id &&
          wow.operation.id == this.wow.operation.id &&
          wow.machine.id == this.wow.machine.id
        ) {
          this.toastr.error("Radnik, operacija i mašina su već dodati");
          this.exists = true;
        }
      });
      if (!this.exists) {
        this.wows.push(this.wow);
        this.wow = new WorkOrderWorker();
        this.exists = false;
      }
      this.exists = false;
      this.clickAddWorkerMachine = false;
    }

    console.log(this.wows);
  }

  editWorkerAndMachine(wow) {
    this.wow.dayPeriod = wow.dayPeriod;
    this.wow.nightPeriod = wow.nightPeriod;
    this.wow.initialState = wow.initialState;
    this.wow.finalState = wow.finalState;
    this.wow.fuel = wow.fuel;
    this.idOfEditingWorkerMachine = this.wow.wowObjectId;
    if (!this.new) {
      this.wow.id = wow.id;
      this.wow = wow;
    }
  }

  editExistingWorkerAndMachine(existing) {
    this.idOfEditingWorkerMachine = existing.wowObjectId;
    this.wows.forEach((wow) => {
      if (wow.wowObjectId == this.idOfEditingWorkerMachine) {
        wow.user.id = this.allEmployees.find(
          (x) => x.userId == wow.user.id
        ).userId;
        wow.machine.id = this.devicesPropulsion.find(
          (x) => x.id == wow.machine.id
        ).id;
        wow.connectingMachine.id = this.devicesCoupling.find(
          (x) => x.id == wow.connectingMachine.id
        ).id;
        wow.operation.id = this.operations.find(
          (x) => x.id == wow.operation.id
        ).id;
        wow.dayPeriod = this.wow.dayPeriod;
        wow.nightPeriod = this.wow.nightPeriod;
        wow.initialState = this.wow.initialState;
        wow.finalState = this.wow.finalState;
        wow.fuel = this.wow.fuel;
        this.toastr.success("Uspešno izvršena promena.");
      }
    });
    console.log(this.wows);
  }

  addDetailWow() {
    if (this.new) {
      this.addNewWow = false;
    } else {
      this.addNewWow = true;
    }
    this.wow.dayPeriod = null;
    this.wow.nightPeriod = null;
    this.wow.initialState = null;
    this.wow.finalState = null;
  }

  addNewWorkerAndMachine() {
    this.wowService.addWorker(this.wow, this.workId).subscribe((res) => {
      console.log(res);
      this.wow = new WorkOrderWorker();
      this.workOrderService.getOne(this.workId).subscribe((data) => {
        this.workOrder = data;
      });
    });
  }

  updateWow(wow) {
    this.wowService.updateWorkOrderWorker(wow.id, wow).subscribe((res) => {
      console.log(res);
      this.toastr.success("Uspešno sačuvane promene.");
    });
  }

  addMaterial(valid) {
    this.clickAddMaterial = true;
    this.spentMaterial.smObjectId = Math.floor(Math.random() * 100 + 1);
    this.spentMaterial.material.unit = this.unit;
    if (valid) {
      this.woMaterials.forEach((material) => {
        if (
          material.material.id == this.spentMaterial.material.id &&
          material.quantity == this.spentMaterial.quantity
        ) {
          this.toastr.error("Materijal sa izabranom količinom je već unet.");
          this.exists = true;
        }
      });
      if (!this.exists) {
        this.woMaterials.push(this.spentMaterial);
        this.spentMaterial = new SpentMaterial();
        this.exists = false;
      }
      this.exists = false;
      this.unit = null;
      this.clickAddMaterial = false;
    }

    console.log(this.woMaterials);
  }

  editMaterial(material) {
    if (material.id == null) {
      this.spentMaterial.spent = material.spent;
      this.spentMaterial.material.unit = material.material.unit;
      this.unit = this.spentMaterial.material.unit;
      this.idOfEditingMaterial = this.material.smObjectId;
    } else {
      this.spentMaterial.spent = material.spent;
      this.spentMaterial.material.unit = material.material.Unit;
      console.log(this.spentMaterial.material.unit);
      this.spentMaterial.spentPerHectar = material.spentPerHectar;
      this.spentMaterial.id = material.id;
      this.spentMaterial = material;
    }
  }

  editExistingMaterial(existing) {
    this.idOfEditingMaterial = existing.smObjectId;
    this.woMaterials.forEach((material) => {
      if (material.smObjectId == this.idOfEditingMaterial) {
        material.material.id = this.substances.find(
          (x) => x.id == material.material.id
        ).id;
        material.quantity = this.spentMaterial.quantity;
      }
    });
    this.toastr.success("Uspešno izvršena promena.");
  }

  addDetailMaterial() {
    if (this.new) {
      this.addNewSpentMaterial = false;
    } else {
      this.addNewSpentMaterial = true;
      this.spentMaterial.material.unit = this.unit;
    }
    this.spentMaterial.spent = null;
  }

  addNewMaterial() {
    this.spentMaterialService
      .addSpentMaterial(this.workId, this.spentMaterial)
      .subscribe((res) => {
        console.log(res);
        this.spentMaterial = new SpentMaterial();
        this.workOrderService.getOne(this.workId).subscribe((data) => {
          this.workOrder = data;
        });
      });
  }

  updateMaterial(spentMaterial) {
    this.spentMaterialService
      .updateSpentMaterial(spentMaterial.id, spentMaterial)
      .subscribe((res) => {
        console.log(res);
        this.toastr.success("Uspešno sačuvane promene.");
      });
  }

  addWorkOrder(valid) {
    this.clickAddWorkOrder = true;
    if (valid) {
      this.clickAddWorkOrder = false;
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

      this.workOrder.responsibleId = this.nameFC.value.userId;

      this.workOrderService.addWorkOrder(this.workOrder).subscribe(
        (data) => {
          this.toastr.success("Uspešno kreiran radni nalog.");
          this.router.navigate(["/workOrder"]);
        },
        (error) => {
          this.toastr.error("Radni nalog nije kreiran.");
        }
      );

      console.log(this.workOrder);
    }
  }

  updateWorkOrder() {
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

    this.workOrder.responsibleId = this.nameFC.value.userId;

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

  /*metoda za ponistavanje selectovanih radnika i operacija jer ukoliko se 
  dodan radnik i operacija, a zatim izmene, prilikom dodavanja novog 
  radnika i operacije bice selektovani prethodni, pa se ovom metodom to izbegava*/
  addEmployee() {
    this.worker = new WorkOrderWorker();
  }

  addDevice() {
    this.machine = new WorkOrderMachine();
  }

  addSubstance() {
    this.material = new SpentMaterial();
  }

  getCulture() {
    this.cropService
      .getAllByFieldAndYear(this.workOrder.tableId, this.selectedYear)
      .subscribe((data) => {
        console.log(data);
        this.crops = data;
      });
  }

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

  editMachineState(machine) {
    this.machine.id = machine.id;
    this.machine.machine.id = machine.machine.id;
    this.machine.machine.name = machine.machine.Name;
    this.machine.initialState = machine.initialState;
    this.machine.finalState = machine.finalState;
    this.machine.fuel = machine.fuel;
    this.editingMachineState = true;
    this.idOfEditingMachine = machine.machine.id;
    this.wom.machine.id = machine.machine.id;
    this.wom.user.id = machine.user.id;
    this.wom.initialState = machine.initialState;
    this.wom.finalState = machine.finalState;
    this.wom.fuel = machine.fuel;
  }

  addMachineState() {
    this.wom.id = this.machine.id;

    this.womService.addMachineState(this.wom, this.workId).subscribe((res) => {
      console.log(res);
    });
    /*
    this.workOrder.machines.forEach((machine) => {
      if (machine.machine.id == this.idOfEditingMachine) {
        machine.machine.name = this.wom.machine.name;
        machine.machine.id = this.wom.machine.id;
        machine.user.id = this.wom.user.id;
        machine.user.name = this.wom.user.name;
        machine.initialState = this.wom.initialState;
        machine.finalState = this.wom.finalState;
        machine.sumState = this.wom.finalState - this.wom.initialState;
        machine.fuel = this.wom.fuel;
      }
    });
    */
    this.editingMachineState = false;
  }

  editSpentMaterial(material) {
    this.spentMaterial.id = material.id;
    this.spentMaterial.material.id = material.material.id;
    this.spentMaterial.quantity = material.quantity;
    this.spentMaterial.quantityPerHectar = material.quantityPerHectar;
    this.spentMaterial.spent = material.spent;
    this.spentMaterial.spentPerHectar = material.spentPerHectar;
    this.spentMaterial.material.unit = material.material.Unit;
    this.editingSpentMaterial = true;
    this.idOfEditingMaterial = material.material.id;
  }

  addSpentMaterial() {
    this.spentMaterialService
      .addSpentMaterial(this.workId, this.spentMaterial)
      .subscribe((res) => {
        console.log(res);
      });

    this.workOrder.materials.forEach((material) => {
      if (material.material.id == this.idOfEditingMaterial) {
        material.material.id = this.spentMaterial.material.id;
        material.material.unit = this.spentMaterial.material.unit;
        material.quantity = this.spentMaterial.quantity;
        material.quantityPerHectar =
          this.spentMaterial.quantity / this.workOrder.area;
        material.spent = this.spentMaterial.spent;
        material.spentPerHectar =
          this.spentMaterial.spent / this.workOrder.area;
      }
    });
    this.editingSpentMaterial = false;
  }
}
