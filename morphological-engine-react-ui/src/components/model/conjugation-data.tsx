import { ConjugationConfiguration, Document } from './models';
import { MorphologicalInput } from './morphological-input';
import { RootLetters as _RootLetters } from './root-letters';
import { VerbalNoun } from './verbal-noun';
import { RootLetters } from './conjugation-header';

export class ConjugationData extends Document {

    constructor(
        public template: string,
        public rootLetters: RootLetters,
        public configuration: ConjugationConfiguration,
        public translation: string,
        public verbalNouns: string[]) {
        super();
    }

    static fromMorphologicalInput(input: MorphologicalInput): ConjugationData {
        return new ConjugationData(input.template.name,
            RootLetters.of(input.rootLetters),
            input.configuration,
            input.translation,
            ConjugationData.createVerbalNouns(input.verbalNouns));
    }

    private static createVerbalNouns(verbalNouns: VerbalNoun[]): string[] {
        if (!verbalNouns) {
            return [];
        }
        const result: string[] = [];
        verbalNouns.forEach(vn => result.push(vn.name));
        return result;
    }
}
