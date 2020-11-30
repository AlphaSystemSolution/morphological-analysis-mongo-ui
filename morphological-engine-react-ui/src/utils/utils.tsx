import { ArabicLetter } from "../components/model/arabic-letter";
import { InputData } from "../components/model/models";

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

    private static getLetterCode(letter: ArabicLetter): string {
        return ArabicLetter.HAMZA.equals(letter) ? ArabicLetter.ALIF.code : letter.code;
    }

    public static viewDictionary(rowData: InputData) {
        const rootLetters = rowData.rootLetters;
        let searchString = Utils.getLetterCode(rootLetters.firstRadical) + Utils.getLetterCode(rootLetters.secondRadical);
        if (!rootLetters.secondRadical.equals(rootLetters.thirdRadical)) {
            searchString += Utils.getLetterCode(rootLetters.thirdRadical);
        }
        const fourthRadical = rootLetters.fourthRadical;
        if (fourthRadical && !ArabicLetter.TATWEEL.equals(fourthRadical)) {
            searchString += Utils.getLetterCode(fourthRadical);
        }
        const url = process.env.REACT_APP_DICTIONARY_URL + searchString;
        window.open(url, '_dictionary');
    }
}