import { List } from 'immutable';
import { action, observable } from 'mobx';
import { v4 as uuid } from 'uuid';
import { ChartConfiguration } from '../components/model/chart-configuration';
import { ConjugationTemplate } from '../components/model/conjugation-template';
import { InputData } from '../components/model/models';
import { MorphologicalChart } from '../components/model/morphological-chart';
import { ApplicationController } from '../services/application-controller';

export default class Project {

    private applicationController: ApplicationController = new ApplicationController();
    id: string = uuid();
    transient: boolean = true;
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
        } else {
            this.data = this.data.push(data);
        }
    }

    @action removeData = (rowsToDelete: string[]) => {
        this.data = this.data.filter((row) => !rowsToDelete.includes(row.id));
    }

    @action loadConjugationData = () => {
        const projectData = this.data;
        if (!projectData.isEmpty) {
            const conjugationData = projectData.map((id) => id.toConjugationData()).toArray();
            return this.applicationController
                .getMorphologicalChart(new ConjugationTemplate(conjugationData))
                .then((charts) => {
                    this.charts = List(charts);
                });
        }
    }

}