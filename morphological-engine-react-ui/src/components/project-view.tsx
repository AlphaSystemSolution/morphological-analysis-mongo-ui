import React, { useContext, useState } from 'react';
import { observer } from 'mobx-react-lite';
import { TabPanel, TabView } from 'primereact/tabview';
import { ProjectContext } from '../store/project-store';
/*import { ApplicationController } from '../services/application-controller';
import Emitter from '../services/event-emitter';
import { EmitterConstants } from './emitter-constants';
import InputTable from './input-table';
import { ChartConfiguration } from './model/chart-configuration';
import { ConjugationTemplate } from './model/conjugation-template';
import { InputData } from './model/models';
import { MorphologicalChartsView } from './morphological-chart-view/morphological-charts-view';*/

interface Props {
    projectId: string
}

const ProjectView: React.FC<Props> = ({ projectId }) => {
    const [activeTabIndex, setActiveTabIndex] = useState(0);
    const [disableConjugationTab, setDisableConjugationTab] = useState(true);

    const context = useContext(ProjectContext);
    const project = context.getProject(projectId);

    // <InputTable initialData={this.props.project!.data} chartConfiguration={this.props.project.chartConfiguration} />
    // <MorphologicalChartsView charts={this.state.charts} />

    return (
        <>
            <TabView activeIndex={activeTabIndex} onTabChange={(e) => setActiveTabIndex(e.index)}>
                <TabPanel header="Table">
                    <p>{project.projectName}</p>
                </TabPanel>
                <TabPanel header="Conjugation" disabled={disableConjugationTab}>
                    <p>MorphologicalChartsView</p>
                </TabPanel>
            </TabView>
        </>
    );
};

export default observer(ProjectView);
