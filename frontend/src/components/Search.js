import React from 'react';

function Search() {
    return (
        <div className="component-container">
            <h2>Search Documents</h2>
            <input type="text" placeholder="Enter search term..." />
            <button className="btn">Search</button>
        </div>
    );
}

export default Search;
