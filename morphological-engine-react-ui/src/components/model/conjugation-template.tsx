import { Document } from './document';
import { ConjugationData } from './conjugation-data';
import { ChartConfiguration } from './chart-configuration';

export class ConjugationTemplate extends Document {

    constructor(public data: ConjugationData[] = [], public chartConfiguration: ChartConfiguration = new ChartConfiguration()){
        super();
    }
}
