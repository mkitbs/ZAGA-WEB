import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchCropField',
  pure: false,
})
export class SearchCropFieldPipe implements PipeTransform {

  transform(crops: any[], query): any {
    if(!query){
      return crops;
    }
    return crops.filter((crop) =>
      this.matchValue(crop, query)
    );
  }

  matchValue(data, value) {
    if(Number(value)){
      var numString = value.toString();
      var idString = data.FieldId.toString();
      return idString.toLowerCase().includes(numString.toLowerCase())
    } else if(value.includes(" - ")){
      return data.fieldName.toLowerCase().includes(value.split(" - ")[1].toLowerCase())
    } else{
      return data.fieldName.toLowerCase().includes(value.toLowerCase()) 
    }
  }

}
