import * as React from 'react';
import { Accordion, AccordionTab } from 'primereact/accordion'
import { MorphologicalChart } from '../model/morphological-chart';
import { ToggleSelecter } from '../toggle-selecter';
import { Utils } from '../../utils/utils';

interface Props {
    charts: MorphologicalChart[]
}

interface State {
    activeIndex: number
    selectedIndex: number
}

export class MorphologicalChartsView extends React.Component<Props, State> {

    constructor(props: Props) {
        super(props);

        this.onChartSelected = this.onChartSelected.bind(this);

        this.state = {
            activeIndex: 0,
            selectedIndex: 0
        };
    }

    private onChartSelected(payload: any) {
        console.info(JSON.stringify(payload));
        this.setState({
            selectedIndex: payload.index
        });
    }

    private renderChartTabs() {
        const charts = this.props.charts.map((c) => c.copy());
        const labels = Utils.chunkArray(charts.map((chart) => chart.label), 8);
        const elements =
            labels.map((labelArray, parentIndex) => {
                const labelElements = labelArray.map((label, index) => {
                    const key = (parentIndex * 10) + index;
                    return (
                        <td key={"chart-label-row" + key}>
                            <ToggleSelecter value={label} index={key} className="chartSelector ui-button p-button-raised"
                                userKey={label.id} checked={this.state.selectedIndex === key} onChange={this.onChartSelected} />&nbsp;
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
        return (
            <React.Fragment>
                <Accordion activeIndex={this.state.activeIndex} onTabChange={(e) => this.setState({ activeIndex: e.index })}>
                    <AccordionTab header="Contents">{this.renderChartTabs()}</AccordionTab>
                </Accordion>
            </React.Fragment>
        );
    }
}