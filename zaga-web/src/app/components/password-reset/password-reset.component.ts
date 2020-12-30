import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-password-reset',
  templateUrl: './password-reset.component.html',
  styleUrls: ['./password-reset.component.css']
})
export class PasswordResetComponent implements OnInit {

  constructor(
    private route: ActivatedRoute,
  ) { }

  token = this.route.snapshot.params.token;
  new;

  ngOnInit() {
    if(this.token == "new"){
      this.new = true;
    } else {
      this.new = false;
    }
  }

}
