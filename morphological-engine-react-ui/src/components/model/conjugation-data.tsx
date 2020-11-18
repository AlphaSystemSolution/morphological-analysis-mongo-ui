import { ConjugationConfiguration, InputData } from './models';
import { Document } from './document';
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
}
