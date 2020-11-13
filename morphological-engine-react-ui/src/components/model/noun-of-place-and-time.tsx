import { ArabicLabel } from './models';

export class NounOfPlaceAndTime extends ArabicLabel {
    constructor(public name: string, public label: string, public code: string) {
        super(name, label, code);
    }
}
