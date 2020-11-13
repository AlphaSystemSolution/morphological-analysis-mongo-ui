export class IdGenerator {

    private static salt = 0x9e3779b9;
    static instance: IdGenerator = new IdGenerator();
  
    private seed: number;
  
    private static hi16(num: number): number {
      // tslint:disable-next-line:no-bitwise
      return (((num >>> 24 & 0xff)) | ((num >>> 16 & 0xff) << 8));
    }
  
    private static lo16(num: number): number {
      // tslint:disable-next-line:no-bitwise
      return ((num >>> 8 & 0xff)) | ((num & 0xff) << 8);
    }
  
    private static result(v0: number, v1: number): number {
      // tslint:disable-next-line:no-bitwise
      return ((v0 & 0xff) << 24) | (((v0 >>> 8) & 0xff) << 16) | ((v1 & 0xff) << 8) | ((v1 >>> 8) & 0xff);
    }
  
    private static mix(num: number): number {
      let v0 = IdGenerator.hi16(num);
      let v1 = IdGenerator.lo16(num);
      // tslint:disable-next-line:no-bitwise
      v0 += ((v1 << 2) + 0 ^ v1) + (IdGenerator.salt ^ (v1 >>> 3)) + 1;
      // tslint:disable-next-line:no-bitwise
      v1 += ((v0 << 2) + 2 ^ v0) + (IdGenerator.salt ^ (v0 >>> 3)) + 3;
      return IdGenerator.result(v0, v1);
    }
  
    private static format(id: number): string {
      const char: string[] = [];
      for (let p = 7; 0 <= p; p--) {
        // tslint:disable-next-line:no-bitwise
        const h = id & 0xf;
        char[p] = h.toString(16);
        // tslint:disable-next-line:no-bitwise
        id >>= 4;
      }
      let result = '';
      char.forEach(r => result = result + r);
      return result.toUpperCase();
    }
  
    static nextId(): string {
      return IdGenerator.format(IdGenerator.instance.next());
    }
  
    constructor() {
      this.seed = Math.random();
    }
  
    next(): number {
      return IdGenerator.mix(this.getAndIncrement());
    }
  
    private getAndIncrement(): number {
      const current = this.seed++;
      return current;
    }
  }
  