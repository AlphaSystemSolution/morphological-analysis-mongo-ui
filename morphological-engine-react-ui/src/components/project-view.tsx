import React, { useContext, useState } from 'react';
import { observer } from 'mobx-react-lite';
import { TabPanel, TabView } from 'primereact/tabview';
import { ProjectContext } from '../store/project-store';
import InputTable from './input-table';

interface Props {
    projectId: string
}

const ProjectView: React.FC<Props> = ({ projectId }) => {
    const [activeTabIndex, setActiveTabIndex] = useState(0);
    const [disableConjugationTab, setDisableConjugationTab] = useState(true);

    const context = useContext(ProjectContext);
    const project = context.getProject(projectId);
    
    return (
        <>
            <TabView activeIndex={activeTabIndex} onTabChange={(e) => setActiveTabIndex(e.index)}>
                <TabPanel header="Table">
                    <InputTable projectId={project.id} />
                </TabPanel>
                <TabPanel header="Conjugation" disabled={disableConjugationTab}>
                    <p>MorphologicalChartsView</p>
                </TabPanel>
            </TabView>
        </>
    );
};

export default observer(ProjectView);
