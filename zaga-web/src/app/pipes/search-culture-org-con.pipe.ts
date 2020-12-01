import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchCultureOrgCon',
  pure: false,
})
export class SearchCultureOrgConPipe implements PipeTransform {

  transform(culutres: any[], query): any {
    return culutres.filter(culture =>
      culture.OrgKon.toLowerCase().includes(query.toLowerCase()));
  }

}
