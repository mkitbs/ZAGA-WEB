import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Worker } from 'src/app/models/Worker';
import { Material } from 'src/app/models/Material';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { WorkOrderService } from 'src/app/service/work-order.service';
import { OperationService } from 'src/app/service/operation.service';
import { CropService } from 'src/app/service/crop.service';
import { Operation } from 'src/app/models/Operation';
import { Crop } from 'src/app/models/Crop';
import { WorkOrder } from 'src/app/models/WorkOrder';
import { CultureService } from 'src/app/service/culture.service';
import { Culture } from 'src/app/models/Culture';
import { Employee } from 'src/app/models/Employee';
import { EmployeeService } from 'src/app/service/employee.service';
import { Machine } from 'src/app/models/Machine';

@Component({
  selector: 'app-creatework-order',
  templateUrl: './creatework-order.component.html',
  styleUrls: ['./creatework-order.component.css']
})
export class CreateworkOrderComponent implements OnInit {
  
  constructor(private route: ActivatedRoute, private toastr: ToastrService, private router: Router, private workOrderService:WorkOrderService,
    private operationService:OperationService, private cropService:CropService, private cultureService:CultureService,
    private employeeService:EmployeeService) { 
    this.router.routeReuseStrategy.shouldReuseRoute = () => false;
  }

