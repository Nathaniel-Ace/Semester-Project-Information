import React, { useState } from 'react';
import './App.css';
import Upload from './components/Upload';
import Search from './components/Search';
import Manage from './components/Manage';

function App() {
  const [activeComponent, setActiveComponent] = useState('upload');

  const renderComponent = () => {
    switch (activeComponent) {
      case 'upload':
        return <Upload />;
      case 'search':
        return <Search />;
      case 'manage':
        return <Manage />;
      default:
        return <Upload />;
    }
  };

  return (
      <div className="app-background">
        {/* Fixiertes Banner oben */}
        <header className="app-header">
          <h1>Document Management System</h1>
          <nav>
            <button
                className={activeComponent === 'upload' ? 'active' : ''}
                onClick={() => setActiveComponent('upload')}
            >
              Upload Document
            </button>
            <button
                className={activeComponent === 'search' ? 'active' : ''}
                onClick={() => setActiveComponent('search')}
            >
              Search Documents
            </button>
            <button
                className={activeComponent === 'manage' ? 'active' : ''}
                onClick={() => setActiveComponent('manage')}
            >
              Manage Documents
            </button>
          </nav>
        </header>

        {/* Hauptinhalt */}
        <main>
          {renderComponent()}
        </main>
      </div>
  );
}

export default App;
