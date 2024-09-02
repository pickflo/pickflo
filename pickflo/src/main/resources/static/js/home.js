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
				
				img.setAttribute('data-movie-id', movie.movieId);
				img.setAttribute('data-bs-toggle', 'modal');
				img.setAttribute('data-bs-target', '#modalMovieDetails');
				
				movieCard.appendChild(img);

				movieListDiv.appendChild(movieCard);
			});
			
			bindPosterImageClickEvent();
		})
		.catch(error => {
			console.error('Error fetching movies:', error);
		});
}); 
/*
document.addEventListener('DOMContentLoaded', function() {
    let page = 1;
    const limit = 10;
    let hasMore = true;
    const movieListDiv = document.querySelector('.movie-list');
    const loadingDiv = document.getElementById('loading');
    
    const loadMovies = () => {
        if (!hasMore) return; // If no more movies to load, return
        
        loadingDiv.style.display = 'block';
        
        axios.get(`/pickflo/api/home/recMovies?page=${page}&limit=${limit}`)
            .then(response => {
                const movies = response.data.content; // Adjust according to the structure of your response
                const totalPages = response.data.totalPages;
                
                movies.forEach(movie => {
                    const movieCard = document.createElement('div');
                    movieCard.className = 'movie-card';
                    
                    const img = document.createElement('img');
                    img.src = movie.movieImg; 
                    img.alt = 'Movie Image';
                    img.className = 'poster-image'; 
                    
                    img.setAttribute('data-movie-id', movie.movieId);
                    img.setAttribute('data-bs-toggle', 'modal');
                    img.setAttribute('data-bs-target', '#modalMovieDetails');
                    
                    movieCard.appendChild(img);
                    movieListDiv.appendChild(movieCard);
                });
                
                page++;
                hasMore = page <= totalPages;
                
                loadingDiv.style.display = 'none';
                bindPosterImageClickEvent();
            })
            .catch(error => {
                console.error('Error fetching movies:', error);
                loadingDiv.style.display = 'none';
            });
    };

    loadMovies();

    window.addEventListener('scroll', () => {
        if (window.innerHeight + window.scrollY >= document.body.offsetHeight) {
            loadMovies();
        }
    });
}); */

