import { List } from 'immutable';
import { action, observable } from 'mobx';
import { v4 as uuid } from 'uuid';
import { ChartConfiguration } from '../components/model/chart-configuration';
import { ConjugationTemplate } from '../components/model/conjugation-template';
import { ExportType, InputData } from '../components/model/models';
import { MorphologicalChart } from '../components/model/morphological-chart';
import { ApplicationController } from '../services/application-controller';

export default class Project {

    private applicationController: ApplicationController = new ApplicationController();
    id: string = uuid();
    @observable transient: boolean = true;
    @observable projectName: string = "Untitled1"
    @observable chartConfiguration: ChartConfiguration = new ChartConfiguration();
    @observable data: List<InputData> = List([]);
    @observable charts: List<MorphologicalChart> = List([]);

    constructor(
        projectName: string,
        transient: boolean = true,
        chartConfiguration: ChartConfiguration = new ChartConfiguration(),
        data: List<InputData> = List([]),
        charts: List<MorphologicalChart> = List([])
    ) {
        this.projectName = projectName;
        this.transient = transient;
        this.chartConfiguration = chartConfiguration;
        this.data = data;
        this.charts = charts;
    }

    @action updateProjectName = (projectName: string) => {
        this.projectName = projectName;
        this.transient = false;
    }

    @action updateChartConfiguration = (chartConfiguration: ChartConfiguration) => {
        this.chartConfiguration = chartConfiguration;
    }

    @action addData = (index: number, data: InputData) => {
        if (index > -1) {
            this.data = this.data.set(index, data);
            this.getChart(data)
                .then((chart) => {
                    this.charts = this.charts.set(index, chart);
                });
        } else {
            this.data = this.data.push(data);
            this.getChart(data)
                .then((chart) => {
                    this.charts = this.charts.push(chart);
                });
        }
    }

    @action removeData = (rowsToDelete: string[]) => {
        this.data = this.data.filter((row) => !rowsToDelete.includes(row.id));
        this.charts = this.charts.filter((row) => !rowsToDelete.includes(row.id));
    }

    @action loadConjugationData = async () => {
        const projectData = this.data;
        let loaded = false;
        if (!projectData.isEmpty()) {
            const conjugationData = projectData.map((id) => id.toConjugationData()).toArray();
            const charts = await this.applicationController
                .getMorphologicalChart(new ConjugationTemplate(conjugationData));
            this.charts = List(charts);
            loaded = true;
        }
        return Promise.resolve(loaded);
    }

    @action saveProject = () => {
        this.applicationController.saveFile(this.projectName, this.data, this.chartConfiguration);
    }

    exportToWord = (exportType: ExportType = ExportType.BOTH, selectedData: InputData[] = []) => {
        const configuration = ChartConfiguration.of(this.chartConfiguration);
        switch (exportType) {
            case ExportType.ABBREVIATED_CONJUGATION:
                configuration.omitToc = true;
                configuration.omitHeader = true;
                configuration.omitDetailedConjugation = true;
                break;
            case ExportType.DETAILED_CONJUGATION:
                configuration.omitToc = true;
                configuration.omitHeader = true;
                configuration.omitAbbreviatedConjugation = true;
                break;
            default:
                break;
        }
        const data = selectedData.length <= 0 ? this.data.toArray() : selectedData;
        if (data.length > 0) {
            const conjugationData = data.map((inputData) => inputData.toConjugationData());
            return this.applicationController.exportToWord(new ConjugationTemplate(conjugationData, configuration));
        }
        return Promise.resolve(null);
    }

    private getChart = async (data: InputData) => {
        const charts = await this.applicationController
            .getMorphologicalChart(new ConjugationTemplate([data.toConjugationData()]));
        return charts[0];
    }

}
