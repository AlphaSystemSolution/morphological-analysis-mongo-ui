import * as React from 'react';

import { TabView, TabPanel } from 'primereact/tabview';
import { Project } from './model/models';
import Emitter from '../services/event-emitter';
import { AppToolbar } from './app-toolbar';
import { ProjectView } from './project-view';

interface Props { }

interface State {
    activeTabIndex: number,
    projects: Project[]
}

export class HomeView extends React.Component<Props, State> {

    constructor(props: Props) {
        super(props);

        this.state = {
            activeTabIndex: 0,
            projects: [new Project("Untitled1", "Untitled1.json")]
        }
    }

    componentDidMount() {
        Emitter.on(AppToolbar.NEW_PROJECT_ACTION, () => this.createNewProject());
        Emitter.on(AppToolbar.IMPORT_PROJECT_ACTION, (project) => this.importProject(project));
    }

    componentWillUnmount() {
        Emitter.off(AppToolbar.NEW_PROJECT_ACTION);
        Emitter.off(AppToolbar.IMPORT_PROJECT_ACTION);
    }

    private createNewProject() {
        const p = this.state.projects.filter((project) => project.projectName.startsWith('Untitled'));
        const projectName = `Untitled${p.length + 1}`;
        this.state.projects.push(new Project(projectName, `${projectName}.json`))
        this.setState({
            projects: this.state.projects
        });
    }

    private importProject(project: Project) {
        const projects = [...this.state.projects];
        projects.push(project);
        this.setState({
            projects: projects,
            activeTabIndex: projects.length - 1
        });
    }

    render() {
        const panels =
            this.state.projects.map((project, index) => {
                return (
                    <TabPanel header={project.projectName} key={"project-" + index}>
                        <ProjectView project={project} />
                    </TabPanel>);
            });

        return (
            <TabView activeIndex={this.state.activeTabIndex} onTabChange={(e) => this.setState({ activeTabIndex: e.index })}>
                {panels}
            </TabView>
        );
    }
}