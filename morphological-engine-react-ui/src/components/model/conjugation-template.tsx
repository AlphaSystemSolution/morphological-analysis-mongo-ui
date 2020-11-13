import { Document } from './models';
import { ConjugationData } from './conjugation-data';
import { ChartConfiguration } from './chart-configuration';
import { MorphologicalInput } from './morphological-input';

export class ConjugationTemplate extends Document {
    data: ConjugationData[] = [];
    chartConfiguration = new ChartConfiguration();

    static createConjugationTemplate(chartConfiguration: ChartConfiguration, inputs: MorphologicalInput[]): ConjugationTemplate {
        const template = new ConjugationTemplate();
        if (!chartConfiguration) {
            chartConfiguration = new ChartConfiguration();
        }
        template.chartConfiguration = chartConfiguration;
        if (inputs && inputs.length > 0) {
            const data: ConjugationData[] = [];
            inputs.forEach(input => data.push(ConjugationData.fromMorphologicalInput(input)));
            template.data = data;
        }
        return template;
    }
}
