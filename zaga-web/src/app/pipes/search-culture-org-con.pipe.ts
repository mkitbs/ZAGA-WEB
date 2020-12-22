import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchCultureOrgCon',
  pure: false,
})
export class SearchCultureOrgConPipe implements PipeTransform {

  transform(culutres: any[], query): any {
    if(!query){
      return culutres;
    }
    var result = culutres.filter(culture =>
      culture.OrgKon.toLowerCase().includes(query.toLowerCase()));

    if(result.length === 0){
      return[-1]
    }
    return result;
  }

}
