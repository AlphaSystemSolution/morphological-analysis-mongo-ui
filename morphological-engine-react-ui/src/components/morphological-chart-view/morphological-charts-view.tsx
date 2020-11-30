import React, { FC, useState } from 'react';
import { Accordion, AccordionTab } from 'primereact/accordion'
import { MorphologicalChart } from '../model/morphological-chart';
import { ToggleSelecter } from '../toggle-selecter';
import { Utils } from '../../utils/utils';
import { AbbreviatedConjugationView } from './abbreviated-conjugation-view';
import { AbbreviatedConjugation } from '../model/abbreviated-conjugation';
import { DetailedConjugation } from '../model/detailed-conjugation';
import { DetailConjugationView } from './detail-conjugation-view';
import { List } from 'immutable';
import Project from '../../store/project';
import { observer } from 'mobx-react-lite';

interface Props2 {
    charts: List<MorphologicalChart>
}

interface State {
    activeTabIndex: number
    selectedChartIndex: number
    abbreviatedConjugation?: AbbreviatedConjugation
    detailConjugation?: DetailedConjugation
}

export class MorphologicalChartsView2 extends React.Component<Props2, State> {

    private static NUM_OF_COLUMNS = 8;

    constructor(props: Props2) {
        super(props);

        this.onChartSelected = this.onChartSelected.bind(this);

        this.state = {
            activeTabIndex: 0,
            selectedChartIndex: 0
        };
    }

    private onChartSelected(payload: any) {
        const index = payload.index;
        console.log(`CurrentIndex: ${index}`)
        this.setState({
            selectedChartIndex: index,
            abbreviatedConjugation: this.props.charts.get(index)!.abbreviatedConjugation,
            detailConjugation: this.props.charts.get(index)!.detailedConjugation,
            activeTabIndex: 1
        });
    }


    private renderChartTabs() {
        const charts = this.props.charts.map((c) => c.copy());
        const labels = Utils.chunkList(charts.map((chart) => chart.label), MorphologicalChartsView2.NUM_OF_COLUMNS);
        const elements =
            labels.map((labelArray, parentIndex) => {
                const labelElements = labelArray.map((label, index) => {
                    const key = (parentIndex * MorphologicalChartsView2.NUM_OF_COLUMNS) + index;
                    return (
                        <div className="p-col-12 p-md-6 p-lg-2" key={"chart-label-row-" + key}>
                            <ToggleSelecter value={label} index={key} className="chartSelector ui-button p-button-raised"
                                userKey={label.id} checked={this.state.selectedChartIndex === key} onChange={this.onChartSelected} />
                        </div>
                    );
                });
                return (
                    <React.Fragment key={"chart-label-" + parentIndex}>
                        {labelElements}
                    </React.Fragment>
                );
            });
        return (
            <div className="p-grid" style={{ direction: 'rtl' }}>
                {elements}
            </div>
        );
    }

    render() {
        const abbreviatedConjugation = this.state.abbreviatedConjugation ? this.state.abbreviatedConjugation : this.props.charts.get(0)!.abbreviatedConjugation;
        const detailConjugation = this.state.detailConjugation ? this.state.detailConjugation : this.props.charts.get(0)!.detailedConjugation;
        return (
            <React.Fragment>
                <Accordion activeIndex={this.state.activeTabIndex} onTabChange={(e) => this.setState({ activeTabIndex: e.index })}>
                    <AccordionTab header="Contents">{this.renderChartTabs()}</AccordionTab>
                    <AccordionTab header="Conjugation">
                        <AbbreviatedConjugationView conjugation={abbreviatedConjugation} />
                        <DetailConjugationView conjugation={detailConjugation} />
                    </AccordionTab>
                </Accordion>
            </React.Fragment>
        );
    }
}

interface Props {
    project: Project
}

const MorphologicalChartsView: FC<Props> = ({ project }) => {

    const numOfColumns = 8;
    const [activeTabIndex, setActiveTabIndex] = useState(0);
    const [selectedChartIndex, setSelectedChartIndex] = useState(0);

    const charts = project.charts;
    const [abbreviatedConjugation, setAbbreviatedConjugation] = useState(charts.isEmpty() ? undefined : charts.get(0)!.abbreviatedConjugation);
    const [detailConjugation, setDetailConjugation] = useState(charts.isEmpty() ? undefined : charts.get(0)!.detailedConjugation);

    const renderEmpty = () => {
        return <strong>Nothing here to display</strong>
    }

    const onChartSelected = (payload: any) => {
        const index = payload.index;
        setSelectedChartIndex(index);
        const chart = (project.charts.get(index)!);
        setAbbreviatedConjugation(chart.abbreviatedConjugation)
        setDetailConjugation(project.charts.get(index)!.detailedConjugation);
        setActiveTabIndex(1);
    }

    const renderChartTabs = () => {
        const labels = Utils.chunkArray(charts.toArray().map((chart) => chart.label), numOfColumns);
        const elements =
            labels.map((labelArray, parentIndex) => {
                const labelElements = labelArray.map((label, index) => {
                    const key = (parentIndex * numOfColumns) + index;
                    return (
                        <div className="p-col-12 p-md-6 p-lg-2" key={"chart-label-row-" + key}>
                            <ToggleSelecter value={label} index={key} className="chartSelector ui-button p-button-raised"
                                userKey={label.id} checked={selectedChartIndex === key} onChange={onChartSelected} />
                        </div>
                    );
                });
                return (
                    <React.Fragment key={"chart-label-" + parentIndex}>
                        {labelElements}
                    </React.Fragment>
                );
            });
        return (
            <div className="p-grid" style={{ direction: 'rtl' }}>
                {elements}
            </div>
        );
    }

    const renderCharts = () => {
        return (
            <Accordion activeIndex={activeTabIndex} onTabChange={(e) => setActiveTabIndex(e.index)}>
                <AccordionTab header="Contents">{renderChartTabs()}</AccordionTab>
                <AccordionTab header="Conjugation">
                        <AbbreviatedConjugationView conjugation={abbreviatedConjugation!} />
                        <DetailConjugationView conjugation={detailConjugation!} />
                    </AccordionTab>
            </Accordion>
        );
    }

    const renderView = () => {
        return charts.isEmpty() ? renderEmpty() : renderCharts();
    }

    return (renderView());
}

export default observer(MorphologicalChartsView);
