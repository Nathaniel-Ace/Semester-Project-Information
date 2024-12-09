import React, { useState } from 'react';
import './Search.css';

function Search() {
    const [query, setQuery] = useState('');
    const [searchMode, setSearchMode] = useState('all');
    const [results, setResults] = useState([]);
    const [lastSearchMode, setLastSearchMode] = useState('all'); // Neuer State, um den letzten Suchmodus zu speichern

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
                setResults(Array.isArray(data) ? data : [data]);
                setLastSearchMode(searchMode); // Suchmodus speichern, der zuletzt verwendet wurde
            } else {
                alert('Error fetching search results. Please try again.');
            }
        } catch (error) {
            console.error('Error:', error);
            alert('An error occurred while searching.');
        }
    };

    const cleanTitle = (title) => {
        return title ? title.replace('/tmp/', '') : 'Untitled Document';
    };

    return (
        <div className="search-container">
            <div className="search-sidebar">
                <h2>Search Documents</h2>
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
                    placeholder={
                        searchMode === 'all'
                            ? ''
                            : searchMode === 'byId'
                                ? 'Enter Document ID...'
                                : 'Enter search term...'
                    }
                    value={query}
                    onChange={(e) => setQuery(e.target.value)}
                    disabled={searchMode === 'all'}
                />
                <button className="btn" onClick={handleSearch}>
                    Search
                </button>
            </div>
            <div className="search-results">
                {results.length > 0 ? (
                    results.map((result, index) => (
                        <div key={index} className="result-card">
                            <div className="result-content">
                                <h3>{cleanTitle(result.title)}</h3>
                                {lastSearchMode !== 'all' && (
                                    <p>{result.ocrText || 'No OCR text available.'}</p>
                                )}
                                <small>Document ID: {result.id}</small>
                            </div>
                        </div>
                    ))
                ) : (
                    <p>No results found.</p>
                )}
            </div>
        </div>
    );
}

export default Search;
