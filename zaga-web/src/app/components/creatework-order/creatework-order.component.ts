import { Component, OnInit } from "@angular/core";
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
import { FormControl } from "@angular/forms";
import { Observable } from "rxjs";
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
  new = false;
  workId = this.route.snapshot.params.workId;

  idOfEditingMaterial: any = 0;
  idOfEditingWorkerMachine: any = 0;

  allEmployees: Employee[] = [];
  operations: Operation[] = [];
  devicesPropulsion: Machine[] = [];
  devicesCoupling: Machine[] = [];
  substances: Material[] = [];
  fields: Field[] = [];
  crops: Crop[] = [];
  woMaterials: SpentMaterial[] = [];
  wows: WorkOrderWorker[] = [];

  wow: WorkOrderWorker = new WorkOrderWorker();
  spentMaterial: SpentMaterial = new SpentMaterial();

  crop: Crop = new Crop();
  workOrder: WorkOrder = new WorkOrder();

  selectedWorkerForMachine;
  selectedTable;
  selectedYear = 2020;
  selectedCrop;
  selectedWorker;
  selectedOperation;
  selectedMachine;
  selectedCouplingMachine;
  selectedMaterial;
  quantityEntered;
  unit;

  clickAddMaterial = false;
  clickAddWorkOrder = false;
  clickAddWorkerMachine = false;
  clickAddMachine = false;
  addNewWow = false;
  addNewSpentMaterial = false;
  clickCloseWorkOrder = false;
  exists = false;

  validWow;
  validWom;
  validWoInfo;

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
      var today = new Date();
      this.workOrder.date = {
        year: today.getFullYear(),
        month: today.getMonth() + 1,
        day: today.getDate(),
      };
      this.userService.getAll().subscribe((data) => {
        this.allEmployees = data.content;
      });
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

        if (this.workOrder.treated == 0) {
          this.workOrder.treated = null;
        }
      });
      this.userService.getAll().subscribe((data) => {
        this.allEmployees = data.content;
        var comparableId = this.workOrder.responsibleId;
        var filterById = function (element) {
          return element.userId == comparableId;
        };

        this.nameFC.setValue(this.allEmployees.filter(filterById)[0]);
      });
    }

    this.operationService.getAll().subscribe((data) => {
      data = this.convertKeysToLowerCase(data);
      this.operations = data;
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

  //methods for on change listeners

  getArea() {
    this.cropService.getOne(this.workOrder.cropId).subscribe((data) => {
      this.crop = data;
      this.crop.area = data.Area;
      this.workOrder.area = data.Area;
    });
  }

  getOperation() {
    this.selectedOperation = this.workOrder.operationId;
  }

  getUnitOfMaterial(id) {
    this.unit = this.substances.find((x) => x.id == id).unit;
    this.woMaterials.forEach((material) => {
      if (material.material.id == id) {
        material.material.unit = this.unit;
      }
    });
  }

  getCulture() {
    this.cropService
      .getAllByFieldAndYear(this.workOrder.tableId, this.selectedYear)
      .subscribe((data) => {
        console.log(data);
        this.crops = data;
      });
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
    return emp && emp.name ? emp.name : "";
  }

  //methods for work order workers, operations and machines

  addWorkerAndMachine(valid) {
    this.clickAddWorkerMachine = true;
    this.wow.wowObjectId = Math.floor(Math.random() * 100 + 1);
    this.wow.user.id = this.selectedWorker;
    this.wow.operation.id = this.selectedOperation;
    this.wow.machine.id = this.selectedMachine;
    this.wow.connectingMachine.id = this.selectedCouplingMachine;
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
      this.selectedWorker = null;
      this.selectedOperation = this.workOrder.operationId;
      this.selectedMachine = null;
      this.selectedCouplingMachine = null;
    }
  }

  editWorkerAndMachine(wow) {
    this.wow.dayPeriod = wow.dayPeriod;
    this.wow.nightPeriod = wow.nightPeriod;
    this.wow.initialState = wow.initialState;
    this.wow.finalState = wow.finalState;
    this.wow.fuel = wow.fuel;
    this.wow.user.name = this.allEmployees.find(
      (x) => x.userId == wow.user.id
    ).name;
    this.wow.machine.name = this.devicesPropulsion.find(
      (x) => x.id == wow.machine.id
    ).name;
    this.idOfEditingWorkerMachine = this.wow.wowObjectId;
    if (!this.new) {
      this.wow.id = wow.id;
      this.wow = wow;
      this.wow.user.name = this.allEmployees.find(
        (x) => x.userId == this.wow.user.id
      ).name;
      this.wow.machine.name = this.devicesPropulsion.find(
        (x) => x.id == this.wow.machine.id
      ).name;
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
      if (this.wow.fuel == -1) {
        this.wow.fuel = null;
      }
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
    this.wow.user.id = this.selectedWorker;
    this.wow.operation.id = this.selectedOperation;
    this.wow.machine.id = this.selectedMachine;
    this.wow.connectingMachine.id = this.selectedCouplingMachine;
    this.wowService.addWorker(this.wow, this.workId).subscribe((res) => {
      console.log(res);
      this.wow = new WorkOrderWorker();
      this.selectedWorker = null;
      this.selectedOperation = null;
      this.selectedMachine = null;
      this.selectedCouplingMachine = null;
      this.workOrderService.getOne(this.workId).subscribe((data) => {
        this.workOrder = data;
      });
    });
  }

  updateWow(wow) {
    this.wowService.updateWorkOrderWorker(wow.id, wow).subscribe((res) => {
      console.log(res);
      this.toastr.success("Uspešno sačuvane promene.");

      this.workOrderService.getOne(this.workId).subscribe((data) => {
        this.workOrder = data;
      });
    });
  }

  //methods for work order materials

  addMaterial(valid) {
    this.clickAddMaterial = true;
    this.spentMaterial.smObjectId = Math.floor(Math.random() * 100 + 1);
    this.spentMaterial.material.id = this.selectedMaterial;
    this.spentMaterial.quantity = this.quantityEntered;
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
      this.selectedMaterial = null;
      this.quantityEntered = null;
      this.unit = null;
      this.clickAddMaterial = false;
    }
  }

  editMaterial(material) {
    if (this.new) {
      this.spentMaterial.spent = material.spent;
      this.spentMaterial.material.unit = material.material.unit;
      this.unit = this.spentMaterial.material.unit;
      this.spentMaterial.material.name = this.substances.find(
        (x) => x.id == material.material.id
      ).name;
      this.spentMaterial.quantity = material.quantity;
      this.idOfEditingMaterial = material.smObjectId;
      console.log(this.spentMaterial);
    } else {
      this.spentMaterial.id = material.id;
      this.spentMaterial = material;
      this.spentMaterial.material.unit = this.substances.find(
        (x) => x.id == this.spentMaterial.material.id
      ).unit;
      this.spentMaterial.material.name = this.substances.find(
        (x) => x.id == this.spentMaterial.material.id
      ).name;
      if (material.spent == -1) {
        this.spentMaterial.spent = null;
        this.spentMaterial.spentPerHectar = null;
      } else {
        this.spentMaterial.spent = material.spent;
        this.spentMaterial.spentPerHectar = material.spentPerHectar;
      }
    }
  }

  editExistingMaterial(existing) {
    this.idOfEditingMaterial = existing.smObjectId;
    this.woMaterials.forEach((material) => {
      if (material.smObjectId == this.idOfEditingMaterial) {
        material.material.id = this.substances.find(
          (x) => x.id == material.material.id
        ).id;
        material.quantity = existing.quantity;
      }
    });
    this.toastr.success("Uspešno izvršena promena.");
    console.log(this.woMaterials);
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
    this.spentMaterial.material.id = this.selectedMaterial;
    this.spentMaterial.quantity = this.quantityEntered;
    this.spentMaterialService
      .addSpentMaterial(this.workId, this.spentMaterial)
      .subscribe((res) => {
        console.log(res);
        this.spentMaterial = new SpentMaterial();
        this.selectedMaterial = null;
        this.quantityEntered = null;
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

        this.workOrderService.getOne(this.workId).subscribe((data) => {
          this.workOrder = data;
        });
      });
  }

  //methods for work order

  addWorkOrder(valid) {
    this.clickAddWorkOrder = true;
    console.log(valid.status);
    if (valid.status == "VALID" && this.nameFC.valid) {
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
  }

  closeWorkOrder() {
    this.clickCloseWorkOrder = true;
    if (this.workOrder.treated != null) {
      this.validWoInfo = true;
    } else {
      this.validWoInfo = false;
    }
    this.workOrder.workers.forEach((wow) => {
      if (
        wow.dayPeriod == -1 &&
        wow.nightPeriod == -1 &&
        wow.initialState == -1 &&
        wow.finalState == -1 &&
        wow.fuel == -1
      ) {
        this.validWow = false;
        const element: HTMLElement = document.getElementById(wow.id);
        this.renderer.setStyle(element, "background-color", "#BD362F");
      } else if (
        wow.dayPeriod == null &&
        wow.nightPeriod == null &&
        wow.initialState == null &&
        wow.finalState == null &&
        wow.fuel == null
      ) {
        this.validWow = false;
        const element: HTMLElement = document.getElementById(wow.id);
        this.renderer.setStyle(element, "background-color", "#BD362F");
      } else {
        this.validWow = true;
      }
    });
    if (this.workOrder.materials.length == 0) {
      this.validWom = true;
    } else {
      this.workOrder.materials.forEach((material) => {
        if (material.spent == -1) {
          this.validWom = false;
          const element: HTMLElement = document.getElementById(material.id);
          this.renderer.setStyle(element, "background-color", "#BD362F");
        } else {
          this.validWom = true;
        }
      });
    }
    if (this.validWow == false && this.validWom == false) {
      this.toastr.error("Unesite učinke.");
    } else if (this.validWom == false) {
      this.toastr.error("Unesite učinke materijala.");
    } else if (this.validWow == false) {
      this.toastr.error("Unesite učinke radnika i mašina.");
    } else if (
      this.validWom == true &&
      this.validWow == true &&
      this.validWoInfo == true
    ) {
      this.workOrderService.closeWorkOrder(this.workOrder).subscribe((res) => {
        console.log(res);
        this.toastr.success("Uspešno zatvoren radni nalog.");
        this.router.navigate(["/workOrder"]);
      });
    }
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

  //delete methods

  deleteExistingWorkOrderWorker(id) {
    this.wowService.deleteWorkOrderWorker(id).subscribe((res) => {
      console.log(res);
      this.workOrderService.getOne(this.workId).subscribe((data) => {
        this.workOrder = data;
      });
    });
  }

  deleteExistingSpentMaterial(id) {
    this.spentMaterialService.deleteSpentMaterial(id).subscribe((res) => {
      console.log(res);
      this.workOrderService.getOne(this.workId).subscribe((data) => {
        this.workOrder = data;
      });
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
}
