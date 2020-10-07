import { Material } from "./Material";

export class SpentMaterial {
  id;
  quantity;
  spent;
  quantityPerHectar;
  spentPerHectar;
  material: Material = new Material();
  smObjectId;
}
