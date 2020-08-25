import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { WorkOrdeMachine } from 'src/app/models/WorkOrderMachine';
import { Worker } from 'src/app/models/Worker';
import { Material } from 'src/app/models/Material';
import { WorkOrderInfo } from 'src/app/models/WorkOrderInfo';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-creatework-order',
  templateUrl: './creatework-order.component.html',
  styleUrls: ['./creatework-order.component.css']
})
export class CreateworkOrderComponent implements OnInit {
  
  constructor(private route: ActivatedRoute, private toastr: ToastrService) { }

  operations = ["BALIRANJE BIG BALA", "BALIRANJE ROL-PRESOM", "DESIKACIJA", "DRLJANJE", "FINO SETVOSPREMANJE", "FOLIJARNA PRIHRANA",
  "GRABLJ. LUCERKE, DETEL I TRAVA", "GRUBERISANJE DO 15 CM", "GRUBERISANJE NA 15-20 CM", "GRUBERISANJE-LJUŠTENJE DO 15 CM", "KOPANJE KANALA ZA REPINU PIPU",
  "KOŠENJE LUCER, DETELIN I TRAVA", "KOŠENJE ZELENE MASE", "MEĐUREDNA KULTIVACIJA", "MEĐUREDNA KULTIVACIJA II", "MULČIRANJE",
  "NAVODNJAVANJE", "OPŠTI POSLOVI", "ORANJE DO 30 CM", "ORANJE DO 30 CM SA PAKER VALJK", "ORANJE DO 35 CM", "ORANJE DO 35 CM SA PAKER VALJK",
  "ORANJE PREKO 35 CM", "ORANJE PREKO 35 CM SA PAKER VA", "PODRIVANJE DO 25CM", "PODRIVANJE DO 35 CM", "PODRIVANJE DO 35CM II",
  "PODRIVANJE DO 50 CM", "PODRIVANJE DO 50 CM SA ZATVARA", "PRAVLJENJE BANKOVA ZA KROMPIR", "PREDSETVENO ĐUBRENJE", "PRESEJAVANJE 1",
  "PRETSETVENA PRIPREMA I PUT", "PREVOZ GORIVA I SIPANJE GORIVA", "PREVOZ MINERALNOG ĐUBRIVA", "PREVOZ RADNIKA", "PREVOZ SEMENA", "PREVOZ SLAME I LUCERKE",
  "PREVOZ STAJNJAKA", "PREVOZ VODE ZA PICE", "PREVOZ VODE ZA PRSKANJE", "PREVOZ VODE ZA PRSKANJE I Z.S.", "PREVOZ ZAŠTITNIH SREDSTAVA",
  "PREVOZ ZELENE MASE", "PRIHRANA MIN.ĐUBRIVOM I", "PRIHRANA MIN.ĐUBRIVOM II", "PRSKANJE FUNG. I + PRIHRANA", "PRSKANJE FUNG. III + PRIHRANA",
  "PRSKANJE FUNGIC+INSEKT+PRIHR.", "PRSKANJE FUNGICIDOM", "PRSKANJE FUNGICIDOM II", "PRSKANJE FUNGICIDOM III", "PRSKANJE GRAMINICIDIMA",
  "PRSKANJE HERBICID+PRIHRANA", "PRSKANJE INSEK+FUNGICID", "PRSKANJE INSEKTICIDIMA", "PRSKANJE INSEKTICIDIMA II", "PRSKANJE KONT. HERBIC+FUNGIC.",
  "PRSKANJE KONT. HERBICIDOM I", "PRSKANJE KONT. HERBICIDOM II", "PRSKANJE KONT. HERBICIDOM III", "PRSKANJE KONT. HERBICIDOM IV",
  "PRSKANJE KONT.HERBICIDOM SIRAK", "PRSKANJE TOTAL. HERBICIDOM I", "PRSKANJE TOTAL. HERBICIDOM III", "PRSKANJE ZEMLJ.I TOTAL.HERBIC.",
  "PRSKANJE ZEMLJIŠNIM HERBICIDOM", "RAD TRIMEROM", "RASTURANJE MELASE", "RASTURANJE MINERALNOG ĐUBRIVA", "RASTURANJE STAJNJAKA", "RAVNANJE PUTEVA",
  "SADNJA KROMPIRA", "SEČENJE METLICA I", "SEČENJE METLICA II", "SEČENJE METLICA III", "SETVA", "SETVOSPREMANJE NOŠ.SPREMAČ II",
  "SETVOSPREMANJE NOŠENIM SPREMAČ", "SETVOSPREMANJE VUČ.SPREMAČ II", "SETVOSPREMANJE VUČENIM SPREMAČ", "ŠLEPANJE", "TANJIRANJE LAKOM TANJIRAČOM",
  "TANJIRANJE PUTEVA", "TANJIRANJE STRNIŠTA", "TANJIRANJE TEŠKOM TANJIRAČ. II", "TANJIRANJE TEŠKOM TANJIRAČOM", "TARUPIRANJE", "TRANSPORT ROBE",
  "TROVANJE GLODARA MAMCIMA", "USEJAVANJE 1", "USLUGE", "UTOVAR ROBE", "UTOVAR STAJNJAKA", "UTOVAR TELEHENDEROM", "UTOVAR-ISTOVAR MIN. ĐUBRIVA",
  "UZORKOVANJE ZEMLJIŠTA", "VALJANJE GLATKIM VALJKOM", "VALJANJE KEMBRIDZ VALJKOM", "VREME U PUTU", "ZAGRTANJE KROMPIRA", "ZAPRASIVANJE LOVNIH KANALA",
  "ZATVARANJE RAZORA", "ZATVARANJE VLAGE", "ŽETVA", "ŽETVA SA SECK.ŽET.OSTATA", "ŽETVA-TRANSPORT ROBE"];