  operations = ["TEST","BALIRANJE BIG BALA", "BALIRANJE ROL-PRESOM", "DESIKACIJA", "DRLJANJE", "FINO SETVOSPREMANJE", "FOLIJARNA PRIHRANA",
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

  cultures = ["BORANIJA", "test", "GRAŠAK", "INDUSTRIJSKA PAPRIKA", "JARI OVAS", "KROMPIR", "KUKURUZ", "KUKURUZ KOKIČAR", "KUKURUZ ŠEĆERAC",
  "LUCERKA", "OZIMA PŠENICA", "OZIMA ULJANA REPICA", "POSTRNA BORANIJA", "POSTRNA SEMENSKA SOJA", "POSTRNI KUKURUZ", "POSTRNI KUKURUZ ŠEĆERAC",
  "RAŽ", "SEMENSKA PAPRIKA", "SEMENSKA PŠENICA", "SEMENSKA SOJA", "SEMENSKA ULJANA REPICA", "SEMENSKI JEČAM", "SEMENSKI KUKURUZ", "SOJA",
  "ŠEĆERNA REPA"];

  tables = ["T-27", "T-28", "T-29", "T-30", "T-31", "T-37", "T-39", "T-40", "T-42", "T-44", "T-46", "T-47", "T-48", "T-52", "T-53", "T-54", 
  "T-55", "T-56", "T-57", "T-58", "T-59", "T-60", "T-61", "T-62", "T-63", "T-64", "T-65", "T-66", "T-67", "T-68"];

  employeess = ["test", "ARSENOV NEMANJA", "BALAĆ MILOŠ", "BALANDŽIĆ MILIJANKA", "BELIĆ DRAGOSLAV", "ĆUK ĐURO", "ĆUK RANKO", "DAVIDOVIĆ PAJIĆ VERA", 
  "FRIC SEPIKA", "JANKOVIĆ SNEŽANA", "JUHAS ROBERT", "KNEŽEVIĆ GORAN", "KORUGA OLIVERA", "KOVAČ DRAGAN", "MARINKOV JOVAN", 
  "MILOVANČEV BRANISLAV", "OBRADOVIĆ SLAVKO", "PLAVŠIĆ BORKA", "RADOŠEVIĆ DJURO", "RAKIĆ MARINA", "RANC ROBERT", "VELJKOVIĆ BORKO", 
  "VUJIČIĆ GORAN", "ZLATKA VUJIČIN GLAVAŠKI"];

  devices = ["test", "IMT 533/539", "IMT 539", "JCB", "JD 6830", "JD 8320", "JD 8420", "JD6620-AGROPANONIJA KULA", "JOHN DEER-6930", "JOHN DEERE 4755", 
  "JOHN DEERE 5510 N", "JOHN DEERE 6620", "JOHN DEERE 6630", "JOHN DEERE 6810", "JOHN DEERE 6820", "JOHN DEERE 8320", "JOHN DEERE 8330", 
  "JOHN DEERE 9680WTS", "JOHN DEERE T660", "JOHN DEERE W650", "MTZ 82", "MTZ 820", "MTZ 892", "MTZ-AGROPANONIJA KULA", "NEW HOLAND CR7.90", 
  "NEW HOLAND T 6.050", "NEW HOLAND T 6.080", "NEW HOLAND T 6.175", "NEW HOLAND T 8.410", "NEW HOLAND T8.410", "NEW HOLAND(VRŠAC)", 
  "NEW HOLLAND T 6.175", "NEW HOLLAND T 7.210", "NEW HOLLAND T-6.050", "NEW HOLLAND T6.080", "NEW HOLLAND T6.175"];

  substances = ["test", "AGROPTIM ZENIT", "ALVERDE", "AMIKSOL", "AMISTER EXTRA", "AMPLIGO", "AN - 34,4 %N", "AZOKSISTAR", "BASAGRAN", "BELVEDERE", 
  "BETANAL AM 11 NEW", "BETANAL MAX PRO", "BLAGO 3", "BLAGO 4", "BLAUVIT", "BOSS 300 SL", "BRODISAN", "BUZZ ULTRA-DF", "CALLAM", "CAMBIO", 
  "CERIX", "CODAFOL 14:6:5", "CODAMIN", "DKC 5068", "DKC 5072", "DKC 5075", "DKC 5632", "DKC 5830", "DKC 5832", "DUOFERTIL NPK-46", "EDUARDA", 
  "ELDORADO", "ENTERPRISE", "ERWIX", "EUROFERTIL 67", "EUROFERTIL TOP 51", "FERTILEADER  AXIS", "FERTILEADER AXIS", "FERTILEADER VITAL", 
  "FITOFERT HUMI SUPER", "FITOFERT KOMBIVIT", "GENERATE", "GEO 2", "KCL", "MAP", "MELASA", "NITRON", "N-LOCK", "NOVALON 19:6:20", "NUTRI MAP BETA 10:40", 
  "NUTRIAKTIVE", "NUTRIVEG 10:10:20", "ROOTER", "SAN 33% N", "UREA - 46%N"];

  modalHeader = "Dodavanje radnika";
  workers = false;
  machines = false;
  materials = false;
  editing = false;
  editingMachine = false;
  editingMaterial = false;
  workId = this.route.snapshot.params.workId;
  
  material : Material = new Material();
  idOfEditingMaterial:any = 0;
  idOfEditingWorker:any = 0;
  idOfEditingMachine:any = 0;
  new = false;
  inputFill = true;
  workerMob:Worker = new Worker();

  operation: Operation = new Operation();
  crop: Crop = new Crop();
  culture: Culture = new Culture();
  employee: Employee = new Employee();
  wo : WorkOrder = new WorkOrder();
  worker : Worker = new Worker();
  employees: Employee[] = [];
  machine: Machine = new Machine();
  woMachines: Machine[] = [];
  woMaterials: Material[] = [];
  cultureId;
  startDate:{}
  endDate:{}
  selectedWorker;
  selectedMachine;
  selectedMaterial;

  ngOnInit() {
    if(this.workId == "new") { //new
      this.new = true;
      this.wo = new WorkOrder();
      this.wo.machines = [];
      this.wo.workers = [];
      this.wo.materials = [];
      this.wo.status = "Novi";
    } else {
      this.new = false;

      this.workOrderService.getOne(this.workId).subscribe(data => {
        this.wo = data;
        
        if(this.wo.status == "NEW"){
          this.wo.status = "Novi";
        } else if(this.wo.status == "IN_PROGRESS"){
          this.wo.status = "U radu";
        } else if(this.wo.status == "CLOSED"){
          this.wo.status = "Zatvoren";
        }

        this.wo.start = { day: +this.wo.start.substring(8,10),
          month: +this.wo.start.substring(5,7), 
          year: +this.wo.start.substring(0,4)
        };
        this.wo.end = { day: +this.wo.end.substring(8,10), 
          month: +this.wo.end.substring(5,7), 
          year: +this.wo.end.substring(0,4)
       };


       this.getOperation(this.wo.operationId);
       this.getCrop(this.wo.cropId);
       this.getResponsibleEmployee(this.wo.responsibleId);

       this.wo.workers.forEach(data => {
        this.employee = data;
        this.getWorker(this.employee.userId);
       })
       console.log(this.wo)
      })
    }
  }

  getOperation(id){
    this.operationService.getOne(id).subscribe(data => {
      this.operation = data;
    })
  }

  getCrop(id){
    this.cropService.getOne(id).subscribe(data => {
      this.crop = data;
      this.cultureId = this.crop.cultureId;
      this.getCulture(this.cultureId);
    })
  }

  getCulture(id){
    this.cultureService.getOne(id).subscribe(data => {
      this.culture = data;
    })
  }

  getResponsibleEmployee(id){
    this.employeeService.getOne(id).subscribe(data => {
      this.employee = data;
    })
  }

  getWorker(id){
    this.employeeService.getOne(id).subscribe(data => {
      this.employee = data;
      this.employees.push(this.employee);
    })
  }

  expandWorkers() {
    this.workers = !this.workers;
    let el = document.getElementById("rad");
    setTimeout(()=>{el.scrollIntoView({behavior:"smooth"})}, 500);
    
  }
  expandMachines() {
    this.machines = !this.machines;
    let el = document.getElementById("mas");
    setTimeout(()=>{el.scrollIntoView({behavior:"smooth"})}, 500);
    
  }
  expandMaterials() {
    this.materials = !this.materials;
    let el = document.getElementById("mat");
    setTimeout(()=>{el.scrollIntoView({behavior:"smooth"})}, 500);
    
  }

  addWorker(){
    this.employee.name = this.selectedWorker;
    this.employees.push(this.employee);
    this.employee = new Employee();
  }

  editWorker(worker){
    this.employee.name = worker.name;
    this.editing = true;
    this.idOfEditingWorker = worker.name;
  }

  editExistingWorker(){
    this.employees.forEach(employee => {
      if(employee.name == this.idOfEditingWorker){
        employee.name = this.selectedWorker;
      }
    })
    this.employee = new Employee();
    this.editing = false;
  }

  addMachine(){
    this.machine.name = this.selectedMachine;
    this.woMachines.push(this.machine);
    this.machine = new Machine();
  }

  editMachine(machine){
    this.machine.name = machine.name;
    this.editingMachine = true;
    this.idOfEditingMachine = machine.name;
  }

  editExistingMachine(){
    this.woMachines.forEach(machine => {
      if(machine.name == this.idOfEditingMachine){
        machine.name = this.selectedMachine;
      }
    })
    this.machine = new Machine();
    this.editingMachine = false;
  }

  addMaterial(){
    this.material.name = this.selectedMaterial;
    this.woMaterials.push(this.material);
    this.material = new Material();
  }

  editMaterial(material){
    this.material.name = material.name;
    this.editingMaterial = true;
    this.idOfEditingMaterial = material.name;
  }

  editExistingMaterial(){
    this.woMaterials.forEach(material => {
      if(material.name == this.idOfEditingMaterial){
        material.name = this.selectedMaterial;
      }
    })
    this.material = new Material();
    this.editingMaterial = false;
  }

  addWorkOrder(){
    
   
    this.wo.machines = this.woMachines;
    this.wo.workers = this.employees;
    this.wo.materials = this.woMaterials;

    /*
    this.workOrderService.addWorkOrder(this.wo).subscribe(data => {
      this.toastr.success("Uspešno kreiran radni nalog.");
    }, error => {
      this.toastr.error("Radni nalog nije kreiran.");
    })
    */
    console.log(this.wo)
  }

  updateWorkOrder(){
    var dateStartToAdd = "";
    var dateEndToAdd = "";
    if(this.wo.start != undefined) {
        if(this.wo.start.month < 10) {
          this.wo.start.month = '0' + this.wo.start.month;
        }
        if(this.wo.start.day < 10){
          this.wo.start.day = '0' + this.wo.start.day;
        }
        dateStartToAdd= this.wo.start.year + '-' 
          + this.wo.start.month + '-' 
          + this.wo.start.day;
    }
    if(this.wo.end != undefined) {
      if(this.wo.end.month < 10) {
        this.wo.end.month = '0' + this.wo.end.month;
      }
      if(this.wo.end.day < 10){
        this.wo.end.day = '0' + this.wo.end.day;
      }
      dateEndToAdd= this.wo.end.year + '-' 
        + this.wo.end.month + '-' 
        + this.wo.end.day;
    }
   
    this.wo.machines = this.woMachines;
    this.wo.workers = this.employees;
    this.wo.materials = this.woMaterials;

    /*
    this.workOrderService.updateWorkOrder(this.wo).subscribe(data => {
      this.toastr.success("Uspešno sačuvan radni nalog.");
    }, error => {
      this.toastr.error("Radni nalog nije sačuvan.");
    })
    */
    console.log(this.wo)
  }

  setForEdit(wor){
    this.workerMob = wor;
    console.log(wor)
  }

}
