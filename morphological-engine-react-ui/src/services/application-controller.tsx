import axios from 'axios';
import { ConjugationTemplate } from '../components/model/conjugation-template';
import { InputData, OutputFormat } from '../components/model/models';
import { MorphologicalChart } from '../components/model/morphological-chart';
import FileSaver from 'file-saver';
import { ChartConfiguration } from '../components/model/chart-configuration';
import { List } from 'immutable';

export class ApplicationController {

    private static MORPHOLOGICAL_ENGINE_URL = `${process.env.REACT_APP_MORPHOLOGICAL_ENGINE_HOST}/morphologicalEngine/morphologicalChart/format/`;

    public async getMorphologicalChart(body: ConjugationTemplate, outputFormat: OutputFormat = OutputFormat.UNICODE): Promise<MorphologicalChart[]> {
        const url = `${ApplicationController.MORPHOLOGICAL_ENGINE_URL}${OutputFormat[outputFormat]}`;
        const response = await axios.post<MorphologicalChart[]>(url, body);
        if (response.status !== 200) {
            return Promise.reject(`Invalid status: ${response.status}:${response.statusText}`);
        }
        return response.data.map((item) => MorphologicalChart.of(item));
    }

    public async exportToWord(body: ConjugationTemplate) {
        const url = `${ApplicationController.MORPHOLOGICAL_ENGINE_URL}${OutputFormat[OutputFormat.STREAM]}`;
        const response = await axios.post(url, body, { responseType: 'blob' });
        if (response.status !== 200) {
            return Promise.reject(`Invalid status: ${response.status}:${response.statusText}`);
        }
        const blob = new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.wordprocessingml.document' });
        window.open(window.URL.createObjectURL(blob), '_export');
        return null;
    }

    public saveFile(projectName: string, inputDatas: List<InputData>, configuration: ChartConfiguration) {
        const fileName = `${projectName}.json`;
        FileSaver.saveAs(new Blob([JSON.stringify({projectName: projectName, data: inputDatas, configuration: configuration})]), fileName);
    }
}
