import { TabPanel, TabView } from 'primereact/tabview';
import * as React from 'react';
import { ApplicationController } from '../services/application-controller';
import Emitter from '../services/event-emitter';
import { EmitterConstants } from './emitter-constants';
import InputTable from './input-table';
import { ConjugationTemplate } from './model/conjugation-template';
import { InputData, Project } from './model/models';
import { MorphologicalChart } from './model/morphological-chart';
import { MorphologicalChartsView } from './morphological-chart-view/morphological-charts-view';

interface Props {
    project: Project
}

interface State {
    activeTabIndex: number
    disableConjugationTab: boolean
    charts: MorphologicalChart[]
}

export class ProjectView extends React.Component<Props, State> {

    private applicationController = new ApplicationController();

    constructor(props: Props) {
        super(props);

        this.state = {
            activeTabIndex: 0,
            disableConjugationTab: true,
            charts: []
        };
    }

    componentDidMount() {
        this.loadInitialData();
        Emitter.on(EmitterConstants.ROW_ADDED, (data: InputData) => this.loadConjugation(EmitterConstants.ROW_ADDED, data));
        Emitter.on(EmitterConstants.ROW_UPDATED, (data: InputData) => this.loadConjugation(EmitterConstants.ROW_UPDATED, data));
        Emitter.on(EmitterConstants.ROWS_DELETED, (deletedIds: string[]) => this.removeConjugations(deletedIds));
    }

    componentWillUnmount() {
        Emitter.off(EmitterConstants.ROW_ADDED);
        Emitter.off(EmitterConstants.ROW_UPDATED);
        Emitter.off(EmitterConstants.ROWS_DELETED);
    }

    private loadInitialData() {
        const data = this.props.project.data;
        if (data.length > 0) {
            const conjugationData = data.map((inputData) => inputData.toConjugationData());
            this.applicationController
                .getMorphologicalChart(new ConjugationTemplate(conjugationData))
                .then((charts) => {
                    this.setState({
                        charts: charts,
                        disableConjugationTab: false
                    });
                });
        }
    }

    private loadConjugation(action: string, data: InputData) {
        const conjugationData = data.toConjugationData();
        const index = EmitterConstants.ROW_UPDATED === action ? this.findIndexById(data.id) : -1;
        this.applicationController
            .getMorphologicalChart(new ConjugationTemplate([conjugationData]))
            .then((results) => {
                const charts = this.state.charts;
                if (index > -1) {
                    charts[index] = results[0];
                } else {
                    charts.push(results[0]);
                }
                this.setState({
                    charts: charts,
                    disableConjugationTab: false
                });
            })
    }

    private removeConjugations(deletedIds: string[]) {
        const deletedIndices = deletedIds.map((id) => this.findIndexById(id)).filter((index) => index > -1);
        const charts = this.state.charts.filter((_, index) => deletedIndices.indexOf(index) === -1);
        this.setState({
            charts: charts
        });
    }

    private findIndexById(id: string): number {
        let result = -1;
        const charts = this.state.charts;
        for (let index = 0; index < charts.length; index++) {
            const chart = charts[index];
            if (chart.id === id) {
                result = index;
                break;
            }
        }
        return result;
    }

    render() {
        return (
            <TabView activeIndex={this.state.activeTabIndex} onTabChange={(e) => this.setState({ activeTabIndex: e.index })}>
                <TabPanel header="Table">
                    <InputTable initialData={this.props.project!.data} />
                </TabPanel>
                <TabPanel header="Conjugation" disabled={this.state.disableConjugationTab}>
                    <MorphologicalChartsView charts={this.state.charts} />
                </TabPanel>
            </TabView>
        );
    }
}