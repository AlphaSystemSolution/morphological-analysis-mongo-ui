import * as React from 'react';

import 'primereact/resources/themes/saga-green/theme.css';
import 'primereact/resources/primereact.min.css';
import 'primeicons/primeicons.css';
import 'primeflex/primeflex.css';

import './assets/font.css';
import './assets/App.css';

import AppToolbar from './components/app-toolbar';
import HomeView from './components/home-view';
import { ProjectStore, ProjectContext } from './store/project-store';

const App = () => {

  return (
    <div className="App">
      <ProjectContext.Provider value={new ProjectStore()}>
        <AppToolbar />
        <div>&nbsp;</div>
        <HomeView />
      </ProjectContext.Provider>
    </div>
  );
}

export default App;
