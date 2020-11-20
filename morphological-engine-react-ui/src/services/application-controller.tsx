import axios from 'axios';
import { ConjugationTemplate } from '../components/model/conjugation-template';
import { OutputFormat } from '../components/model/models';
import { MorphologicalChart } from '../components/model/morphological-chart';

export class ApplicationController {

    public async getMorphologicalChart(outputFormat: OutputFormat, body: ConjugationTemplate): Promise<MorphologicalChart[]> {
        const response = await axios.post<MorphologicalChart[]>(`${process.env.REACT_APP_MORPHOLOGICAL_ENGINE_URL}${OutputFormat[outputFormat]}`, body);
        if (response.status != 200) {
            return Promise.reject(`Invalid status: ${response.status}:${response.statusText}`);
        }
        return response.data.map((item) => MorphologicalChart.of(item));
    }
}