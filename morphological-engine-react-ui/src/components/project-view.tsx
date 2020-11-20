import { TabPanel, TabView } from 'primereact/tabview';
import * as React from 'react';
import Emitter from '../services/event-emitter';
import { AbbreviatedConjugationView } from './abbreviated-conjugation-view';
import { EmitterConstants } from './emitter-constants';
import InputTable from './input-table';
import { Project } from './model/models';
import { MorphologicalChart } from './model/morphological-chart';

interface Props {
    project: Project
}

interface State {
    activeTabIndex: number
    disableConjugationTab: boolean
    chart: MorphologicalChart
}

export class ProjectView extends React.Component<Props, State> {

    constructor(props: Props) {
        super(props);

        this.showMorphologicalChart = this.showMorphologicalChart.bind(this);

        this.state = {
            activeTabIndex: 0,
            disableConjugationTab: true,
            chart: new MorphologicalChart()
        };
    }

    componentDidMount() {
        Emitter.on(EmitterConstants.MORPHOLOGICAL_CHART, this.showMorphologicalChart);
    }

    componentWillUnmount() {
        Emitter.off(EmitterConstants.MORPHOLOGICAL_CHART);
    }

    private showMorphologicalChart(chart: MorphologicalChart) {
        this.setState({
            chart: chart,
            disableConjugationTab: false,
            activeTabIndex: 1
        });
    }

    render() {
        return (
            <TabView activeIndex={this.state.activeTabIndex} onTabChange={(e) => this.setState({ activeTabIndex: e.index })}>
                <TabPanel header="Table">
                    <InputTable initialData={this.props.project!.data} />
                </TabPanel>
                <TabPanel header="Conjugation" disabled={this.state.disableConjugationTab}>
                    <div>
                        <AbbreviatedConjugationView conjugation={this.state.chart.abbreviatedConjugation} />
                    </div>
                </TabPanel>
            </TabView>
        );
    }
}