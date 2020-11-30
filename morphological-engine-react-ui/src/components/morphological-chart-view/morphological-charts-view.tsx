import React, { FC, useState } from 'react';
import { Accordion, AccordionTab } from 'primereact/accordion'
import { ToggleSelecter } from '../toggle-selecter';
import { Utils } from '../../utils/utils';
import { AbbreviatedConjugationView } from './abbreviated-conjugation-view';
import { DetailConjugationView } from './detail-conjugation-view';
import Project from '../../store/project';
import { observer } from 'mobx-react-lite';

interface Props {
    project: Project
}

const MorphologicalChartsView: FC<Props> = ({ project }) => {

    const numOfColumns = 8;
    const [activeTabIndex, setActiveTabIndex] = useState(1);
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
