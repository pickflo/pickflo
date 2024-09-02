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