  cultures = ["BORANIJA", "GRAŠAK", "INDUSTRIJSKA PAPRIKA", "JARI OVAS", "KROMPIR", "KUKURUZ", "KUKURUZ KOKIČAR", "KUKURUZ ŠEĆERAC",
  "LUCERKA", "OZIMA PŠENICA", "OZIMA ULJANA REPICA", "POSTRNA BORANIJA", "POSTRNA SEMENSKA SOJA", "POSTRNI KUKURUZ", "POSTRNI KUKURUZ ŠEĆERAC",
  "RAŽ", "SEMENSKA PAPRIKA", "SEMENSKA PŠENICA", "SEMENSKA SOJA", "SEMENSKA ULJANA REPICA", "SEMENSKI JEČAM", "SEMENSKI KUKURUZ", "SOJA",
  "ŠEĆERNA REPA"];

  tables = ["T-27", "T-28", "T-29", "T-30", "T-31", "T-37", "T-39", "T-40", "T-42", "T-44", "T-46", "T-47", "T-48", "T-52", "T-53", "T-54", 
  "T-55", "T-56", "T-57", "T-58", "T-59", "T-60", "T-61", "T-62", "T-63", "T-64", "T-65", "T-66", "T-67", "T-68"];

  employees = ["ARSENOV NEMANJA", "BALAĆ MILOŠ", "BALANDŽIĆ MILIJANKA", "BELIĆ DRAGOSLAV", "ĆUK ĐURO", "ĆUK RANKO", "DAVIDOVIĆ PAJIĆ VERA", 
  "FRIC SEPIKA", "JANKOVIĆ SNEŽANA", "JUHAS ROBERT", "KNEŽEVIĆ GORAN", "KORUGA OLIVERA", "KOVAČ DRAGAN", "MARINKOV JOVAN", "MEJIĆ  PETRA", 
  "MILOVANČEV BRANISLAV", "OBRADOVIĆ  SLAVKO", "PLAVŠIĆ BORKA", "RADOŠEVIĆ DJURO", "RAKIĆ MARINA", "RANC ROBERT", "VELJKOVIĆ BORKO", 
  "VUJIČIĆ GORAN", "ZLATKA VUJIČIN GLAVAŠKI"];

