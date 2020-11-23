import { TabPanel, TabView } from 'primereact/tabview';
import * as React from 'react';
import { ApplicationController } from '../services/application-controller';
import InputTable from './input-table';
import { ConjugationTemplate } from './model/conjugation-template';
import { Project } from './model/models';
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