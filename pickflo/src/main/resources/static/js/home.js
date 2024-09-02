/**
 * home.html
 */

document.addEventListener('DOMContentLoaded', function() {
	axios.get('/pickflo/api/home/recMovies')
		.then(response => {
			const movies = response.data;
			const movieListDiv = document.querySelector('.movie-list');
			
			if (!movieListDiv) {
                console.error('Error: .movie-list element not found.');
                return;
            }
			
			movies.forEach(movie => {
				const movieCard = document.createElement('div');
				movieCard.className = 'movie-card';

				const img = document.createElement('img');
				img.src = movie.movieImg; 
				img.alt = 'Movie Image';
				img.className = 'poster-image'; 
				
				movieCard.appendChild(img);

				movieListDiv.appendChild(movieCard);
			});
		})
		.catch(error => {
			console.error('Error fetching movies:', error);
		});
});

/*
document.addEventListener('DOMContentLoaded', function() {
	const loading = document.getElementById('loading');
	const movieList = document.querySelector('.movie-list');
	
	let currentPage = 1;
	let isLoading = false;
	
	function loadMovies(page) {
        if (isLoading) return; 
        isLoading = true;
	
	axios.get('/pickflo/api/home/recMovies', { params: { page: page, limit: 18 } })
            .then(response => {
                const movies = response.data;
                
                if (movies.length === 0) {
                    window.removeEventListener('scroll', handleScroll);
                    return;
                }
                
                const movieListDiv = document.querySelector('.movie-list');
                
                if (!movieListDiv) {
                    console.error('Error: .movie-list element not found.');
                    return;
                }
                
                movies.forEach(movie => {
                    const movieCard = document.createElement('div');
                    movieCard.className = 'movie-card';
                    
                    const img = document.createElement('img');
                    img.src = movie.movieImg; 
                    img.alt = 'Movie Image';
                    img.className = 'poster-image'; 
                    
                    movieCard.appendChild(img);
                    movieListDiv.appendChild(movieCard);
                });
                
                isLoading = false; // Allow new requests
            })
            .catch(error => {
                console.error('Error fetching movies:', error);
                isLoading = false; // Allow new requests
            });
    }
    
    function handleScroll() {
        if (window.innerHeight + window.scrollY >= document.body.offsetHeight - 100) {
            currentPage++;
            loadMovies(currentPage);
        }
    }
    
    // Initial load
    loadMovies(currentPage);
    
    // Load more movies when scrolled to bottom
    window.addEventListener('scroll', handleScroll);
});
*/