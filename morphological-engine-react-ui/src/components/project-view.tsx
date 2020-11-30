import React, { useEffect, useState } from 'react';
import { observer } from 'mobx-react-lite';
import { TabPanel, TabView } from 'primereact/tabview';
import InputTable from './input-table';
import Project from '../store/project';
import MorphologicalChartsView from './morphological-chart-view/morphological-charts-view';

interface Props {
    project: Project
}

const ProjectView: React.FC<Props> = ({ project }) => {
    const [activeTabIndex, setActiveTabIndex] = useState(0);

    useEffect(() => {
        project.loadConjugationData();

        return () => { }
    }, [project])

    return (
        <>
            <TabView activeIndex={activeTabIndex} onTabChange={(e) => setActiveTabIndex(e.index)}>
                <TabPanel header="Table">
                    <InputTable project={project} />
                </TabPanel>
                <TabPanel header="Conjugation">
                    <MorphologicalChartsView project={project} />
                </TabPanel>
            </TabView>
        </>
    );
};

export default observer(ProjectView);
