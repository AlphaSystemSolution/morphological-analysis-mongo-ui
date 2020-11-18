import { Document } from './document';
import { ConjugationData } from './conjugation-data';
import { ChartConfiguration } from './chart-configuration';

export class ConjugationTemplate extends Document {

    constructor(public data: ConjugationData[] = [], public chartConfiguration: ChartConfiguration = new ChartConfiguration()) {
        super();
    }

    public static of(src?: any): ConjugationTemplate {
        if (!src) {
            throw new Error(`Unable to create ConjugationTemplate from: ${src}`);
        }
        return new ConjugationTemplate(
            src.data.map((d: any) => ConjugationData.of(d)),
            ChartConfiguration.of(src.chartConfiguration)
        );
    }
}
