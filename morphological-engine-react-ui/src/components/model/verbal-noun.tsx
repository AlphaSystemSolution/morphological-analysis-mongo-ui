import { ArabicLabel } from './arabic-label';
import { NamedTemplate } from './named-template';

export class VerbalNoun extends ArabicLabel {
    static VERBAL_NOUN_V1 = new VerbalNoun('VERBAL_NOUN_V1', 'فَعْلٌ', 'VERBAL_NOUN_V1');
    static VERBAL_NOUN_V2 = new VerbalNoun('VERBAL_NOUN_V2', 'فُعُلٌ', 'VERBAL_NOUN_V2');
    static VERBAL_NOUN_V3 = new VerbalNoun('VERBAL_NOUN_V3', 'فُعْلٌ', 'VERBAL_NOUN_V3');
    static VERBAL_NOUN_V4 = new VerbalNoun('VERBAL_NOUN_V4', 'فَعَلٌ', 'VERBAL_NOUN_V4');
    static VERBAL_NOUN_V5 = new VerbalNoun('VERBAL_NOUN_V5', 'فَعِلٌ', 'VERBAL_NOUN_V5');
    static VERBAL_NOUN_V6 = new VerbalNoun('VERBAL_NOUN_V6', 'فُعَلٌ', 'VERBAL_NOUN_V6');
    static VERBAL_NOUN_V7 = new VerbalNoun('VERBAL_NOUN_V7', 'فِعْلٌ', 'VERBAL_NOUN_V7');
    static VERBAL_NOUN_V8 = new VerbalNoun('VERBAL_NOUN_V8', 'فِعَلٌ', 'VERBAL_NOUN_V8');
    static VERBAL_NOUN_V9 = new VerbalNoun('VERBAL_NOUN_V9', 'فَعْلَةٌ', 'VERBAL_NOUN_V9');
    static VERBAL_NOUN_V10 = new VerbalNoun('VERBAL_NOUN_V10', 'فِعْلَةٌ', 'VERBAL_NOUN_V10');
    static VERBAL_NOUN_V11 = new VerbalNoun('VERBAL_NOUN_V11', 'فُعْلَةٌ', 'VERBAL_NOUN_V11');
    static VERBAL_NOUN_V12 = new VerbalNoun('VERBAL_NOUN_V12', 'فَعَلَةٌ', 'VERBAL_NOUN_V12');
    static VERBAL_NOUN_V13 = new VerbalNoun('VERBAL_NOUN_V13', 'فَعِلَةٌ', 'VERBAL_NOUN_V13');
    static VERBAL_NOUN_V14 = new VerbalNoun('VERBAL_NOUN_V14', 'فَعَالَةٌ', 'VERBAL_NOUN_V14');
    static VERBAL_NOUN_V15 = new VerbalNoun('VERBAL_NOUN_V15', 'فِعَالَةٌ', 'VERBAL_NOUN_V15');
    static VERBAL_NOUN_V16 = new VerbalNoun('VERBAL_NOUN_V16', 'فُعُوْلٌ', 'VERBAL_NOUN_V16');
    static VERBAL_NOUN_V17 = new VerbalNoun('VERBAL_NOUN_V17', 'فَعُوْلٌ', 'VERBAL_NOUN_V17');
    static VERBAL_NOUN_V18 = new VerbalNoun('VERBAL_NOUN_V18', 'فَعُوْلَةٌ', 'VERBAL_NOUN_V18');
    static VERBAL_NOUN_V19 = new VerbalNoun('VERBAL_NOUN_V19', 'فَعَالٌ', 'VERBAL_NOUN_V19');
    static VERBAL_NOUN_V20 = new VerbalNoun('VERBAL_NOUN_V20', 'فِعَالٌ', 'VERBAL_NOUN_V20');
    static VERBAL_NOUN_V21 = new VerbalNoun('VERBAL_NOUN_V21', 'فُعَالٌ', 'VERBAL_NOUN_V21');
    static VERBAL_NOUN_V22 = new VerbalNoun('VERBAL_NOUN_V22', 'فِعْلَى', 'VERBAL_NOUN_V22');
    static VERBAL_NOUN_V23 = new VerbalNoun('VERBAL_NOUN_V23', 'فَعْلَى', 'VERBAL_NOUN_V23');
    static VERBAL_NOUN_V24 = new VerbalNoun('VERBAL_NOUN_V24', 'فُعْلَى', 'VERBAL_NOUN_V24');
    static VERBAL_NOUN_V25 = new VerbalNoun('VERBAL_NOUN_V25', 'فَعِيْلٌ', 'VERBAL_NOUN_V25');
    static VERBAL_NOUN_V26 = new VerbalNoun('VERBAL_NOUN_V26', 'فُعْلَانٌ', 'VERBAL_NOUN_V26');
    static VERBAL_NOUN_V27 = new VerbalNoun('VERBAL_NOUN_V27', 'مَفْعَلَةٌ', 'VERBAL_NOUN_V27');
    static VERBAL_NOUN_V28 = new VerbalNoun('VERBAL_NOUN_V28', 'مَفْعِلَةٌ', 'VERBAL_NOUN_V28');
    static VERBAL_NOUN_FORM_II = new VerbalNoun('VERBAL_NOUN_FORM_II', 'تَفْعِيْلٌ', 'VERBAL_NOUN_FORM_II');
    static VERBAL_NOUN_FORM_II_DEFECTIVE_VERB = new VerbalNoun('VERBAL_NOUN_FORM_II_DEFECTIVE_VERB', 'تَفْعِلَةٌ',
        'VERBAL_NOUN_FORM_II_DEFECTIVE_VERB');
    static VERBAL_NOUN_FORM_III_V1 = new VerbalNoun('VERBAL_NOUN_FORM_III_V1', 'فِعَالٌ', 'VERBAL_NOUN_FORM_III_V1');
    static VERBAL_NOUN_FORM_III_V2 = new VerbalNoun('VERBAL_NOUN_FORM_III_V2', 'مُفَاعِلَةٌ', 'VERBAL_NOUN_FORM_III_V2');
    static VERBAL_NOUN_FORM_III_DEFECTIVE_VERB = new VerbalNoun('VERBAL_NOUN_FORM_III_DEFECTIVE_VERB',
        'مُفَاعَلَةٌ', 'VERBAL_NOUN_FORM_III_DEFECTIVE_VERB');
    static VERBAL_NOUN_FORM_IV = new VerbalNoun('VERBAL_NOUN_FORM_IV', 'إِفْعَالٌ', 'VERBAL_NOUN_FORM_IV');
    static VERBAL_NOUN_FORM_V = new VerbalNoun('VERBAL_NOUN_FORM_V', 'تَفَعُّلٌ', 'VERBAL_NOUN_FORM_V');
    static VERBAL_NOUN_FORM_VI = new VerbalNoun('VERBAL_NOUN_FORM_VI', 'تَفَاعُلٌ', 'VERBAL_NOUN_FORM_VI');
    static VERBAL_NOUN_FORM_VII = new VerbalNoun('VERBAL_NOUN_FORM_VII', 'إِنْفِعَالٌ', 'VERBAL_NOUN_FORM_VII');
    static VERBAL_NOUN_FORM_VIII = new VerbalNoun('VERBAL_NOUN_FORM_VIII', 'إِفْتِعَالٌ', 'VERBAL_NOUN_FORM_VIII');
    static VERBAL_NOUN_FORM_IX = new VerbalNoun('VERBAL_NOUN_FORM_IX', 'إِفْعِلَالٌ', 'VERBAL_NOUN_FORM_IX');
    static VERBAL_NOUN_FORM_X = new VerbalNoun('VERBAL_NOUN_FORM_X', 'إِسْتَفْعَالٌ', 'VERBAL_NOUN_FORM_X');

