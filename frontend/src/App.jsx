import { useState } from 'react';
import './App.css';

function App() {
  const [originalUrl, setOriginalUrl] = useState('');
  const [shortUrl, setShortUrl] = useState('');
  const [error, setError] = useState('');
  const [copyText, setCopyText] = useState('Copy');
  const [isLoading, setIsLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setShortUrl('');
    setCopyText('Copy');
    setIsLoading(true);

    if (!originalUrl) {
      setError('Please enter a URL.');
      setIsLoading(false);
      return;
    }

    try {
      const response = await fetch('https://url-shortener-dli9.onrender.com/api/shorten', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ originalUrl }),
      });

      if (!response.ok) {
        const errorData = await response.json().catch(() => ({ message: 'Something went wrong.' }));
        throw new Error(errorData.message || 'Something went wrong. Please try again.');
      }

      const data = await response.json();
      setShortUrl(data.shortUrl);
    } catch (err) {
      setError(err.message);
    } finally {
      setIsLoading(false);
    }
  };

  const handleCopy = () => {
    navigator.clipboard.writeText(shortUrl);
    setCopyText('Copied!');
    setTimeout(() => setCopyText('Copy'), 2000);
  };

  return (
    <div className="App">
      <header className="App-header">
        <h1>URL Shortener</h1>
        <p className="subtitle">Create short and memorable links in seconds.</p>
        <div className="card">
          <form onSubmit={handleSubmit}>
            <input
              type="url"
              placeholder="Enter a long URL"
              value={originalUrl}
              onChange={(e) => setOriginalUrl(e.target.value)}
              required
            />
            <button type="submit" disabled={isLoading}>
              {isLoading ? <div className="loader"></div> : 'Shorten'}
            </button>
          </form>
          {error && <p className="error">{error}</p>}
          {shortUrl && (
            <div className="result">
              <a href={shortUrl} target="_blank" rel="noopener noreferrer">
                {shortUrl}
              </a>
              <button onClick={handleCopy} className="copy-btn">
                {copyText}
              </button>
            </div>
          )}
        </div>
      </header>
    </div>
  );
}

export default App;
