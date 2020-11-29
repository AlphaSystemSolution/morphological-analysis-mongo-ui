import React, { useContext, useState, useEffect } from 'react';
import { observer } from 'mobx-react-lite';
import { TabView, TabPanel } from 'primereact/tabview';
import ProjectView from './project-view';
import { ProjectContext } from '../store/project-store';
import Emitter from '../services/event-emitter';
import { EmitterConstants } from './emitter-constants';

const HomeView = () => {
    const [activeTabIndex, setActiveTabIndex] = useState(0);
    const context = useContext(ProjectContext);
    const { projects } = context;

    useEffect(() => {
        Emitter.on(EmitterConstants.PROJECT_CREATED, (index: number) => setActiveTabIndex(index));

        return () => {
            Emitter.off(EmitterConstants.PROJECT_CREATED);
        }
    }, []);

    const panels = projects.map((project) => {
        return (
            <TabPanel header={project.projectName} key={project.id}>
                <ProjectView projectId={project.id} />
            </TabPanel>
        );
    });

    return (
        <TabView activeIndex={activeTabIndex} onTabChange={(e) => setActiveTabIndex(e.index)}>
            {panels}
        </TabView>
    );
};

export default observer(HomeView);
