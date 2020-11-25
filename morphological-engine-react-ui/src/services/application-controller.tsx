import axios from 'axios';
import { ConjugationTemplate } from '../components/model/conjugation-template';
import { InputData, OutputFormat } from '../components/model/models';
import { MorphologicalChart } from '../components/model/morphological-chart';
import FileSaver from 'file-saver';
import { ChartConfiguration } from '../components/model/chart-configuration';

export class ApplicationController {

    public async getMorphologicalChart(body: ConjugationTemplate, outputFormat: OutputFormat = OutputFormat.UNICODE): Promise<MorphologicalChart[]> {
        const url = `${process.env.REACT_APP_MORPHOLOGICAL_ENGINE_URL}${OutputFormat[outputFormat]}`;
        const response = await axios.post<MorphologicalChart[]>(url, body);
        if (response.status !== 200) {
            return Promise.reject(`Invalid status: ${response.status}:${response.statusText}`);
        }
        return response.data.map((item) => MorphologicalChart.of(item));
    }

    public async exportToWord(body: ConjugationTemplate) {
        const url = `${process.env.REACT_APP_MORPHOLOGICAL_ENGINE_URL}${OutputFormat[OutputFormat.STREAM]}`;
        const response = await axios.post(url, body, { responseType: 'blob' });
        if (response.status !== 200) {
            return Promise.reject(`Invalid status: ${response.status}:${response.statusText}`);
        }
        const blob = new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.wordprocessingml.document' });
        window.open(window.URL.createObjectURL(blob), '_export');
        return null;
    }

    public saveFile(fileName: string, inputDatas: InputData[], configuration: ChartConfiguration) {
        const data = inputDatas.map((item) => item.toConjugationData());
        const template = new ConjugationTemplate(data, configuration);
        FileSaver.saveAs(new Blob([JSON.stringify(template)]), fileName);
    }
}