import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchCultureCultureType',
  pure: false,
})
export class SearchCultureCultureTypePipe implements PipeTransform {

  transform(cultures: any[], query): any {
    if(!query){
      return cultures;
    }
    var result = cultures.filter(culture => 
      culture.Type.toLowerCase().includes(query.toLowerCase()));
    
    if(result.length === 0){
      return[-1]
    }
    return result;
  }

}
