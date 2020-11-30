import React, { useContext, useEffect, useState } from 'react';
import { observer } from 'mobx-react-lite';
import { TabView, TabPanel } from 'primereact/tabview';
import ProjectView from './project-view';
import { ProjectContext } from '../store/project-store';
import Emitter from '../services/event-emitter';
import { EmitterConstants } from './emitter-constants';

const HomeView = () => {
    const [refreshTab, setRefreshTab] = useState(false);
    const context = useContext(ProjectContext);
    const { projects, activeProjectIndex, setActiveProjectIndex: selectProject } = context;

    useEffect(() => {
        // after save tab title doesn't get refreshed automatically,
        // this is just to refresh tab title
        Emitter.on(EmitterConstants.PROJECT_SAVED, () => setRefreshTab(!refreshTab));
        return () => {
            Emitter.off(EmitterConstants.PROJECT_SAVED);
        }
    }, [refreshTab]);

    return (
        <TabView activeIndex={activeProjectIndex} onTabChange={(e) => selectProject(e.index)}>
            {
              projects.map((project) => {
                return (
                    <TabPanel header={project.projectName} key={project.id}>
                        <ProjectView project={project} />
                    </TabPanel>
                );
            })  
            }
        </TabView>
    );
};

export default observer(HomeView);