  devices = ["IMT 533/539", "IMT 539", "JCB", "JD 6830", "JD 8320", "JD 8420", "JD6620-AGROPANONIJA KULA", "JOHN DEER-6930", "JOHN DEERE 4755", 
  "JOHN DEERE 5510 N", "JOHN DEERE 6620", "JOHN DEERE 6630", "JOHN DEERE 6810", "JOHN DEERE 6820", "JOHN DEERE 8320", "JOHN DEERE 8330", 
  "JOHN DEERE 9680WTS", "JOHN DEERE T660", "JOHN DEERE W650", "MTZ 82", "MTZ 820", "MTZ 892", "MTZ-AGROPANONIJA KULA", "NEW HOLAND CR7.90", 
  "NEW HOLAND T 6.050", "NEW HOLAND T 6.080", "NEW HOLAND T 6.175", "NEW HOLAND T 8.410", "NEW HOLAND T8.410", "NEW HOLAND(VRŠAC)", 
  "NEW HOLLAND T 6.175", "NEW HOLLAND T 7.210", "NEW HOLLAND T-6.050", "NEW HOLLAND T6.080", "NEW HOLLAND T6.175"];

  substances = ["AGROPTIM ZENIT", "ALVERDE", "AMIKSOL", "AMISTER EXTRA", "AMPLIGO", "AN - 34,4 %N", "AZOKSISTAR", "BASAGRAN", "BELVEDERE", 
  "BETANAL AM 11 NEW", "BETANAL MAX PRO", "BLAGO 3", "BLAGO 4", "BLAUVIT", "BOSS 300 SL", "BRODISAN", "BUZZ ULTRA-DF", "CALLAM", "CAMBIO", 
  "CERIX", "CODAFOL 14:6:5", "CODAMIN", "DKC 5068", "DKC 5072", "DKC 5075", "DKC 5632", "DKC 5830", "DKC 5832", "DUOFERTIL NPK-46", "EDUARDA", 
  "ELDORADO", "ENTERPRISE", "ERWIX", "EUROFERTIL 67", "EUROFERTIL TOP 51", "FERTILEADER  AXIS", "FERTILEADER AXIS", "FERTILEADER VITAL", 
  "FITOFERT HUMI SUPER", "FITOFERT KOMBIVIT", "GENERATE", "GEO 2", "KCL", "MAP", "MELASA", "NITRON", "N-LOCK", "NOVALON 19:6:20", "NUTRI MAP BETA 10:40", 
  "NUTRIAKTIVE", "NUTRIVEG 10:10:20", "ROOTER", "SAN 33% N", "UREA - 46%N"];

  workers = false;
  machines = false;
  materials = false;
  editing = false;
  editingMachine = false;
  editingMaterial = false;
  workId = this.route.snapshot.params.workId;
  workOrder;
  machine : WorkOrdeMachine = new WorkOrdeMachine();
  workOrderMachine : WorkOrdeMachine = new WorkOrdeMachine();
  worker : Worker = new Worker();
  material : Material = new Material();
  idOfEditingMaterial:any = 0;
  idOfEditingWorker:any = 0;
  idOfEditingMachine:any = 0;
  new = false;
  workOrders: any[];
  workOrderInfo : WorkOrderInfo = new WorkOrderInfo();
  inputFill = true;

  ngOnInit() {
    if(this.workId == "new") { //new
      this.new = true;
      this.workOrder = new WorkOrderInfo();
      this.workOrder.machines = [];
      this.workOrder.workers = [];
      this.workOrder.materials = [];
      this.workOrders = JSON.parse(localStorage["workOrders"]);
      this.workOrder.status = "Novi";
    } else {
      this.new = false;
      this.workOrders = JSON.parse(localStorage["workOrders"]);
      this.workOrder = this.workOrders[this.workId-1];
      this.workOrder.start = { day: +this.workOrder.start.substring(0,2), 
                               month: +this.workOrder.start.substring(3,5), 
                               year: +this.workOrder.start.substring(6,10)
                             };
      this.workOrder.end = { day: +this.workOrder.end.substring(0,2), 
                             month: +this.workOrder.end.substring(3,5), 
                             year: +this.workOrder.end.substring(6,10)
                            };
    }
  }

