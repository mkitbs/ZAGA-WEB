import { Component, OnInit } from '@angular/core';
import { IDropdownSettings } from 'ng-multiselect-dropdown';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Roles } from 'src/app/models/Roles';
import { Setting } from 'src/app/models/Setting';
import { SignupRequest } from 'src/app/models/SignupRequest';
import { User } from 'src/app/models/User';
import { AuthService } from 'src/app/service/auth/auth.service';
import { UserService } from 'src/app/service/user.service';

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
    private authService: AuthService
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
      this.userService.updateUser(this.user).subscribe(res => {
        this.selectedIndex = 0;
        this.user = new SignupRequest();
        this.toastr.success("Korisnik je uspešno sačuvan.")
      }, error => {
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

}
