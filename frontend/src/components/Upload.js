import React, { useState } from 'react';

function Upload() {
    const [file, setFile] = useState(null);
    const [title, setTitle] = useState('');

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
                alert('File uploaded successfully!');
            } else {
                alert('File upload failed!');
            }
        } catch (error) {
            console.error('Error uploading file:', error);
            alert('Error uploading file!');
        }
    };

    return (
        <div className="component-container">
            <h2>Upload a New Document</h2>
            <input type="text" placeholder="Enter title" value={title} onChange={handleTitleChange} />
            <input type="file" accept=".pdf" onChange={handleFileChange} />
            <button className="btn" onClick={handleUpload}>Upload</button>
        </div>
    );
}

export default Upload;