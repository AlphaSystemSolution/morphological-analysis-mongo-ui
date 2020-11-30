import { createContext } from 'react';
import { List } from 'immutable';
import { action, computed, makeAutoObservable, observable } from "mobx";
import Emitter from '../services/event-emitter';
import { EmitterConstants } from '../components/emitter-constants';
import Project from './project';
import { InputData } from '../components/model/models';
import { ChartConfiguration } from '../components/model/chart-configuration';

export class ProjectStore {

    private static createNewProject(counter: number): Project {
        return new Project(`Untitled${counter}`);
    }

    @observable.shallow projects: List<Project> = List([]);
    @observable activeProjectIndex: number = 0;
    private transientProjects = 0;

    constructor() {
        makeAutoObservable(this);
        this.initilaizeEmptyProject(1);
    }

    private initilaizeEmptyProject(counter: number) {
        const project = ProjectStore.createNewProject(counter);
        this.projects = this.projects.push(project);
        this.activeProjectIndex = this.projects.size - 1;
        this.transientProjects += 1;
    }

    @action addProject = () => {
        this.transientProjects += 1;
        this.initilaizeEmptyProject(this.transientProjects);
    }

    @action importProject = (file: any) => {
        const fileReader = new FileReader();
        fileReader.readAsText(file);
        fileReader.onload = () => {
            const content = JSON.parse(fileReader.result as string);
            const data = content.data.map((src: any)=> InputData.of(src));
            const project = new Project(content.projectName, false, ChartConfiguration.of(content.configuration), List(data));
            this.projects = this.projects.push(project);
            this.activeProjectIndex = this.projects.size - 1;
            Emitter.emit(EmitterConstants.PROJECT_IMPORTED, this.size - 1);
        }
        fileReader.onerror = () => {
            console.error(`Error reading file: ${file.name}`)
            throw new Error("Unable to read file");
        }
    }

    @action setActiveProjectIndex = (index: number) => {
        this.activeProjectIndex = index;
    }

    @computed getProject(id: string): Project {
        return this.projects.filter((project) => id === project.id).get(0)!;
    }

    @computed get addedProject(): Project {
        return this.projects.last();
    }

    @computed get size(): number {
        return this.projects.size;
    }
}

export const ProjectContext = createContext({} as ProjectStore);
