import React, { useState } from 'react';
import { observer } from 'mobx-react-lite';
import { TabPanel, TabView } from 'primereact/tabview';
import InputTable from './input-table';
import Project from '../store/project';

interface Props {
    project: Project
}

const ProjectView: React.FC<Props> = ({ project }) => {
    const [activeTabIndex, setActiveTabIndex] = useState(0);
    const [disableConjugationTab, setDisableConjugationTab] = useState(true);

    return (
        <>
            <TabView activeIndex={activeTabIndex} onTabChange={(e) => setActiveTabIndex(e.index)}>
                <TabPanel header="Table">
                    <InputTable project={project} />
                </TabPanel>
                <TabPanel header="Conjugation" disabled={disableConjugationTab}>
                    <p>MorphologicalChartsView</p>
                </TabPanel>
            </TabView>
        </>
    );
};

export default observer(ProjectView);
