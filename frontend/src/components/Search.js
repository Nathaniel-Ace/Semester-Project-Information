import React, { useState } from 'react';

function Search() {
    const [query, setQuery] = useState(''); // Speichert den Suchbegriff oder die ID
    const [searchMode, setSearchMode] = useState('all'); // Modus der Suche: all, byId, fulltext
    const [results, setResults] = useState([]);

    const handleSearch = async () => {
        let url = '';

        if (searchMode === 'all') {
            url = 'http://localhost:8080/api/v1/documents/find/all';
        } else if (searchMode === 'byId') {
            if (!query.trim()) {
                alert('Please enter a document ID.');
                return;
            }
            url = `http://localhost:8080/api/v1/documents/find/${query}`;
        } else if (searchMode === 'fulltext') {
            if (!query.trim()) {
                alert('Please enter a search term.');
                return;
            }
            url = `http://localhost:8080/api/v1/documents/search?query=${encodeURIComponent(query)}`;
        }

        try {
            const response = await fetch(url);
            if (response.ok) {
                const data = await response.json();
                // Backend sendet entweder ein Objekt (bei findById) oder eine Liste (bei anderen)
                setResults(Array.isArray(data) ? data : [data]);
            } else {
                alert('Error fetching search results. Please try again.');
            }
        } catch (error) {
            console.error('Error:', error);
            alert('An error occurred while searching.');
        }
    };

    return (
        <div className="component-container">
            <h2>Search Documents</h2>
            <h1>TEST</h1>
            <div className="search-controls">
                <select
                    value={searchMode}
                    onChange={(e) => setSearchMode(e.target.value)}
                >
                    <option value="all">Find All Documents</option>
                    <option value="byId">Find Document by ID</option>
                    <option value="fulltext">Full-Text Search</option>
                </select>
                <input
                    type="text"
                    placeholder={searchMode === 'byId' ? 'Enter Document ID...' : 'Enter search term...'}
                    value={query}
                    onChange={(e) => setQuery(e.target.value)}
                    disabled={searchMode === 'all'} // Deaktiviert das Feld für "Alle Dokumente"
                />
                <button className="btn" onClick={handleSearch}>
                    Search
                </button>
            </div>
            <div className="results">
                {results.length > 0 ? (
                    <ul>
                        {results.map((result, index) => (
                            <li key={index}>
                                <h3>{result.title || `Document ID: ${result.id}`}</h3>
                                <p>{result.ocrText || 'No OCR text available.'}</p>
                                {result.id && <small>Document ID: {result.id}</small>}
                            </li>
                        ))}
                    </ul>
                ) : (
                    <p>No results found.</p>
                )}
            </div>
        </div>
    );
}

export default Search;