  expandWorkers() {
    this.workers = !this.workers;
  }
  expandMachines() {
    this.machines = !this.machines;
  }
  expandMaterials() {
    this.materials = !this.materials;
  }

  addWorker() {
      if(this.worker.date != undefined) {
        if(this.worker.date.month < 10) {
          this.worker.date.month = '0' + this.worker.date.month;
        }
          this.worker.date = this.worker.date.day + '.' 
          + this.worker.date.month + '.' 
          + this.worker.date.year + '.';
      }  
      this.worker.id = this.workOrder.workers.length + 1;
      this.worker.workPeriod = this.worker.dayWorkPeriod + this.worker.nightWorkPeriod;
      this.workOrder.workers.push(this.worker);
      this.worker = new Worker();
      this.editing = false;
  } 
  
  editExistingWorker() {
    var dateToAdd = "";
    if(this.worker.date != undefined) {
        if(this.worker.date.month < 10) {
          this.worker.date.month = '0' + this.worker.date.month;
        }
        dateToAdd= this.worker.date.day + '.' 
          + this.worker.date.month + '.' 
          + this.worker.date.year + '.';
    }
    this.workOrder.workers.forEach(element => {
      if(element.id == this.idOfEditingWorker) {
        element.worker = this.worker.worker;
        element.dayWorkPeriod = this.worker.dayWorkPeriod;
        element.nightWorkPeriod = this.worker.nightWorkPeriod;
        element.date = dateToAdd;
        element.operation = this.worker.operation;

      }
    });
    this.worker = new Worker();
    this.editing = false;
  }

  editWorker(worker) {
    this.worker.worker = worker.worker;
    this.worker.workPeriod = worker.workPeriod;
    this.worker.operation = worker.operation;
    this.worker.nightWorkPeriod = worker.nightWorkPeriod;
    this.worker.dayWorkPeriod = worker.dayWorkPeriod;
    this.worker.workPeriod = worker.nightWorkPeriod + worker.dayWorkPeriod;
    if(worker.date != undefined) {
        this.worker.date = {day: +worker.date.substring(0,2), 
                            month: +worker.date.substring(3,5), 
                            year: +worker.date.substring(6,10)
                          };
    }
    this.editing = true;
    this.idOfEditingWorker = worker.id;
  }

  addMachine() {
    
    this.machine.id = this.workOrder.machines.length + 1;
    this.machine.sumState = this.machine.finalState - this.machine.initialState;
    this.workOrder.machines.push(this.machine);
    this.machine = new WorkOrdeMachine();
    this.editingMachine = false;
  }

  editMachine(machine) {
    this.machine.worker = machine.worker;
    this.machine.machine = machine.machine;
    this.machine.initialState = machine.initialState;
    this.machine.finalState = machine.finalState;
    this.machine.sumState = machine.finalState - machine.initialState;
    this.machine.fuel = machine.fuel;
    this.machine.workPeriod = machine.workPeriod;
    this.editingMachine = true;
    this.idOfEditingMachine = machine.id;
  }

  updateExistingMachine() {
 
    this.workOrder.machines.forEach(element => {
      if(element.id == this.idOfEditingMachine) {
        element.worker = this.machine.worker;
        element.workPeriod = this.machine.workPeriod;
        element.machine = this.machine.machine;
        element.fuel = this.machine.fuel;
        element.finalState = this.machine.finalState;
        element.initialState = this.machine.initialState;
        element.sumState = this.machine.sumState;
      }
    });
    this.machine = new WorkOrdeMachine();
    this.editingMachine= false;
  }

