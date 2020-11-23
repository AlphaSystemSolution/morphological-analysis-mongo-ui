import * as React from 'react';
import { Accordion, AccordionTab } from 'primereact/accordion'
import { MorphologicalChart } from '../model/morphological-chart';
import { ToggleSelecter } from '../toggle-selecter';
import { Utils } from '../../utils/utils';
import { AbbreviatedConjugationView } from './abbreviated-conjugation-view';
import { AbbreviatedConjugation } from '../model/abbreviated-conjugation';

interface Props {
    charts: MorphologicalChart[]
}

interface State {
    activeTabIndex: number
    selectedChartIndex: number
    abbreviatedConjugation?: AbbreviatedConjugation
}

export class MorphologicalChartsView extends React.Component<Props, State> {

    private static NUM_OF_COLUMNS = 8;

    constructor(props: Props) {
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
            abbreviatedConjugation: this.props.charts[index].abbreviatedConjugation,
            activeTabIndex: 1
        });
    }


    private renderChartTabs() {
        const charts = this.props.charts.map((c) => c.copy());
        const labels = Utils.chunkArray(charts.map((chart) => chart.label), MorphologicalChartsView.NUM_OF_COLUMNS);
        const elements =
            labels.map((labelArray, parentIndex) => {
                const labelElements = labelArray.map((label, index) => {
                    const key = (parentIndex * MorphologicalChartsView.NUM_OF_COLUMNS) + index;
                    return (
                        <td key={"chart-label-row" + key}>
                            <ToggleSelecter value={label} index={key} className="chartSelector ui-button p-button-raised"
                                userKey={label.id} checked={this.state.selectedChartIndex === key} onChange={this.onChartSelected} />&nbsp;
                        </td>
                    );
                });
                return <tr key={"chart-label" + parentIndex}>{labelElements}</tr>;
            });
        return (<table style={{ 'direction': 'rtl' }}>
            <tbody>{elements}</tbody>
        </table>);
    }

    render() {
        const abbreviatedConjugation = this.state.abbreviatedConjugation ? this.state.abbreviatedConjugation : this.props.charts[0].abbreviatedConjugation;
        return (
            <React.Fragment>
                <Accordion activeIndex={this.state.activeTabIndex} onTabChange={(e) => this.setState({ activeTabIndex: e.index })}>
                    <AccordionTab header="Contents">{this.renderChartTabs()}</AccordionTab>
                    <AccordionTab header="Conjugation">
                        <AbbreviatedConjugationView conjugation={abbreviatedConjugation} />
                    </AccordionTab>
                </Accordion>
            </React.Fragment>
        );
    }
}