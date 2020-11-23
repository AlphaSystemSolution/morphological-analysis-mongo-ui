export class Utils {

    public static copyArray(array: any[]) {
        return array.map(x => Object.assign({}, x));
    }

    public static chunkArray<T>(srcArray: T[], chunkSize: number): T[][] {
        const result = [];
        while (srcArray.length) {
            result.push(srcArray.splice(0, chunkSize));
        }
        return result;
    }
}