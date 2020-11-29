import React, { useContext } from 'react';
import { observer } from 'mobx-react-lite';
import { TabView, TabPanel } from 'primereact/tabview';
import ProjectView from './project-view';
import { ProjectContext } from '../store/project-store';

const HomeView = () => {
    const context = useContext(ProjectContext);
    const { projects, activeProjectIndex, setActiveProjectIndex: selectProject } = context;

    const panels = projects.map((project) => {
        return (
            <TabPanel header={project.projectName} key={project.id}>
                <ProjectView projectId={project.id} />
            </TabPanel>
        );
    });

    return (
        <TabView activeIndex={activeProjectIndex} onTabChange={(e) => selectProject(e.index)}>
            {panels}
        </TabView>
    );
};

export default observer(HomeView);
