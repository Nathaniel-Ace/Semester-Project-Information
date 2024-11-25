import React, { useState } from 'react';

function Upload() {
    const [file, setFile] = useState(null);

    const handleFileChange = (event) => {
        setFile(event.target.files[0]);
    };

    const handleUpload = async () => {
        console.log('Upload button clicked');

        if (!file) {
            alert('Please select a file first!');
            return;
        }

        const formData = new FormData();
        formData.append('file', file);

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
            <input type="file" accept=".pdf" onChange={handleFileChange} />
            <button className="btn" onClick={handleUpload}>Upload</button>
        </div>
    );
}

export default Upload;