    static verbalNounPatterns: VerbalNoun[] = [
        VerbalNoun.VERBAL_NOUN_V1, // 0
        VerbalNoun.VERBAL_NOUN_V2, // 1
        VerbalNoun.VERBAL_NOUN_V3, // 2
        VerbalNoun.VERBAL_NOUN_V4, // 3
        VerbalNoun.VERBAL_NOUN_V5, // 4
        VerbalNoun.VERBAL_NOUN_V6, // 5
        VerbalNoun.VERBAL_NOUN_V7, // 6
        VerbalNoun.VERBAL_NOUN_V8, // 7
        VerbalNoun.VERBAL_NOUN_V9, // 8
        VerbalNoun.VERBAL_NOUN_V10, // 9
        VerbalNoun.VERBAL_NOUN_V11, // 10
        VerbalNoun.VERBAL_NOUN_V12, // 11
        VerbalNoun.VERBAL_NOUN_V13, // 12
        VerbalNoun.VERBAL_NOUN_V14, // 13
        VerbalNoun.VERBAL_NOUN_V15, // 14
        VerbalNoun.VERBAL_NOUN_V16, // 15
        VerbalNoun.VERBAL_NOUN_V17, // 16
        VerbalNoun.VERBAL_NOUN_V18, // 17
        VerbalNoun.VERBAL_NOUN_V19, // 18
        VerbalNoun.VERBAL_NOUN_V20, // 19
        VerbalNoun.VERBAL_NOUN_V21, // 20
        VerbalNoun.VERBAL_NOUN_V22, // 21
        VerbalNoun.VERBAL_NOUN_V23, // 22
        VerbalNoun.VERBAL_NOUN_V24, // 23
        VerbalNoun.VERBAL_NOUN_V25, // 24
        VerbalNoun.VERBAL_NOUN_V26, // 25
        VerbalNoun.VERBAL_NOUN_V27, // 26
        VerbalNoun.VERBAL_NOUN_V28, // 27
        VerbalNoun.VERBAL_NOUN_FORM_II, // 28
        VerbalNoun.VERBAL_NOUN_FORM_II_DEFECTIVE_VERB, // 29
        VerbalNoun.VERBAL_NOUN_FORM_III_V1, // 30
        VerbalNoun.VERBAL_NOUN_FORM_III_V2, // 31
        VerbalNoun.VERBAL_NOUN_FORM_III_DEFECTIVE_VERB, // 32
        VerbalNoun.VERBAL_NOUN_FORM_IV, // 33
        VerbalNoun.VERBAL_NOUN_FORM_V, // 34
        VerbalNoun.VERBAL_NOUN_FORM_VI, // 35
        VerbalNoun.VERBAL_NOUN_FORM_VII, // 36
        VerbalNoun.VERBAL_NOUN_FORM_VIII, // 37
        VerbalNoun.VERBAL_NOUN_FORM_IX, // 38
        VerbalNoun.VERBAL_NOUN_FORM_X, // 39
    ];

