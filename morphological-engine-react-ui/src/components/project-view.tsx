import * as React from 'react';

import * as path from 'path';
import { TabView, TabPanel } from 'primereact/tabview';
import { InputData, Project } from './model/models';
import InputTable from './input-table';
import Emitter from '../services/event-emitter';
import { AppToolbar } from '../AppToolbar';
import { RootLetters } from './model/root-letters';
import { ConjugationTemplate } from "../components/model/conjugation-template";
import { ApplicationController } from '../services/application-controller';
import { ArabicLetter } from './model/arabic-letter';
import { NamedTemplate } from './model/named-template';
import { VerbalNoun } from './model/verbal-noun';

interface Props { }

interface State {
    activeTabIndex: number,
    projects: Project[]
}

export class ProjectView extends React.Component<Props, State> {

    private applicationController: ApplicationController

    constructor(props: Props) {
        super(props);

        this.applicationController = new ApplicationController();
        this.state = {
            activeTabIndex: 0,
            projects: [new Project("Untitled1", "Untitled1.json")]
        }
    }

    componentDidMount() {
        Emitter.on(AppToolbar.NEW_PROJECT_ACTION, () => this.createNewProject());
        Emitter.on(AppToolbar.IMPORT_PROJECT_ACTION, (file) => this.importProject(file));
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

    private importProject(file: File) {
        const fileReader = new FileReader();
        fileReader.readAsText(file);
        fileReader.onload = () => {
            const content = fileReader.result as string;
            const ct: ConjugationTemplate = Object.assign(new ConjugationTemplate(), JSON.parse(content));
            let data: InputData[] = [];
            ct.data.forEach((item) => {
                const rootLetters = item.rootLetters
                data.push(new InputData(
                    new RootLetters(
                        ArabicLetter.getByName(rootLetters.firstRadical),
                        ArabicLetter.getByName(rootLetters.secondRadical),
                        ArabicLetter.getByName(rootLetters.thirdRadical),
                        !rootLetters.fourthRadical ? ArabicLetter.TATWEEL : ArabicLetter.getByName(rootLetters.fourthRadical)
                    ),
                    NamedTemplate.getByName(item.template),
                    item.translation,
                    item.configuration.removePassiveLine,
                    item.configuration.skipRuleProcessing,
                    item.verbalNouns.map((name) => VerbalNoun.getByName(name)),
                    item.id
                ));
            });

            const project = new Project(path.basename(file.name, ".json"), file.name, data, ct.chartConfiguration)
            const projects = [...this.state.projects];
            projects.push(project);
            this.setState({
                projects: projects,
                activeTabIndex: projects.length - 1
            });
        };
        fileReader.onerror = () => {
            console.error(`Error reading file: ${file.name}`)
            throw new Error("Unable to read file");
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