  addMaterial() {
    this.material.id = this.workOrder.materials.length + 1;
    this.workOrder.materials.push(this.material);
    this.editingMaterial = false;
    this.material = new Material();
  }

  editMaterial(material) {
    this.material.name = material.name;
    this.material.quantity = material.quantity;
    this.material.unit = material.unit;
    this.material.spent = material.spent;
    this.material.spentPerHectar = material.spentPerHectar;
    this.material.quantityPerHectar = material.quantityPerHectar;
    this.editingMaterial = true;
    this.idOfEditingMaterial = material.id;
  }

  editExistingMaterial() {

    this.workOrder.materials.forEach(element => {
      if(element.id == this.idOfEditingMaterial) {
        element.name = this.material.name;
        element.unit = this.material.unit;
        element.quantity = this.material.quantity;
        element.quantityPerHectar = this.material.quantityPerHectar;
        element.spent = this.material.spent;
        element.spentPerHectar = this.material.spentPerHectar;
      }
    });
    this.material = new Material();
    this.editingMaterial = false;
  }

  saveData() {
    localStorage.clear();
    var dateStartToAdd = "";
    var dateEndToAdd = "";
    if(this.workOrder.start != undefined) {
        if(this.workOrder.start.month < 10) {
          this.workOrder.start.month = '0' + this.workOrder.start.month;
        }
        dateStartToAdd= this.workOrder.start.day + '.' 
          + this.workOrder.start.month + '.' 
          + this.workOrder.start.year + '.';
    }
    if(this.workOrder.end != undefined) {
      if(this.workOrder.end.month < 10) {
        this.workOrder.end.month = '0' + this.workOrder.end.month;
      }
      dateEndToAdd= this.workOrder.end.day + '.' 
        + this.workOrder.end.month + '.' 
        + this.workOrder.end.year + '.';
    }
    this.workOrder.start = dateStartToAdd;
    this.workOrder.end = dateEndToAdd;
    localStorage["workOrders"] = JSON.stringify(this.workOrders);
    this.toastr.success("Uspešno sačuvan radni nalog.")
  }

  closeWorkOrder() {
    
    //logic for validation here
    if(this.workOrder.treated != ""){
      this.workOrder.status = "Zatvoren";
      localStorage.clear();
      localStorage["workOrders"] = JSON.stringify(this.workOrders);
      this.toastr.success("Uspešno zatvoren radni nalog.");
      this.inputFill = true;
    } else{
      this.toastr.error("Neuspešno zatvoren radni nalog. Popunite sva polja.")
      this.inputFill = false;
      
    }
  }

  calculationQuantityPerHectar(){
    this.material.quantityPerHectar = this.material.quantity / this.workOrder.area;
  }

  addWorkOrder(){
    var dateStartToAdd = "";
    var dateEndToAdd = "";
    if(this.workOrder.start != undefined) {
        if(this.workOrder.start.month < 10) {
          this.workOrder.start.month = '0' + this.workOrder.start.month;
        }
        dateStartToAdd= this.workOrder.start.day + '.' 
          + this.workOrder.start.month + '.' 
          + this.workOrder.start.year + '.';
    }
    if(this.workOrder.end != undefined) {
      if(this.workOrder.end.month < 10) {
        this.workOrder.end.month = '0' + this.workOrder.end.month;
      }
      dateEndToAdd= this.workOrder.end.day + '.' 
        + this.workOrder.end.month + '.' 
        + this.workOrder.end.year + '.';
    }
    this.workOrder.start = dateStartToAdd;
    this.workOrder.end = dateEndToAdd;
    this.workOrder.id = this.workOrders.length + 1;
    this.workOrder.status = "Novi";
    this.workOrder.treated = "";
    this.workOrders.push(this.workOrder);
    localStorage["workOrders"] = JSON.stringify(this.workOrders);
    this.toastr.success("Uspešno dodat nalog.");
  }



}
