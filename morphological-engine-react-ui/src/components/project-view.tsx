import * as React from 'react';

import { TabView, TabPanel } from 'primereact/tabview';
import { Project } from './model/models';
import InputTable from './input-table';

interface Props { }

interface State {
    activeTabIndex: number,
    projects: Project[]
}

export class ProjectView extends React.Component<Props, State> {

    constructor(props: Props) {
        super(props);

        this.state = {
            activeTabIndex: 0,
            projects: [new Project("Untitled1", "Untitled1.json")]
        }
    }

    render() {
        const panels =
            this.state.projects.map((project, index) => {
                return <TabPanel header={project.projectName} key={"project-" + index}>
                    <InputTable initialData={project.data} />
                </TabPanel>;
            });

        return (
            <TabView activeIndex={this.state.activeTabIndex} onTabChange={(e) => this.setState({ activeTabIndex: e.index })}>
                {panels}
            </TabView>
        );
    }
}