import React, { useContext, FC } from 'react';

import { observer } from 'mobx-react-lite';
import { TabPanel } from 'primereact/tabview';
import ProjectView from './project-view';
import { ProjectContext } from '../store/project-store';

interface Props {
    projectId: string
}

const ProjectPanelView: FC<Props> = ({ projectId }) => {
    const context = useContext(ProjectContext);
    const project = context.getProject(projectId);
    console.log(`>>>>> ${project.projectName}`);

    return (
        <TabPanel header={project.projectName}>
            <ProjectView projectId={project.id} />
        </TabPanel>
    );
};

export default observer(ProjectPanelView);
