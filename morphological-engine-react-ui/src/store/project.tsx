import { action, observable } from 'mobx';
import { v4 as uuid } from 'uuid';
import { ChartConfiguration } from '../components/model/chart-configuration';

export default class Project {

    id: string = uuid();
    transient: boolean = true;
    @observable projectName: string = "Untitled1"
    @observable chartConfiguration: ChartConfiguration = new ChartConfiguration();

    constructor(
        projectName: string,
        transient: boolean = true,
        chartConfiguration: ChartConfiguration = new ChartConfiguration()
    ) {
        this.projectName = projectName;
        this.transient = transient;
        this.chartConfiguration = chartConfiguration;
    }

    @action updateProjectName = (projectName: string) => {
        this.projectName = projectName;
        this.transient = false;
    }

    @action updateChartConfiguration = (chartConfiguration: ChartConfiguration) => {
        this.chartConfiguration = chartConfiguration;
    }

}