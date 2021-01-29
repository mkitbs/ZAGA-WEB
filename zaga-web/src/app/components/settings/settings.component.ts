import { Component, OnInit } from '@angular/core';
import { IDropdownSettings } from 'ng-multiselect-dropdown';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Roles } from 'src/app/models/Roles';
import { Setting } from 'src/app/models/Setting';
import { SignupRequest } from 'src/app/models/SignupRequest';
import { User } from 'src/app/models/User';
import { AuthService } from 'src/app/service/auth/auth.service';
import { CropService } from 'src/app/service/crop.service';
import { CultureGroupService } from 'src/app/service/culture-group.service';
import { CultureService } from 'src/app/service/culture.service';
import { FieldGroupService } from 'src/app/service/field-group.service';
import { FieldService } from 'src/app/service/field.service';
import { MachineGroupService } from 'src/app/service/machine-group.service';
import { MachineService } from 'src/app/service/machine.service';
import { OperationGroupService } from 'src/app/service/operation-group.service';
import { OperationService } from 'src/app/service/operation.service';
import { UserService } from 'src/app/service/user.service';
import { VarietyService } from 'src/app/service/variety.service';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {

  constructor(
    private userService: UserService,
    private toastr: ToastrService,
    private spinner: NgxSpinnerService,
    private authService: AuthService,
    private cropService: CropService,
    private machineService: MachineService,
    private fieldService: FieldService,
    private fieldGroupService: FieldGroupService,
    private machineGroupService: MachineGroupService,
    private operationGroupService: OperationGroupService,
    private operationService: OperationService,
    private cultureGroupService: CultureGroupService,
    private culutreService: CultureService,
    private varietyService: VarietyService
  ) { 
    this.dropdownSettings = {
      singleSelection: false,
      idField: 'id',
      textField: 'name',
      selectAllText: 'Označi sve',
      unSelectAllText: 'Poništi sve',
      searchPlaceholderText: 'Pretraži',
      noDataAvailablePlaceholderText: 'Nema dodatih rola',
      allowSearchFilter: true,
    };
  }

  sapSettingsBool = false;
  accountSettingsBool = false;
  loading = false;
  isUpdate = false;

  users: User[] = [];
  newUser: SignupRequest = new SignupRequest();
  roles: Roles[] = [];
  user: SignupRequest = new SignupRequest();
  setting: Setting = new Setting();

  selectedRolesNewUser = [];
  selectedRolesUser = [];
  clickAddNewUser;
  selectedTypeOfMainData;
  useSap;
  newPassword;
  confirmNewPassword;

  dropdownSettings: IDropdownSettings;

  selectedIndex = 0;

  clickedCrop = false;
  clickedMachine = false;
  clickedEmployee = false;
  clickedField = false;
  clickedFieldGroup = false;
  clickedMachineGroup = false;
  clickedOperationGroup = false;
  clickedOperation = false;
  clickedCulture = false;
  clickedCultureGroup = false;
  clickedVariety = false;

  loadingCrop = true;
  loadingMachine = true;
  loadingEmployee = true;
  loadingField = true;
  loadingFieldGroup = true;
  loadingMachineGroup = true;
  loadingOperationGroup = true;
  loadingOperation = true;
  loadingCulture = true;
  loadingCultureGroup = true;
  loadingVariety = true;

  syncedCrop;
  syncedMachine;
  syncedEmployee;
  syncedField;
  syncedFieldGroup;
  syncedMachineGroup;
  syncedOperationGroup;
  syncedOperation;
  syncedCulture;
  syncedCultureGroup;
  syncedVariety;

  repeat = false;

  ngOnInit() {
    this.getAllUsers();

    this.userService.getAllRoles().subscribe(data => {
      this.roles = data;
    })

    this.getUserSettings();
  }

  getAllUsers(){
    this.userService.getAllUsers().subscribe(data => {
      this.users = data;
    })
  }

  sapSettings(){
    this.sapSettingsBool = !this.sapSettingsBool;
    this.accountSettingsBool = false;
  }

  accountSettings(){
    this.accountSettingsBool = !this.accountSettingsBool;
    this.sapSettingsBool = false;
  }

  addNewUser(valid){
    this.clickAddNewUser = true;
    this.loading = true;
    console.log(this.newUser.dateOfBirth)
    if(this.newUser.password != this.newUser.confirmPassword){
      this.toastr.error("Lozinke nisu iste.");
      this.loading=false
    } else if(valid){
      this.spinner.show();
      this.newUser.roles = this.selectedRolesNewUser;
      /*
      var dateString;
      var day;
      var month;
      if(this.newUser.dateOfBirth.day >= 1 && this.newUser.dateOfBirth.day <= 9){
        day = "0" + this.newUser.dateOfBirth.day;
      } else {
        day = this.newUser.dateOfBirth.day;
      }
      if(this.newUser.dateOfBirth.month >= 1 && this.newUser.dateOfBirth.month <= 9){
        month = "0" + this.newUser.dateOfBirth.month;
      } else {
        month = this.newUser.dateOfBirth.month;
      }
      var year = this.newUser.dateOfBirth.year;
      dateString = year + "-" + month + "-" + day;
      this.newUser.dateOfBirth = dateString;
      */
      console.log(this.newUser)
      this.clickAddNewUser = false;
      
      this.userService.signup(this.newUser).subscribe(res => {
        this.spinner.hide();
        this.toastr.success("Korisnik je kreiran");
        this.getAllUsers();
        this.selectedIndex = this.selectedIndex - 1;
        this.newUser = new SignupRequest();
        this.loading = false;
        this.selectedRolesNewUser = [];
      },error=>{
        this.loading = false;
        this.spinner.hide();
        if(error.status === 409){
          this.toastr.error("Uneti email se već koristi.");
        } else if(error.status === 400){
          this.toastr.error("Datum rođenja mora biti u prošlosti.")
        } else if(error.status === 406){
          this.toastr.error("SAP ID se već koristi.")
        } else {
          this.toastr.error("Trenutno nije moguće kreirati korisnika.")
        }
      })
    } else if(!valid){
      this.loading = false;
    }
  }

  saveChangeSAP(){
    this.setting.useSap = this.useSap;
    this.setting.masterDataFormat = this.selectedTypeOfMainData;
    console.log(this.setting)
    this.authService.updateUserSettings(this.setting.tenantId, this.setting).subscribe(res => {
      this.toastr.success("Promene su sačuvane.")
    }, error => {
      this.toastr.error("Trenutno nije moguće izvršiti promene.")
    })
   
  }

  nextStep(user) {
    console.log(user)
    this.isUpdate = true;
    if(this.selectedIndex == -1){
      this.selectedIndex = this.selectedIndex + 3;
    } else {
      this.selectedIndex = this.selectedIndex + 2;
    }
    this.user = user;
    this.user.password = null;
    /*
    var date = user.dateOfBirth.split("T")[0];
    var day = date.split("-")[2];
    var month = date.split("-")[1];
    var year = date.split("-")[0]
    year = parseInt(year);
    if(day.startsWith("0")){
      day = day.substring(1)
      day = parseInt(day);
    } else {
      day = parseInt(day);
    }
    if(month.startsWith("0")){
      month = month.substring(1)
      month = parseInt(month);
    } else {
      month = parseInt(month);
    }
    this.user.dateOfBirth = {
      day: day,
      month: month,
      year: year
    }
    */
    this.selectedRolesUser = user.roles;
    console.log(this.user)
  }

  updateUser(valid){
    this.isUpdate = false;
    if(valid){
      if(this.newPassword != null && this.confirmNewPassword != null){
        this.user.password = this.newPassword;
        this.user.confirmPassword = this.confirmNewPassword;
      } else {
        this.user.password = null;
        this.user.confirmPassword = null;
      }
      /*
      var dateString;
      var day;
      var month;
      if(this.user.dateOfBirth.day >= 1 && this.user.dateOfBirth.day <= 9){
        day = "0" + this.user.dateOfBirth.day;
      } else {
        day = this.user.dateOfBirth.day;
      }
      if(this.user.dateOfBirth.month >= 1 && this.user.dateOfBirth.month <= 9){
        month = "0" + this.user.dateOfBirth.month;
      } else {
        month = this.user.dateOfBirth.month;
      }
      var year = this.user.dateOfBirth.year;
      dateString = year + "-" + month + "-" + day;
      this.user.dateOfBirth = dateString;
      */
      this.user.roles = [];
      this.user.roles = this.selectedRolesUser;
      console.log(this.user)
      this.spinner.show();
      this.userService.updateUser(this.user).subscribe(res => {
        this.spinner.hide();
        this.selectedIndex = 0;
        this.user = new SignupRequest();
        this.toastr.success("Korisnik je uspešno sačuvan.")
      }, error => {
        this.spinner.hide();
        this.toastr.error("Došlo je do greške. Trenutno nije moguće sačuvati promene.")
      })
    }
    
  }

  deleteUser(id){
    this.userService.deleteUser(id).subscribe(res => {
      this.getAllUsers();
      this.toastr.success("Korisnik je obrisan.")
    }, error => {
      this.toastr.error("Trenutno nije moguće obrisati korisnika.")
    })
  }

  activateUser(id){
    this.userService.activateUser(id).subscribe(res => {
      this.toastr.success("Korisnik je aktiviran.")
      this.getAllUsers();
    }, error => {
      this.toastr.error("Korisnik nije aktiviran.")
    })
  }

  deactivateUser(id){
    this.userService.deactivateUser(id).subscribe(res => {
      this.toastr.success("Korisnik je deaktiviran.")
      this.getAllUsers();
    }, error => {
      this.toastr.error("Korisnik nije deaktiviran.")
    })
  }

  languageSettings(){
    this.toastr.info("Nije implementirano.")
  }

  getUserSettings(){
    this.authService.getUserSettings().subscribe(data => {
      this.setting = data;
      this.selectedTypeOfMainData = this.setting.masterDataFormat;
      this.useSap = this.setting.useSap;
    })
  }

  clickCrop(){
    this.clickedCrop = !this.clickedCrop;
  }

  clickMachine(){
    this.clickedMachine = !this.clickedMachine;
  }

  clickEmployee(){
    this.clickedEmployee = !this.clickedEmployee;
  }

  clickFieldGroup(){
    this.clickedFieldGroup = !this.clickedFieldGroup;
  }

  clickField(){
    this.clickedField = !this.clickedField;
  }

  clickMachineGroup(){
    this.clickedMachineGroup = !this.clickedMachineGroup;
  }

  clickOperationGroup(){
    this.clickedOperationGroup = !this.clickedOperationGroup;
  }

  clickOperation(){
    this.clickedOperation = !this.clickedOperation;
  }

  clickCultureGroup(){
    this.clickedCultureGroup = !this.clickedCultureGroup;
  }

  clickCulture(){
    this.clickedCulture = !this.clickedCulture;
  }

  clickVariety(){
    this.clickedVariety = !this.clickedVariety;
  }

  dismissAll(){
    this.clickedCrop = false;
    this.clickedMachine = false;
    this.clickedEmployee = false;
    this.clickedField = false;
    this.clickedFieldGroup = false;
    this.clickedMachineGroup = false;
    this.clickedOperationGroup = false;
    this.clickedOperation = false;
    this.clickedCulture = false;
    this.clickedCultureGroup = false;
    this.clickedVariety = false;
  }

  selectAll(){
    this.clickedCrop = true;
    this.clickedMachine = true;
    this.clickedEmployee = true;
    this.clickedField = true;
    this.clickedFieldGroup = true;
    this.clickedMachineGroup = true;
    this.clickedOperationGroup = true;
    this.clickedOperation = true;
    this.clickedCulture = true;
    this.clickedCultureGroup = true;
    this.clickedVariety = true;
  }

  sync(){
    if(
      this.clickedCrop == false &&
      this.clickedMachine == false &&
      this.clickedEmployee == false &&
      this.clickedField == false &&
      this.clickedFieldGroup == false &&
      this.clickedMachineGroup == false &&
      this.clickedOperationGroup == false &&
      this.clickedOperation == false &&
      this.clickedCulture == false &&
      this.clickedCultureGroup == false &&
      this.clickedVariety == false
      ){
        this.toastr.error("Ni jedan matični podatak nije označen.")
      } else {
        if(this.clickedCrop){
          this.cropService.syncCrop().subscribe(data => {
            this.loadingCrop = false;
            this.syncedCrop = true;
            console.log("Crops", data);
          }, error => {
            this.loadingCrop = false;
            this.syncedCrop = false;
            console.log("Crops error", error);
          })
        }
        if(this.clickedMachine){
          this.machineService.syncMachine().subscribe(data => {
            this.loadingMachine = false;
            this.syncedMachine = true;
            console.log("Machines", data);
          }, error => {
            this.loadingMachine = false;
            this.syncedMachine = false;
            console.log("Machine error", error);
          })
        }
        if(this.clickedEmployee){
          this.userService.syncEmployee().subscribe(data => {
            this.loadingEmployee = false;
            this.syncedEmployee = true;
            console.log("Employees", data);
          }, error => {
            this.loadingEmployee = false;
            this.syncedEmployee = false;
            console.log("Employee error", error);
          })
        }
        if(this.clickedField){
          this.fieldService.syncField().subscribe(data => {
            this.loadingField = false;
            this.syncedField = true;
            console.log("Fields", data);
          }, error => {
            this.loadingField = false;
            this.syncedField = false;
            console.log("Fields error", error);
          })
        }
        if(this.clickedFieldGroup){
          this.fieldGroupService.syncFieldGroup().subscribe(data => {
            this.loadingFieldGroup = false;
            this.syncedFieldGroup = true;
            console.log("Field groups", data);
          }, error => {
            this.loadingFieldGroup = false;
            this.syncedFieldGroup = false;
            console.log("Field groups error", error);
          })
        }
        if(this.clickedMachineGroup){
          this.machineGroupService.syncMachineGroup().subscribe(data => {
            this.loadingMachineGroup = false;
            this.syncedMachineGroup = true;
            console.log("Machine groups", data);
          }, error => {
            this.loadingMachineGroup = false;
            this.syncedMachineGroup = false;
            console.log("Machine groups error", error);
          })
        }
        if(this.clickedOperationGroup){
          this.operationGroupService.syncOperationGroup().subscribe(data => {
            this.loadingOperationGroup = false;
            this.syncedOperationGroup = true;
            console.log("Operation groups", data);
          }, error => {
            this.loadingOperationGroup = false;
            this.syncedOperationGroup = false;
            console.log("Operation groups error", error);
          })
        }
        if(this.clickedOperation){
          this.operationService.syncOperation().subscribe(data => {
            this.loadingOperation = false;
            this.syncedOperation = true;
            console.log("Operations", data);
          }, error => {
            this.loadingOperation = false;
            this.syncedOperation = false;
            console.log("Operations error", error);
          })
        }
        if(this.clickedCulture){
          this.culutreService.syncCulture().subscribe(data => {
            this.loadingCulture = false;
            this.syncedCulture = true;
            console.log("Cultures", data);
          }, error => {
            this.loadingCulture = false;
            this.syncedCulture = false;
            console.log("Cultures error", error)
          })
        }
        if(this.clickedCultureGroup){
          this.cultureGroupService.syncCultureGroup().subscribe(data => {
            this.loadingCultureGroup = false;
            this.syncedCultureGroup = true;
            console.log("Culture groups", data);
          }, error => {
            this.loadingCultureGroup = false;
            this.syncedCultureGroup = false;
            console.log("Culture groups error", error);
          })
        }
        if(this.clickedVariety){
          this.varietyService.syncVariety().subscribe(data => {
            this.loadingVariety = false;
            this.syncedVariety = true;
            console.log("Varieties", data);
          }, error => {
            this.loadingVariety = false;
            this.syncedVariety = false;
            console.log("Varieties error", error);
          })
        }
        //this.dismissAll();
        if(this.syncedCrop || this.syncedCulture || this.syncedCultureGroup || this.syncedEmployee || this.syncedField
          || this.syncedFieldGroup || this.syncedMachine || this.syncedMachineGroup || this.syncedOperation 
          || this.syncedOperationGroup || this.syncedVariety){
            this.repeat = false;
          } else {
            this.repeat = true;
          }
      }
  }

  repeatSync() {
    if(!this.syncedCrop) {
      this.loadingCrop = true;
      this.cropService.syncCrop().subscribe(data => {
        this.loadingCrop = false;
        this.syncedCrop = true;
        console.log("Crops", data);
      }, error => {
        this.loadingCrop = false;
        this.syncedCrop = false;
        console.log("Crops error", error);
      })
    }
    if(!this.syncedCulture) {
      this.loadingCulture = true;
      this.culutreService.syncCulture().subscribe(data => {
        this.loadingCulture = false;
        this.syncedCulture = true;
        console.log("Cultures", data);
      }, error => {
        this.loadingCulture = false;
        this.syncedCulture = false;
        console.log("Cultures error", error)
      })
    }
    if(!this.syncedCultureGroup) {
      this.loadingCultureGroup = true;
      this.cultureGroupService.syncCultureGroup().subscribe(data => {
        this.loadingCultureGroup = false;
        this.syncedCultureGroup = true;
        console.log("Culture groups", data);
      }, error => {
        this.loadingCultureGroup = false;
        this.syncedCultureGroup = false;
        console.log("Culture groups error", error);
      })
    }
    if(!this.syncedEmployee) {
      this.loadingEmployee = true;
      this.userService.syncEmployee().subscribe(data => {
        this.loadingEmployee = false;
        this.syncedEmployee = true;
        console.log("Employees", data);
      }, error => {
        this.loadingEmployee = false;
        this.syncedEmployee = false;
        console.log("Employee error", error);
      })
    }
    if(!this.syncedField) {
      this.loadingField = true;
      this.fieldService.syncField().subscribe(data => {
        this.loadingField = false;
        this.syncedField = true;
        console.log("Fields", data);
      }, error => {
        this.loadingField = false;
        this.syncedField = false;
        console.log("Fields error", error);
      })
    }
    if(!this.syncedFieldGroup) {
      this.loadingFieldGroup = true;
      this.fieldGroupService.syncFieldGroup().subscribe(data => {
        this.loadingFieldGroup = false;
        this.syncedFieldGroup = true;
        console.log("Field groups", data);
      }, error => {
        this.loadingFieldGroup = false;
        this.syncedFieldGroup = false;
        console.log("Field groups error", error);
      })
    }
    if(!this.syncedMachine) {
      this.loadingMachine = true;
      this.machineService.syncMachine().subscribe(data => {
        this.loadingMachine = false;
        this.syncedMachine = true;
        console.log("Machines", data);
      }, error => {
        this.loadingMachine = false;
        this.syncedMachine = false;
        console.log("Machine error", error);
      })
    }
    if(!this.syncedMachineGroup) {
      this.loadingMachineGroup = true;
      this.machineGroupService.syncMachineGroup().subscribe(data => {
        this.loadingMachineGroup = false;
        this.syncedMachineGroup = true;
        console.log("Machine groups", data);
      }, error => {
        this.loadingMachineGroup = false;
        this.syncedMachineGroup = false;
        console.log("Machine groups error", error);
      })
    }
    if(!this.syncedOperation) {
      this.loadingOperation = true;
      this.operationService.syncOperation().subscribe(data => {
        this.loadingOperation = false;
        this.syncedOperation = true;
        console.log("Operations", data);
      }, error => {
        this.loadingOperation = false;
        this.syncedOperation = false;
        console.log("Operations error", error);
      })
    }
    if(!this.syncedOperationGroup) {
      this.loadingOperationGroup = true;
      this.operationGroupService.syncOperationGroup().subscribe(data => {
        this.loadingOperationGroup = false;
        this.syncedOperationGroup = true;
        console.log("Operation groups", data);
      }, error => {
        this.loadingOperationGroup = false;
        this.syncedOperationGroup = false;
        console.log("Operation groups error", error);
      })
    }
    if(!this.syncedVariety) {
      this.loadingVariety = true;
      this.varietyService.syncVariety().subscribe(data => {
        this.loadingVariety = false;
        this.syncedVariety = true;
        console.log("Varieties", data);
      }, error => {
        this.loadingVariety = false;
        this.syncedVariety = false;
        console.log("Varieties error", error);
      })
    }
  }

}
