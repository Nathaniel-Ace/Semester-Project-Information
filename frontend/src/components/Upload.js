import React, { useState } from 'react';

function Upload() {
    const [file, setFile] = useState(null);

    const handleFileChange = (event) => {
        setFile(event.target.files[0]);
    };

    const test = () => {
        console.log("test");
    }

    const handleUpload = async () => {
        if (!file) {
            alert('Please select a file to upload.');
            return;
        }

        const formData = new FormData();
        formData.append('file', file);

        try {
            const response = await fetch('http://localhost:8081/api/v1/documents/upload', {
                method: 'POST',
                body: formData,
            });

            if (response.ok) {
                alert('File uploaded successfully.');
            } else {
                alert('Failed to upload file.');
            }
        } catch (error) {
            console.error('Error uploading file:', error);
            alert('Error uploading file.');
        }
    };

    return (
        <div className="component-container">
            <h2>Upload aA New Document</h2>
            <input type="file" accept=".pdf" onChange={handleFileChange} />
            <button className="btn" onClick={test}>Upload</button>
        </div>
    );
}

export default Upload;