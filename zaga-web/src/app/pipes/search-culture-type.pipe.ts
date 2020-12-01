import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchCultureType',
  pure: false,
})
export class SearchCultureTypePipe implements PipeTransform {

  transform(cultures: any[], query): any {
    return cultures.filter(culture =>
      culture.Type.toLowerCase().includes(query.toLowerCase()));
  }

}
