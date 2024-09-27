import React from 'react';

function Upload() {
    return (
        <div className="component-container">
            <h2>Upload a New Document</h2>
            <input type="file" accept=".pdf" />
            <button className="btn">Upload</button>
        </div>
    );
}

export default Upload;
