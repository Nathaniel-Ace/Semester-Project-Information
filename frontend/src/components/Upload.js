import React, { useState } from 'react';

function Upload() {
    const [file, setFile] = useState(null);
    const [title, setTitle] = useState('');
    const [uploadResult, setUploadResult] = useState(null); // Für die Upload-Antwort

    const handleFileChange = (event) => {
        setFile(event.target.files[0]);
    };

    const handleTitleChange = (event) => {
        setTitle(event.target.value);
    };

    const handleUpload = async () => {
        console.log('Upload button clicked');

        if (!file) {
            alert('Please select a file first!');
            return;
        }

        if (!title) {
            alert('Please enter a title!');
            return;
        }

        const formData = new FormData();
        formData.append('file', file);
        formData.append('title', title);

        try {
            const response = await fetch('http://localhost:8080/api/v1/documents/upload', {
                method: 'POST',
                body: formData,
            });

            if (response.ok) {
                const data = await response.json();
                setUploadResult(data); // Antwort speichern
                alert('File uploaded successfully!');
            } else {
                const errorMessage = await response.text();
                alert(`File upload failed: ${errorMessage}`);
            }
        } catch (error) {
            console.error('Error uploading file:', error);
            alert('Error uploading file!');
        }
    };

    return (
        <div className="component-container">
            <h2>Upload a New Document</h2>
            <input
                type="text"
                placeholder="Enter title"
                value={title}
                onChange={handleTitleChange}
            />
            <input
                type="file"
                accept=".pdf"
                onChange={handleFileChange}
            />
            <button className="btn" onClick={handleUpload}>Upload</button>

            {uploadResult && (
                <div className="upload-result">
                    <h3>Upload Details:</h3>
                    <p><strong>ID:</strong> {uploadResult.id}</p>
                    <p><strong>Title:</strong> {uploadResult.title}</p>
                    <p><strong>File URL:</strong> <a href={uploadResult.fileUrl} target="_blank" rel="noopener noreferrer">{uploadResult.fileUrl}</a></p>
                </div>
            )}
        </div>
    );
}

export default Upload;
