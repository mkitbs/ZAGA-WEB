import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchCultureProductionType',
  pure: false,
})
export class SearchCultureProductionTypePipe implements PipeTransform {

  transform(cultures: any[], query): any {
    return cultures.filter(culture =>
      culture.OrgKon.toLowerCase().includes(query.toLowerCase()));
  }

}
