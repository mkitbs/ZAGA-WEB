import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchCultureCultureType',
  pure: false,
})
export class SearchCultureCultureTypePipe implements PipeTransform {

  transform(cultures: any[], query): any {
    return cultures.filter(culture => 
      culture.Type.toLowerCase().includes(query.toLowerCase()));
  }

}
