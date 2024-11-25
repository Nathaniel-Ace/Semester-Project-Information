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
        <header className="app-header">
          <h1>Document Management System</h1>
          <nav>
            <button onClick={() => setActiveComponent('upload')}>Upload Document</button>
            <button onClick={() => setActiveComponent('search')}>Search Documents</button>
            <button onClick={() => setActiveComponent('manage')}>Manage Documents</button>
          </nav>
        </header>
        <main>
          {renderComponent()}
        </main>
      </div>
  );
}

export default App;