    static formITemplates: VerbalNoun[][] = [
        [VerbalNoun.VERBAL_NOUN_V1, VerbalNoun.VERBAL_NOUN_V2, VerbalNoun.VERBAL_NOUN_V3, VerbalNoun.VERBAL_NOUN_V4],
        [VerbalNoun.VERBAL_NOUN_V5, VerbalNoun.VERBAL_NOUN_V6, VerbalNoun.VERBAL_NOUN_V7, VerbalNoun.VERBAL_NOUN_V8],
        [VerbalNoun.VERBAL_NOUN_V9, VerbalNoun.VERBAL_NOUN_V10, VerbalNoun.VERBAL_NOUN_V11, VerbalNoun.VERBAL_NOUN_V12],
        [VerbalNoun.VERBAL_NOUN_V13, VerbalNoun.VERBAL_NOUN_V14, VerbalNoun.VERBAL_NOUN_V15, VerbalNoun.VERBAL_NOUN_V27],
        [VerbalNoun.VERBAL_NOUN_V28]
    ];

    static formIITemplates: VerbalNoun[][] = [
        [VerbalNoun.VERBAL_NOUN_FORM_II]
    ];

    static formIIITemplates: VerbalNoun[][] = [
        [VerbalNoun.VERBAL_NOUN_FORM_III_V1, VerbalNoun.VERBAL_NOUN_FORM_III_V2]
    ];

    static formIVTemplates: VerbalNoun[][] = [
        [VerbalNoun.VERBAL_NOUN_FORM_IV]
    ];

    static formVTemplates: VerbalNoun[][] = [
        [VerbalNoun.VERBAL_NOUN_FORM_V]
    ];

    static formVITemplates: VerbalNoun[][] = [
        [VerbalNoun.VERBAL_NOUN_FORM_VI]
    ];

    static formVIITemplates: VerbalNoun[][] = [
        [VerbalNoun.VERBAL_NOUN_FORM_VII]
    ];

    static formVIIITemplates: VerbalNoun[][] = [
        [VerbalNoun.VERBAL_NOUN_FORM_VIII]
    ];

    static formIXTemplates: VerbalNoun[][] = [
        [VerbalNoun.VERBAL_NOUN_FORM_IX]
    ];

    static formXTemplates: VerbalNoun[][] = [
        [VerbalNoun.VERBAL_NOUN_FORM_X]
    ];

    static getByTemplate(template: NamedTemplate): VerbalNoun[] {
        if (NamedTemplate.FORM_II_TEMPLATE.equals(template)) {
            return [VerbalNoun.VERBAL_NOUN_FORM_II]
        } else if (NamedTemplate.FORM_III_TEMPLATE.equals(template)) {
            return [VerbalNoun.VERBAL_NOUN_FORM_III_V1, VerbalNoun.VERBAL_NOUN_FORM_III_V2]
        } else if (NamedTemplate.FORM_IV_TEMPLATE.equals(template)) {
            return [VerbalNoun.VERBAL_NOUN_FORM_IV]
        } else if (NamedTemplate.FORM_V_TEMPLATE.equals(template)) {
            return [VerbalNoun.VERBAL_NOUN_FORM_V]
        } else if (NamedTemplate.FORM_VI_TEMPLATE.equals(template)) {
            return [VerbalNoun.VERBAL_NOUN_FORM_VI]
        } else if (NamedTemplate.FORM_VII_TEMPLATE.equals(template)) {
            return [VerbalNoun.VERBAL_NOUN_FORM_VII]
        } else if (NamedTemplate.FORM_VIII_TEMPLATE.equals(template)) {
            return [VerbalNoun.VERBAL_NOUN_FORM_VIII]
        } else if (NamedTemplate.FORM_IX_TEMPLATE.equals(template)) {
            return [VerbalNoun.VERBAL_NOUN_FORM_IX]
        } else if (NamedTemplate.FORM_X_TEMPLATE.equals(template)) {
            return [VerbalNoun.VERBAL_NOUN_FORM_X]
        }
        return [];
    }

    static getByName(name: string): VerbalNoun {
        const results = VerbalNoun.verbalNounPatterns.filter(al => (al.name === name));
        return results[0];
    }

    constructor(public name: string, public label: string, public code: string) {
        super(name, label, code);
    }

    public copy(): VerbalNoun {
        return new VerbalNoun(this.name, this.label, this.code);
    }
}
