import * as React from 'react';

import { TabView, TabPanel } from 'primereact/tabview';
import { Project } from './model/models';
import InputTable from './input-table';
import Emitter from '../services/event-emitter';

interface Props { }

interface State {
    activeTabIndex: number,
    projects: Project[]
}

export class ProjectView extends React.Component<Props, State> {

    constructor(props: Props) {
        super(props);

        this.createNewProject = this.createNewProject.bind(this);

        this.state = {
            activeTabIndex: 0,
            projects: [new Project("Untitled1", "Untitled1.json")]
        }
    }

    componentDidMount() {
        Emitter.on('create-new-project', () => this.createNewProject());
    }

    componentWillUnmount() {
        Emitter.off('create-new-project')
    }

    private createNewProject() {
        const p = this.state.projects.filter((project) => project.projectName.startsWith('Untitled'));
        const projectName = `Untitled${p.length + 1}`;
        this.state.projects.push(new Project(projectName, `${projectName}.json`))
        this.setState({
            projects: this.state.projects
        });
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