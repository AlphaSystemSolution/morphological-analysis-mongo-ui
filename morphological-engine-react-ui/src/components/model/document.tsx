import { IdGenerator } from '../../utils/id-generator';

export class Document {
    private _id: string;
  
    constructor() {
      this._id = IdGenerator.nextId();
    }
  
    get id(): string {
      return this._id;
    }
  
    set id(id: string) {
      this._id = id ? id : IdGenerator.nextId();
    }
  }