export class Utils {

    public static copyArray(array: any[]) {
        return array.map(x => Object.assign({}, x));
    }
}