import { createContext } from 'react';
import { List, Map } from 'immutable';
import { InputData } from '../components/model/models';
import { action, computed, makeAutoObservable, observable } from "mobx";
import { ConjugationTemplate } from '../components/model/conjugation-template';
import Emitter from '../services/event-emitter';
import { EmitterConstants } from '../components/emitter-constants';
import { ApplicationController } from '../services/application-controller';
import { MorphologicalChart } from '../components/model/morphological-chart';
import Project from './project';

export class ProjectStore {

    private static createNewProject(counter: number): Project {
        return new Project(`Untitled${counter}`);
    }

    @observable.shallow projects: List<Project> = List([]);
    @observable projectData: Map<string, List<InputData>> = Map();
    @observable charts: Map<string, List<MorphologicalChart>> = Map();
    @observable activeProjectIndex: number = 0;
    private applicationController: ApplicationController;

    constructor() {
        makeAutoObservable(this);
        this.applicationController = new ApplicationController();
        this.initilaizeEmptyProject(1);
    }

    private initilaizeEmptyProject(counter: number) {
        const project = ProjectStore.createNewProject(counter);
        this.projects = this.projects.push(project);
        this.projectData = this.projectData.set(project.id, List());
        this.charts = this.charts.set(project.id, List());
        this.activeProjectIndex = this.projects.size - 1;
    }

    @action addProject = () => {
        const untitledProjects = this.projects.filter((project) => project.projectName.startsWith('Untitled')).size;
        this.initilaizeEmptyProject(untitledProjects + 1);
    }

    @action importProject = (file: any) => {
        const fileReader = new FileReader();
        fileReader.readAsText(file);
        fileReader.onload = () => {
            const content = JSON.parse(fileReader.result as string);
            const conjugationTemplate = ConjugationTemplate.of(content.conjugationTemplate);
            const data = conjugationTemplate.data.map((data) => data.toInputData());
            const project = new Project(content.projectName, false, conjugationTemplate.chartConfiguration);
            this.projects = this.projects.push(project);
            this.projectData = this.projectData.set(project.id, List(data));
            this.charts = this.charts.set(project.id, List());
            this.activeProjectIndex = this.projects.size - 1;
            Emitter.emit(EmitterConstants.PROJECT_IMPORTED, this.size - 1);
        }
        fileReader.onerror = () => {
            console.error(`Error reading file: ${file.name}`)
            throw new Error("Unable to read file");
        }
    }

    @action loadConjugationData = (projectIndex: number) => {
        const project = this.projects.get(projectIndex);
        const projectData = project ? this.projectData.get(project.id)! : List();
        if (project && !projectData.isEmpty) {
            const conjugationData = projectData.map((id) => id.toConjugationData()).toArray();
            return this.applicationController
                .getMorphologicalChart(new ConjugationTemplate(conjugationData))
                .then((charts) => {
                    this.charts = this.charts.set(project.id, List(charts));
                });
        }
    }

    @action setActiveProjectIndex = (index: number) => {
        this.activeProjectIndex = index;
    }

    @action addData = (id: string, index: number, data: InputData) => {
        let values = this.projectData.get(id)!;
        if(index > -1) {
            values = values.set(index, data);
        } else {
            values = values.push(data);
        }
        this.projectData = this.projectData.set(id, values);
    }

    @action removeData = (id: string, rowsToDelete: string[]) => {
        let values = this.projectData.get(id)!;
        values = values.filter((row) => !rowsToDelete.includes(row.id));
        this.projectData = this.projectData.set(id, values);
    }

    @computed getProject(id: string): Project {
        return this.projects.filter((project) => id === project.id).get(0)!;
    }

    @computed getProjectData(id: string): List<InputData> {
        return this.projectData.get(id)!;
    }

    @computed get addedProject(): Project {
        return this.projects.last();
    }

    @computed get size(): number {
        return this.projects.size;
    }
}

export const ProjectContext = createContext({} as ProjectStore);
